import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;
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
	public int numVisited;
	   // constructor takes a digraph (not necessarily a DAG)
	   public SAP(Digraph G) {
		   this.G = G;		   
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
			   FindSAP findSAP = new FindSAP(v, w);
//			   StdOut.println("time elapsed in single length: " + (System.nanoTime() - startTime));
			   sapDist.put(query, findSAP.getSAPDist());
			   sapAncestor.put(query, findSAP.getSAPAncestor());
			   numVisited = findSAP.getNumVisited();
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
			   FindSAP findSAP = new FindSAP(v, w);
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
		   FindSAP findSAP = new FindSAP(v, w);
//		   StdOut.println("time elapsed in multiple length: " + (System.nanoTime() - startTime));
		   return findSAP.getSAPDist();
		   	   
	   }
	   
//
//	   // a common ancestor that participates in shortest ancestral path; -1 if no such path
	   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {//		  
		   FindSAP findSAP = new FindSAP(v, w);
		   return findSAP.getSAPAncestor();
	   }
	   

	   private class FindSAP {
		   //		   long startTime = System.nanoTime();
		   int minDist = INFINITY;		   
		   int tmpDist;
		   int ancestor = -1;		   		   
		   HashMap<Integer, Integer> distV, distW;
		   int numVisited = 0;
		   int qVSize = 0, qWSize = 0;
		   public FindSAP(int v, int w) {
			   int v1, w1;
			   boolean exit = false;			   
			   distV = new HashMap<Integer, Integer>();
			   distW = new HashMap<Integer, Integer>();
//			   HashSet<Integer> lastVisited = new HashSet<Integer>();
			   Queue<Integer> qV = new Queue<Integer>();
			   Queue<Integer> qW = new Queue<Integer>();
			   qV.enqueue(v);
			   distV.put(v, 0);
			   qW.enqueue(w);
			   distW.put(w, 0);			   
			   if (distW.containsKey(v)) {
				   ancestor = v;
				   minDist = 0;
				   return;
			   }
			   sapBFS(qV, qW);
		   }
		   //			   StdOut.println(System.nanoTime() - startTime);

		   
		   public FindSAP(Iterable<Integer> v, Iterable<Integer> w) {
			   int v1, w1;
			   distV = new HashMap<Integer, Integer>();
			   distW = new HashMap<Integer, Integer>();
			   //			   HashSet<Integer> lastVisited = new HashSet<Integer>();
			   Queue<Integer> qV = new Queue<Integer>();
			   Queue<Integer> qW = new Queue<Integer>();
			   for (int s : v) {
				   qV.enqueue(s);
				   distV.put(s, 0);
			   }
			   for (int s : w) {
				   qW.enqueue(s);
				   distW.put(s, 0);
				   if (distV.containsKey(s)) {
					   ancestor = s;
					   minDist = 0;
					   return;
				   }
			   }			   
			   sapBFS(qV, qW);

		   }
		   private void sapBFS(Queue<Integer> qV, Queue<Integer> qW) {
			   int v1, w1;
			   int nextDist;
			   boolean exitV = false, exitW = false;
//			   while(((!qV.isEmpty()) | (!qW.isEmpty()))) {
//			   while((!exitV & !exitW) | !qV.isEmpty() | !qW.isEmpty()) {
			   HashSet<Integer> visited = new HashSet<Integer>();
			   visited.add(qV.peek());
			   visited.add(qW.peek());
			   while((!qV.isEmpty() & !exitV) | (!qW.isEmpty() & !exitW)) {
				   if (!qV.isEmpty() & !exitV) {
					   v1 = qV.dequeue();
					   // early exit condition
					   if (distV.get(v1) == minDist) {						  
						   exitV = true;						  					  
					   }
					   if (!exitV) {
						   for (int v2 : G.adj(v1)) {							   
							   if (!distV.containsKey(v2)) {
								   numVisited += 1;
								   visited.add(v2);
								   nextDist = distV.get(v1) + 1;
								   distV.put(v2, nextDist);								   
								   if (distW.containsKey(v2)) {
									   tmpDist = nextDist + distW.get(v2);
									   if (tmpDist <= minDist) {
										   ancestor = v2;
										   minDist = tmpDist;
									   }
								   }
								   qV.enqueue(v2);
							   }
						   }
					   }
					   
				   }
				   if (!qW.isEmpty() & !exitW) {
					   w1 = qW.dequeue();
					   // early exit condition
					   if (distW.get(w1) == minDist) {
						   exitW = true;						   
					   }
					   if (!exitW) {
						   for (int w2 : G.adj(w1)) {							   
							   if (!distW.containsKey(w2)) {
								   numVisited += 1;
								   visited.add(w2);
								   nextDist = distW.get(w1) + 1;
								   distW.put(w2, nextDist);
//								   if (distV.get(w2) != null) {
								   if (distV.containsKey(w2)) {
									   tmpDist = distV.get(w2) + nextDist;
									   if (tmpDist <= minDist) {
										   ancestor = w2;
										   minDist = tmpDist;
									   }
								   }
								   qW.enqueue(w2);
							   }
						   }
					   }
				   }				   
			   }
//			   StdOut.println("number of visited nodes: " + visited.size());
//			   StdOut.println("visited nodes: " + visited.toString());
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
		   public int getNumVisited() {
			   return numVisited;
		   }
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
	   private void printHashMap(HashMap<Integer, Integer> h) {		   	   
		   for (int k : h.keySet()) {			   
			   StdOut.print("{" + k + ": " + h.get(k) + "}, ");
		   }
		   StdOut.println();
	   }
	   // do unit testing of this class
	   public static void main(String[] args) {
//		   In in = new In(args[0]);
		   
//		   int numVisited = 0;
//		   In in = new In("wordnet\\digraph_test2.txt");
//		   Digraph G = new Digraph(in);		   
//		   SAP sap = new SAP(G);		   
//		   StdOut.println("length: " + sap.length(1, 2) + " ancestor: "+ sap.ancestor(1, 2));
//		   numVisited += sap.numVisited;
//		   StdOut.println(numVisited);
		   
//		   int numVisited = 0;
//		   In in = new In("wordnet\\digraph-wordnet.txt");
//		   Digraph G = new Digraph(in);		   
//		   SAP sap = new SAP(G);		   
//		   StdOut.println("length: " + sap.length(23231, 23302) + " ancestor: "+ sap.ancestor(23231, 23302));
//		   numVisited += sap.numVisited;
//		   StdOut.println(numVisited);
		   
		   In in = new In("wordnet\\digraph-wordnet.txt");
		   Digraph G = new Digraph(in);		   
		   SAP sap = new SAP(G);
		   Random rand = new Random();		   
		   int min = 0;
		   int max = 82191;
		   int numCalls = 100000;
		   int randomNum1, randomNum2;
		   int numVisited = 0;
		   long startTime = System.nanoTime();
		   for (int i = 0; i < numCalls; i++) {			   
			   randomNum1 = rand.nextInt((max - min) + 1) + min;
			   randomNum2 = rand.nextInt((max - min) + 1) + min;
			   sap.length(randomNum1, randomNum2);
			   numVisited += sap.numVisited;
		   }
		   long time = System.nanoTime() - startTime;
		   double callSec = numCalls  / (double) time;
		   StdOut.println("time elapsed in during calls: " + time);
		   StdOut.println("calls / sec: " + callSec* 1000000000);		   
		   StdOut.println("avg. vertices visited per call: " +  (numVisited / (double) numCalls));
		   
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
