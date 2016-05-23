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
	private HashMap<Integer, Integer> distV, distW;
	private ArrayList<Bag<Integer>> markedV;
	private ArrayList<Bag<Integer>> markedW;
	private HashSet<Integer> marked;

	   // constructor takes a digraph (not necessarily a DAG)
	   public SAP(Digraph G) {
		   this.G = G;
		   this.distV = new HashMap<Integer, Integer>();
		   this.distW = new HashMap<Integer, Integer>();
		   this.marked = new HashSet<Integer>();		   
		   this.markedV = new ArrayList<Bag<Integer>>();
		   this.markedW = new ArrayList<Bag<Integer>>();
		   this.sapDist = new HashMap<HashSet<Integer>, Integer>();
		   this.sapAncestor = new HashMap<HashSet<Integer>, Integer>();
	   }	   
	   //length of shortest ancestral path between v and w; -1 if no such path
	   public int length(int v, int w) {		   
//		   long startTime = System.nanoTime();
		   HashSet<Integer> query = new HashSet<Integer>();
		   query.add(v);
		   query.add(w);
		   if (!sapDist.containsKey(query)) {
			   markedV.clear();
			   markedW.clear();
			   bfs(v, distV, markedV);			   			   
			   bfs(w, distW, markedW);
//			   StdOut.println(markedUnion.toString());
			   FindSAP findSAP = new FindSAP();
//			   StdOut.println("time elapsed in single length: " + (System.nanoTime() - startTime));
			   sapDist.put(query, findSAP.getSAPDist());
			   sapAncestor.put(query, findSAP.getSAPAncestor());
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
			   markedV.clear();
			   markedW.clear();
			   bfs(v, distV, markedV);			   			   
			   bfs(w, distW, markedW);
			   FindSAP findSAP = new FindSAP();
			   sapDist.put(query, findSAP.getSAPDist());
			   sapAncestor.put(query, findSAP.getSAPAncestor());
			   return sapAncestor.get(query);
		   }
		   else {
			   return sapAncestor.get(query);
		   }
	   }
	   
//
//	   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	   public int length(Iterable<Integer> v, Iterable<Integer> w) {
//		   long startTime = System.nanoTime();
		   markedV.clear();
		   markedW.clear();
		   bfs(v, distV, markedV);			   			   
		   bfs(w, distW, markedW);
		   FindSAP findSAP = new FindSAP();
//		   StdOut.println("time elapsed in multiple length: " + (System.nanoTime() - startTime));
		   return findSAP.getSAPDist();
		   	   
	   }
	   
