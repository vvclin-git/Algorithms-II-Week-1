import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.*;
//import edu.princeton.cs.algs4.Digraph;
//import edu.princeton.cs.algs4.Graph;
//import edu.princeton.cs.algs4.StdIn;
//import edu.princeton.cs.algs4.StdOut;
public class SAP {
	private static final int INFINITY = Integer.MAX_VALUE;	
	private Digraph G;	
	private HashMap<HashSet<Integer>, Integer> sapDist;
	private HashMap<HashSet<Integer>, Integer> sapAncestor;
	private int[] distV, distW, pathV, pathW;
//	private int[] marked;
	private boolean[] marked, markedSAP;
//	private boolean marked[];
	   // constructor takes a digraph (not necessarily a DAG)
	   public SAP(Digraph G) {
		   this.G = G;
		   this.distV = new int[G.V()];
		   this.distW = new int[G.V()];
		   this.pathV = new int[G.V()];
		   this.pathW = new int[G.V()];
//		   this.marked = new int[G.V()];
		   this.marked = new boolean[G.V()];
		   this.markedSAP = new boolean[G.V()];
		   this.sapDist = new HashMap<HashSet<Integer>, Integer>();
		   this.sapAncestor = new HashMap<HashSet<Integer>, Integer>();
		   for (int v = 0; v < G.V(); v++) {
			   this.distV[v] = INFINITY;
			   this.distW[v] = INFINITY;
		   }
	   }	   
	   //length of shortest ancestral path between v and w; -1 if no such path
	   public int length(int v, int w) {		   
		   HashSet<Integer> query = new HashSet<Integer>();
		   query.add(v);
		   query.add(w);
		   if (!sapDist.containsKey(query)) {
//			   resetDist();		   
			   bfs(v, distV, pathV);			   			   
			   bfs(w, distW, pathW);			   
//			   printArray(distV);
//			   printArray(distW);
			   sap(v, w);
			   return sapDist.get(query);
		   }
		   else {
			   return sapDist.get(query);
		   }
	   }
//
//	   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	   public int ancestor(int v, int w) {		   
		   HashSet<Integer> query = new HashSet<Integer>();
		   query.add(v);
		   query.add(w);
		   if (!sapAncestor.containsKey(query)) {
//			   resetDist();		   
			   bfs(v, distV, pathV);			   
			   bfs(w, distW, pathW);
			   sap(v, w);
			   return sapAncestor.get(query);
		   }
		   else {
			   return sapAncestor.get(query);
		   }
	   }
//
//	   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	   public int length(Iterable<Integer> v, Iterable<Integer> w) {
		   int minDist = INFINITY;
		   int tmpDist;
		   if (v == null | w == null) {
			   throw new java.lang.NullPointerException();
		   }
		   for (Integer v1 : v) {
			   for (Integer w1 : w) {
				   if (v1 == null | w1 == null) {
					   throw new java.lang.NullPointerException();
				   }
				   tmpDist = length(v1, w1);
				   if (tmpDist < minDist & tmpDist > 0) {
					   minDist = tmpDist;
				   }
			   }
		   }
		   if (minDist != INFINITY) {
			   return minDist;
		   }
		   else {
			   return -1;
			   
		   }		   
	   }
