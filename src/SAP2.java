import edu.princeton.cs.algs4.*;
//import edu.princeton.cs.algs4.Digraph;
//import edu.princeton.cs.algs4.Graph;
//import edu.princeton.cs.algs4.StdIn;
//import edu.princeton.cs.algs4.StdOut;
public class SAP2 {
	Graph uGraph;
	Digraph dGraph;
	Digraph dGraphR;
	BreadthFirstPaths bfs;
	int wMin;
	int minDist = Integer.MAX_VALUE;
	
	   // constructor takes a digraph (not necessarily a DAG)
	   public SAP2(Digraph G) {
		   dGraph = G;		   
		   dGraphR = G.reverse();
		   uGraph = d2uGraph(G);
		   
	   }
	   // to convert a digraph into a unigraph
	   private Graph d2uGraph(Digraph dG) {		   
		   Graph G = new Graph(dG.V());
		   for (int v = 0; v < dG.V(); v++) {
			   for (int w : dG.adj(v)) {
				   G.addEdge(v, w);
				   G.addEdge(w, v);
			   }			   
		   }
		   return G;
	   }
	   //length of shortest ancestral path between v and w; -1 if no such path
	   public int length(int v, int w) {
		   bfs = new BreadthFirstPaths(uGraph, v);
		   if (bfs.hasPathTo(w)) {
			   return bfs.distTo(w);
		   }
		   return -1;
	   }
//
//	   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	   public int ancestor(int v, int w) {		   
		   int p0 = v;
		   int path[];
		   boolean reachable = false;		   
		   int dist = length(v, w);
		   if (dist != -1) {
			   path = new int[dist + 1];
			   int pathI = 0;			   
			   for (int p : bfs.pathTo(w)) {				   
				   path[pathI] = p;
				   pathI++;
			   }			   
			   for (int i = 1; i < path.length; i++) {
				   reachable = false;
				   for (int vNext : dGraph.adj(p0)) {
					   if (vNext == path[i]) {
						   reachable = true;
						   p0 = vNext;
						   break;
					   }					   
				   }
				   if (!reachable) {
					   return p0;
				   }
			   }
		   }
		   return -1;
	   }
//
//	   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	   public int length(Iterable<Integer> v, Iterable<Integer> w) {		   
		   bfs = new BreadthFirstPaths(uGraph, v);		   
		   int dist;
		   for (int w1 : w) {
			   if (bfs.hasPathTo(w1)) {
				   dist = bfs.distTo(w1);
				   if (dist <= minDist) {
					   minDist = dist;
					   wMin = w1;
				   }
			   }
		   }
		   if (minDist < Integer.MAX_VALUE) {
			   return minDist;
		   }
		   else {
			   return -1;
		   }
	   }
//
//	   // a common ancestor that participates in shortest ancestral path; -1 if no such path
	   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		   int path[];
		   int p0;
		   boolean reachable = false;
		   if (minDist == Integer.MAX_VALUE) {
			  minDist = length(v, w); 
		   }
		   if (minDist != -1) {
			   path = new int[minDist + 1];
			   int pathI = 0;			   
			   for (int p : bfs.pathTo(wMin)) {				   
				   path[pathI] = p;
				   pathI++;
			   }
			   p0 = path[0];
			   for (int i = 1; i < path.length; i++) {
				   reachable = false;
				   for (int vNext : dGraph.adj(p0)) {
					   if (vNext == path[i]) {
						   reachable = true;
						   p0 = vNext;
						   break;
					   }					   
				   }
				   if (!reachable) {
					   return p0;
				   }
			   }			   
		   }		  
		   return -1;
	   }

	   // do unit testing of this class
	   public static void main(String[] args) {
//		   In in = new In(args[0]);
		   In in = new In("wordnet\\digraph1.txt");
		   Digraph G = new Digraph(in);
		    SAP2 sap = new SAP2(G);
//		    while (!StdIn.isEmpty()) {
//		        int v = StdIn.readInt();
//		        int w = StdIn.readInt();
//		        int length   = sap.length(v, w);
//		        int ancestor = sap.ancestor(v, w);
//		        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//		    }
		    Bag<Integer> v = new Bag<Integer>();
		    Bag<Integer> w = new Bag<Integer>();
		    v.add(7);
		    v.add(8);
		    w.add(4);
		    w.add(9);
		    w.add(11);
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
		    StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	   }
}