//
//	   // a common ancestor that participates in shortest ancestral path; -1 if no such path
	   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		   markedV.clear();
		   markedW.clear();
		   bfs(v, distV, markedV);			   			   
		   bfs(w, distW, markedW);
		   FindSAP findSAP = new FindSAP();
		   return findSAP.getSAPAncestor();
	   }
	   

	   private class FindSAP {
//		   long startTime = System.nanoTime();
		   int minDist = INFINITY;		   
		   int tmpDist;
		   int ancestor = -1;
		   int distFromV = 0, distFromW = 0;		   
		   public FindSAP() {
//			   printMarkedS(markedV);
//			   printMarkedS(markedW);
			   marked.clear();
//			   while(distFromV <= minDist & distFromV < markedV.size()) {				   
				   while(distFromV < markedV.size()) {
				   StdOut.print(distFromV + " | ");
				   for(int x : markedV.get(distFromV)) {
					   if (!marked.contains(x)) {
						   StdOut.print(x + ", ");
						   if (distV.containsKey(x) & distW.containsKey(x)) {
							   tmpDist = distV.get(x) + distW.get(x);
							   marked.add(x);
							   if (tmpDist == 0) {
								   minDist = tmpDist;
								   ancestor = x;								   
								   return;						   
							   }
							   if (tmpDist <= minDist) {
								   minDist = tmpDist;
								   ancestor = x;
							   }
						   }
					   }					   
				   }
				   StdOut.println();
				   distFromV += 1;
			   }
//			   while(distFromW <= minDist & distFromW < markedW.size()) {				   
				   while(distFromW < markedW.size()) {
				   StdOut.print(distFromW + " | ");
				   for(int x : markedW.get(distFromW)) {
					   if (!marked.contains(x)) {
						   StdOut.print(x + ", ");
						   if (distV.containsKey(x) & distW.containsKey(x)) {
							   tmpDist = distV.get(x) + distW.get(x);
							   marked.add(x);
							   if (tmpDist == 0) {
								   minDist = tmpDist;
								   ancestor = x;								   
								   return;						   
							   }
							   if (tmpDist <= minDist) {
								   minDist = tmpDist;
								   ancestor = x;
							   }
						   }
					   }
					   
				   }
				   StdOut.println();
				   distFromW += 1;
			   }
				   
		   }

		   public int getSAPDist() {
			   if (minDist != INFINITY) {
				   return minDist;
			   }
			   else {
				   return -1;
			   }
		   }
		   public int getSAPAncestor() {
			   if (minDist != INFINITY) {
				   return ancestor;
			   }
			   else {
				   return -1;
			   }
		   }
	   }	   
	   private void bfs(int s, HashMap<Integer, Integer> distTo, ArrayList<Bag<Integer>> markedS) {		   
//		   long startTime = System.nanoTime();
		   marked.clear();
		   distTo.clear();
		   Queue<Integer> q = new Queue<Integer>();
		   marked.add(s);
		   distTo.put(s, 0);
		   markedS.add(new Bag<Integer>());
		   markedS.get(0).add(s);
		   q.enqueue(s);		   		   
		   while (!q.isEmpty()) {
			   int v = q.dequeue();
			   for (int w : G.adj(v)) {
				   if (!marked.contains(w)) {
					   if (markedS.size() - 1 < (distTo.get(v) + 1)) {
						   markedS.add(new Bag<Integer>());
					   }					   
					   distTo.put(w, distTo.get(v) + 1);
					   markedS.get(distTo.get(w)).add(w);
					   marked.add(w);
					   q.enqueue(w);
				   }
			   }
		   }
//		   StdOut.println("time elapsed in single bfs: " + (System.nanoTime() - startTime));
	   }
	   private void bfs(Iterable<Integer> s, HashMap<Integer, Integer> distTo, ArrayList<Bag<Integer>> markedS) {		   
//		   long startTime = System.nanoTime();
		   marked.clear();
		   distTo.clear();
		   Queue<Integer> q = new Queue<Integer>();
		   markedS.add(new Bag<Integer>());
		   for (Integer s1 : s) {
			   if (s1 == null) {
				   throw new java.lang.NullPointerException();
			   }			   
			   
			   marked.add(s1);
			   distTo.put(s1,  0);			   
			   markedS.get(0).add(s1);
			   q.enqueue(s1);
		   }		   
		   while (!q.isEmpty()) {
			   int v = q.dequeue();			   			   
			   for (int w : G.adj(v)) {
				   if (!marked.contains(w)) {
					   if (markedS.size() - 1 < (distTo.get(v) + 1)) {
						   markedS.add(new Bag<Integer>());
					   }
					   distTo.put(w, distTo.get(v) + 1);
					   markedS.get(distTo.get(w)).add(w);
					   marked.add(w);					   
					   q.enqueue(w);
				   }
			   }
		   }
//		   StdOut.println("time elapsed in multiple bfs: " + (System.nanoTime() - startTime));
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
	   private void printMarkedS(ArrayList<Bag<Integer>> markedS) {
		   StdOut.println();
		   for (Bag<Integer> b : markedS) {
			   StdOut.print("{");
			   for (int x : b) {
				   StdOut.print(x + ", ");
			   }
			   StdOut.print("}, ");
		   }
		   StdOut.println();
	   }
	   // do unit testing of this class
	   public static void main(String[] args) {
//		   In in = new In(args[0]);
		   In in = new In("wordnet\\digraph_test2.txt");
		   Digraph G = new Digraph(in);		   
		   SAP sap = new SAP(G);
		   StdOut.println("length: " + sap.length(1, 2) + " ancestor: "+ sap.ancestor(1, 2));
//		    while (!StdIn.isEmpty()) {
//		        int v = StdIn.readInt();
//		        int w = StdIn.readInt();
//		        int length   = sap.length(v, w);
//		        int ancestor = sap.ancestor(v, w);
//		        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//		    }
//		    Bag<Integer> v = new Bag<Integer>();
//		    Bag<Integer> w = new Bag<Integer>();
//		    v.add(11);
//		    w.add(12);
//		    v.add(1);
//		    v.add(2);
//		    w.add(2);
//		    w.add(3);
//		    w.add(4);
//	        int length   = sap.length(v, w);	        
//	        int ancestor = sap.ancestor(v, w);
//		    StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	   }
}
