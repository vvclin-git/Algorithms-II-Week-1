import edu.princeton.cs.algs4.*;
//import edu.princeton.cs.algs4.Digraph;
//import edu.princeton.cs.algs4.Graph;
//import edu.princeton.cs.algs4.StdIn;
//import edu.princeton.cs.algs4.StdOut;
public class SAP3 {
	private SAPBFS sapbfs;
	private Digraph G;
	   // constructor takes a digraph (not necessarily a DAG)
	   public SAP3(Digraph G) {
		   this.G = G;		   
	   }
	   
	   //length of shortest ancestral path between v and w; -1 if no such path
	   public int length(int v, int w) {
		   sapbfs = new SAPBFS(G, v, w);
		   return sapbfs.getSapDist();
	   }
//
//	   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	   public int ancestor(int v, int w) {		   
		   sapbfs = new SAPBFS(G, v, w);
		   return sapbfs.getAnscetor();		   
	   }
//
//	   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	   public int length(Iterable<Integer> v, Iterable<Integer> w) {		   
		   int minDist = Integer.MAX_VALUE;
		   int tmpDist;
		   for (int s1 : v) {
			   for (int s2 : v) {
				   tmpDist = length(s1, s2);
				   if ( tmpDist <= minDist) {
					   minDist = tmpDist;
				   }
			   }
		   }
		   if (minDist == Integer.MAX_VALUE) {
			   return -1;
		   }
		   else {
			   return minDist;
		   }
	   }
//
//	   // a common ancestor that participates in shortest ancestral path; -1 if no such path
	   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		   int minDist = Integer.MAX_VALUE;
		   int tmpDist;
		   int sapancestor = -1;
		   for (int s1 : v) {
			   for (int s2 : v) {
				   tmpDist = length(s1, s2);
				   if ( tmpDist <= minDist) {
					   minDist = tmpDist;
					   sapancestor = ancestor(s1, s2);
				   }
			   }
		   }		   
		   return sapancestor;		   
	   }

	   // do unit testing of this class
	   public static void main(String[] args) {
//		   In in = new In(args[0]);
		   In in = new In("wordnet\\digraph1.txt");
		   Digraph G = new Digraph(in);
		    SAP3 sap = new SAP3(G);
		    while (!StdIn.isEmpty()) {
		        int v = StdIn.readInt();
		        int w = StdIn.readInt();
		        int length   = sap.length(v, w);
		        int ancestor = sap.ancestor(v, w);
		        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		    }
//		    Bag<Integer> v = new Bag<Integer>();
//		    Bag<Integer> w = new Bag<Integer>();
//		    v.add(7);
//		    v.add(8);
//		    w.add(4);
//		    w.add(9);
//		    w.add(11);
//	        int length   = sap.length(v, w);
//	        int ancestor = sap.ancestor(v, w);
//		    StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	   }
}
