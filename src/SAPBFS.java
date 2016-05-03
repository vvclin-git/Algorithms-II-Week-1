import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.*;
public class SAPBFS {
	private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s->v path?
    private int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
    private int[] distTo;      // distTo[v] = length of shortest s->v path
	private int[] from;
	private HashMap<HashSet<Integer>, Integer> sapDist;
	private HashMap<HashSet<Integer>, Integer> sapAncestor;
	private HashMap<Integer, ArrayList<Bag<Integer>>> path;	
	Digraph G;
	public SAPBFS(Digraph G) {
		marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        from = new int[G.V()];
        path = new HashMap<Integer, ArrayList<Bag<Integer>>>();
        this.G = G;
        sapDist = new HashMap<HashSet<Integer>, Integer>();
        sapAncestor = new HashMap<HashSet<Integer>, Integer>();
        path = new HashMap<Integer, ArrayList<Bag<Integer>>>();
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
        
	}
	private void sapbfs(int v, int w) {		
		//bfs from v
		Queue<Integer> q = new Queue<Integer>();
		q.enqueue(v);	       
		int distV = 0;				
		path.put(v, new ArrayList<Bag<Integer>>());	       
		path.get(v).add(new Bag<Integer>());
		path.get(v).get(distV).add(v);
		from[v] = v;
		while (!q.isEmpty()) {			
			int vTmp = q.dequeue();			
			distV += 1;
			path.get(v).add(new Bag<Integer>());
			for (int vAdj : G.adj(vTmp)) {
				path.get(v).get(distV).add(vAdj);
				if (!marked[vAdj]) {						   
					marked[vAdj] = true;
					from[vAdj] = v;					
					q.enqueue(vAdj);
				}
				else {					
					mergePath(from[vAdj], from[vTmp], vAdj, path);
				}
			}
		}
		//bfs from w
		q.enqueue(w);
		int distW = 0;				
		path.put(w, new ArrayList<Bag<Integer>>());	       
		path.get(w).add(new Bag<Integer>());
		path.get(w).get(distW).add(w);
		from[w] = w;
		while (!q.isEmpty()) {			
			int vTmp = q.dequeue();
			distW += 1;
			path.get(w).add(new Bag<Integer>());
			for (int vAdj : G.adj(vTmp)) {
				path.get(w).get(distW).add(vAdj);
				if (!marked[vAdj]) {						   
					marked[vAdj] = true;
					from[vAdj] = w;					
					q.enqueue(vAdj);
				}
				else {
//					StdOut.println("Merge at " + vAdj);
//					printPath(v);
//					printPath(w);
//					printPath(from[vAdj]);
//					printPath(from[vTmp]);					
//					StdOut.println();
					//mergePath(from[vAdj], from[vTmp], vAdj, path);
					mergePath(from[vAdj], from[vTmp], vAdj, path);
					printPath(v);
					printPath(w);
//					printPath(from[vAdj]);
//					printPath(from[vTmp]);					
					StdOut.println();
				}
			}
		}
		printPath(v);
		printPath(w);
		// find SAP
		sap(v, w);		
    }
	private void sapbfs(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {				
		
	}
	
	private void mergePath(int v, int w, int w1, HashMap<Integer, ArrayList<Bag<Integer>>> path) {
		// join path of v & w at the joint point w1, w -> v
		int joinInd = 0;
		for (Bag<Integer> b : path.get(v)) {
			for (int v1 : b) {
				if (v1 == w1) {
					joinInd = path.get(v).indexOf(b) + 1;					
				}
			}			
		}		
		for (int i = joinInd; i < path.get(v).size(); i++) {			
			path.get(w).add(path.get(v).get(i));
		}		
	}
	private void sap(int v, int w) {
		int distV = 0;
		int distW = 0;
		int ancestor;		
		HashSet<Integer> query = new HashSet<Integer>();
		query.add(v);
		query.add(w);
		for (Bag<Integer> bv : path.get(v)) {
			distW = 0;
			for (Bag<Integer> bw : path.get(w)) {				
				ancestor = coItem(bv, bw);
				if (ancestor != -1) {					
					sapDist.put(query, distW + distV);
					sapAncestor.put(query, ancestor);
					return;
				}
				distW += 1;
			}
			distV += 1;
		}
		sapDist.put(query, -1);
		sapAncestor.put(query, -1);
	}
	private int coItem(Bag<Integer> bag1, Bag<Integer> bag2) {
		for (int v1 : bag1) {
			for (int v2 : bag2) {
				if (v1 == v2) {
					return v1;
				}
			}
		}
		return -1;
	}
	public int getSapDist(int v, int w) {
		HashSet<Integer> query = new HashSet<Integer>();
		query.add(v);
		query.add(w);		
		if (sapDist.containsKey(query)) {
			return sapDist.get(query);
		}
		else {
			sapbfs(v, w);
			return sapDist.get(query);
		}		
	}
	public int getSapAncestor(int v, int w) {
		HashSet<Integer> query = new HashSet<Integer>();
		query.add(v);
		query.add(w);		
		if (sapAncestor.containsKey(query)) {
			return sapAncestor.get(query);
		}
		else {
			sapbfs(v, w);
			return sapAncestor.get(query);
		}
	}
	private void printPath(int v) {
		StdOut.println();
		StdOut.print("Path from " + v + ": ");
		for (Bag<Integer> i : path.get(v)) {
			printBag(i);
			StdOut.print("->");
		}		
	}
	private void printBag(Bag<Integer> i) {
		StdOut.print("(");
		for (int e : i) {
			StdOut.print(e + ",");
		}
		StdOut.print(")");
	}
}