//
//	   // a common ancestor that participates in shortest ancestral path; -1 if no such path
	   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		   int minDist = INFINITY;
		   int tmpDist;
		   int ancestor = -1;
		   if (v == null | w == null) {
			   throw new java.lang.NullPointerException();
		   }
		   for (Integer v1 : v) {
			   for (Integer w1 : w) {
				   if (v1 == null | w1 == null) {
					   throw new java.lang.NullPointerException();
				   }
				   tmpDist = length(v1, w1);
				   if (tmpDist < minDist & tmpDist > 0) {
					   minDist = tmpDist;
					   ancestor = ancestor(v1, w1);
				   }
			   }
		   }
		   return ancestor;	   
	   }
	   private void resetDist() {
		   for (int v = 0; v < G.V(); v++) {
			   distV[v] = INFINITY;
			   distW[v] = INFINITY;
		   }
	   }
	   private void sap(int v, int w) {
		   int minDist = INFINITY;		   
		   int tmpDist;
		   int ancestor = -1;
		   int x;
		   HashSet<Integer> query = new HashSet<Integer>();
		   query.add(v);
		   query.add(w);
		   Queue<Integer> pathFind = new Queue<Integer>();
		   pathFind.enqueue(v);
		   pathFind.enqueue(w);
		   markedSAP[v] = true;
		   markedSAP[w] = true;
//		   marked[v] = v;
//		   marked[w] = w;
		   //printArray(marked);
		   if (distV[w] != INFINITY | distW[v] != INFINITY) {
			   if (distV[w] < distW[v]) {
				   minDist = distV[w];
				   ancestor = w;
			   }
			   else {
				   minDist = distW[v];
				   ancestor = v;
			   }
			   sapDist.put(query, minDist);
			   sapAncestor.put(query, ancestor);
		   }
		   while (!pathFind.isEmpty()) {
			   x = pathFind.dequeue();
//			   StdOut.println(x);
//			   printArray(markedSAP);
			   
			   for (int x1 : G.adj(x)) {
				   if (!markedSAP[x1]) {
					   if (distV[x1] != INFINITY & distW[x1] != INFINITY) {
							 tmpDist = distV[x1] + distW[x1];
//							 StdOut.println(x1 + "| distV: " + distV[x1] + ", distW: " + distW[x1] + 
//									 ", tmpDist: " + tmpDist + 
//									 ", minDist: " + minDist);
//							   printArray(markedSAP);
							 if (tmpDist <= minDist) {
								 minDist = tmpDist;
								 ancestor = x1;
							 }
							 else {
							 sapDist.put(query, minDist);
							   sapAncestor.put(query, ancestor);
//							   StdOut.println("early escape");
							   return;
						 }
					   }
					   pathFind.enqueue(x1);
					   markedSAP[x1] = true;
				   }				   
			   }
		   }
		   if (ancestor == -1) {
			   sapDist.put(query, -1);
			   sapAncestor.put(query, ancestor);
			   return;
		   }
		   else {
			   sapDist.put(query, minDist);
			   sapAncestor.put(query, ancestor);
		   }
	   }
	   private void sap1(int v, int w) {
		   int minDist = INFINITY;
		   int tmpDist;
		   int ancestor = -1;
		   HashSet<Integer> query = new HashSet<Integer>();
		   query.add(v);
		   query.add(w);		   
		   for (int i = 0; i < G.V(); i++) {
			   tmpDist = distV[i] + distW[i];			   
			   if (tmpDist < minDist & tmpDist >= 0) {
				   minDist = tmpDist;
				   ancestor = i;
			   }
		   }
		   if (ancestor != -1) {
			   sapDist.put(query, minDist);
			   sapAncestor.put(query, ancestor);
		   }
		   else {
			   sapDist.put(query, -1);
			   sapAncestor.put(query, -1);
		   }
	   }
//	   private void bfs1(int s, int[] distTo, int[] pathTo) {		   
//		   Queue<Integer> q = new Queue<Integer>();
//		   marked[s] = s;
//		   distTo[s] = 0;
//		   pathTo[s] = 0;
//		   q.enqueue(s);
//		   while (!q.isEmpty()) {
//			   int v = q.dequeue();
//			   for (int w : G.adj(v)) {
//				   if (marked[w] != s) {					   
//					   distTo[w] = distTo[v] + 1;
//					   pathTo[w] = v;
//					   marked[w] = s;
//					   
//					   q.enqueue(w);
//				   }
//			   }
//		   }
//	   }
	   private void bfs(int s, int[] distTo, int[] pathTo) {		   
		   marked = new boolean[distTo.length];
		   Queue<Integer> q = new Queue<Integer>();
		   marked[s] = true;
		   distTo[s] = 0;
		   pathTo[s] = 0;
		   q.enqueue(s);
		   while (!q.isEmpty()) {
			   int v = q.dequeue();
			   for (int w : G.adj(v)) {
				   if (!marked[w]) {					   
					   distTo[w] = distTo[v] + 1;
					   pathTo[w] = v;
					   marked[w] = true;
					   q.enqueue(w);
				   }
			   }
		   }
	   }
	   private Iterable<Integer> pathTo(int v, int[] distTo, int[] edgeTo) {	        
	        Stack<Integer> path = new Stack<Integer>();
	        int x;
	        for (x = v; distTo[x] != 0; x = edgeTo[x])
	            path.push(x);
	        path.push(x);
	        return path;
	    }
	   private void printArray(int[] array) {
		   for (int i = 0; i < array.length; i++) {
			   StdOut.print(array[i] + ", ");
		   }
		   StdOut.println();
	   }
	   private void printArray(boolean[] array) {
		   for (int i = 0; i < array.length; i++) {
			   StdOut.print(array[i] + ", ");
		   }
		   StdOut.println();
	   }
	   // do unit testing of this class
	   public static void main(String[] args) {
//		   In in = new In(args[0]);
		   In in = new In("wordnet\\digraph3.txt");
		   Digraph G = new Digraph(in);		   
		    SAP sap = new SAP(G);
		    StdOut.println("length: " + sap.length(2, 6) + " ancestor: "+ sap.ancestor(2, 6));
//		    while (!StdIn.isEmpty()) {
//		        int v = StdIn.readInt();
//		        int w = StdIn.readInt();
//		        int length   = sap.length(v, w);
//		        int ancestor = sap.ancestor(v, w);
//		        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//		    }
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
