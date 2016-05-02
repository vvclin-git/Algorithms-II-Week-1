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
	public SAPBFS(Digraph G, int s1, int s2) {
		marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        from = new int[G.V()];
        path = new HashMap<Integer, ArrayList<Bag<Integer>>>();
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
        sapbfs(G, s1, s2);
	}
	private void sapbfs(Digraph G, int v, int w) {
		int ancestor;
		HashSet<Integer> query = new HashSet<Integer>();
		query.add(v);
		query.add(w);
		//bfs from v
		Queue<Integer> q = new Queue<Integer>();
		q.enqueue(v);	       
		int distV = 0;				
		path.put(v, new ArrayList<Bag<Integer>>());	       
		path.get(v).add(new Bag<Integer>());
		path.get(v).get(distV).add(v);		
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
					mergePath(from[vAdj], from[vTmp], vAdj, path);
				}
			}
		}
		// find SAP
		distV = 0;
		distW = 0;
		for (Bag<Integer> bv : path.get(v)) {				
			for (Bag<Integer> bw : path.get(w)) {				
				ancestor = coV(bv, bw);
				if (ancestor != -1) {
					sapDist.put(query, distW + distV);
					sapAncestor.put(query, ancestor)
				}
				distW += 1;
			}
			distV += 1;
		}
		
    }
	private void sapbfs(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {				
		
	}
	private int distVTo(int v) {
		for (Bag<Integer> b : pathV.get(v)) {
			for (int v1 : b) {
				if (v == v1) {
					return pathV.get(v).indexOf(b);
				}
			}
		}
		return -1;
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
			for (int i = joinInd; i < path.get(v).size(); i++) {
				path.get(w).add(path.get(v).get(i));
			}
		}		
	}
	private void sap(int v, int w) {
		for (Bag<Integer> b : path.get(v)) {
			
		}
	}
	private int coV(Bag<Integer> bag1, Bag<Integer> bag2) {
		for (int v1 : bag1) {
			for (int v2 : bag2) {
				if (v1 == v2) {
					return v1;
				}
			}
		}
		return -1;
	}
	public int getSapDist() {
		return sapDist;
	}
	public int getAnscetor() {
		return anscetor;
	}

	
}
