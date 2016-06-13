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
	private String lastQuery; 
	private int lastDist, lastAncestor; 
	   // constructor takes a digraph (not necessarily a DAG)
	   public SAP(Digraph G) {
		   this.G = new Digraph(G);
		   this.lastQuery = "";
	   }	   
	   //length of shortest ancestral path between v and w; -1 if no such path
	   public int length(int v, int w) {		   
		   String query;
		   if (v > w) {
			   query = v + " " + w ;
		   }
		   else {
			   query = w + " " + v ;
		   }		   		   
		   if (!lastQuery.equals(query)) {
			   FindSAP findSAP = new FindSAP(v, w);
			   lastQuery = query;
			   lastDist = findSAP.getSAPDist();
			   lastAncestor = findSAP.getSAPAncestor();
//			   StdOut.println("new query | " + query );
			   return lastDist;
		   }
		   else {
//			   StdOut.println("old query | " + query);
			   return lastDist;
		   }
		   // no cache version
//		   FindSAP findSAP = new FindSAP(v, w);
//		   return findSAP.getSAPDist();
	   }
//
//	   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	   public int ancestor(int v, int w) {		   
		   String query;
		   if (v > w) {
			   query = v + " " + w ;
		   }
		   else {
			   query = w + " " + v ;
		   }		   		   
		   if (!lastQuery.equals(query)) {
			   FindSAP findSAP = new FindSAP(v, w);
			   lastQuery = query;
			   lastDist = findSAP.getSAPDist();
			   lastAncestor = findSAP.getSAPAncestor();
//			   StdOut.println("new query | " + query );
			   return lastAncestor;
		   }
		   else {
//			   StdOut.println("old query | " + query);
			   return lastAncestor;
		   }
		   // no cache version
//		   FindSAP findSAP = new FindSAP(v, w);
//		   return findSAP.getSAPAncestor();
	   }
	   
//
//	   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	   public int length(Iterable<Integer> v, Iterable<Integer> w) {
		   String query;
		   ArrayList<Integer> vArray = new ArrayList<Integer>();
		   ArrayList<Integer> wArray = new ArrayList<Integer>();
		   for (int v1 : v) {
			   vArray.add(v1);
		   }
		   for (int w2 : w) {
			   wArray.add(w2);
		   }
		   vArray.sort(null);
		   wArray.sort(null);
		   if (vArray.size() > wArray.size()) {
			   query = vArray.toString() + " " + wArray.toString();
		   }
		   else {
			   query = wArray.toString() + " " + vArray.toString();
		   }
		   if (!lastQuery.equals(query)) {
			   FindSAP findSAP = new FindSAP(v, w);
			   lastQuery = query;
			   lastDist = findSAP.getSAPDist();
			   lastAncestor = findSAP.getSAPAncestor();
//			   StdOut.println("new query | " + query );
			   return lastDist;
		   }
		   else {
//			   StdOut.println("old query | " + query);
			   return lastDist;
		   }
		   // no cache version
//		   FindSAP findSAP = new FindSAP(v, w);
//		   return findSAP.getSAPDist();
   
	   }
	   
//
//	   // a common ancestor that participates in shortest ancestral path; -1 if no such path
	   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {//
		   String query;
		   ArrayList<Integer> vArray = new ArrayList<Integer>();
		   ArrayList<Integer> wArray = new ArrayList<Integer>();
		   for (int v1 : v) {
			   vArray.add(v1);
		   }
		   for (int w2 : w) {
			   wArray.add(w2);
		   }
		   vArray.sort(null);
		   wArray.sort(null);
		   if (vArray.size() > wArray.size()) {
			   query = vArray.toString() + " " + wArray.toString();
		   }
		   else {
			   query = wArray.toString() + " " + vArray.toString();
		   }
		   if (!lastQuery.equals(query)) {
			   FindSAP findSAP = new FindSAP(v, w);
			   lastQuery = query;
			   lastDist = findSAP.getSAPDist();
			   lastAncestor = findSAP.getSAPAncestor();
			   //			   StdOut.println("new query | " + query );
			   return lastAncestor;
		   }
		   else {
			   //			   StdOut.println("old query | " + query);
			   return lastAncestor;
		   }

		   // no cache version		  
		   //		   FindSAP findSAP = new FindSAP(v, w);
		   //		   return findSAP.getSAPAncestor();

	   }
	   

	   private class FindSAP {
		   //		   long startTime = System.nanoTime();
		   int minDist = INFINITY;		   
		   int tmpDist;
		   int ancestor = -1;		   		   
		   HashMap<Integer, Integer> visitedV, visitedW;
		   ArrayList<Integer> distV, distW;
		   int numVisited = 0;
		   public FindSAP(int v, int w) {
			   // trivial case test
			   if (v == w) {
				   ancestor = v;
				   minDist = 0;
				   return;
			   }	
			   visitedV = new HashMap<Integer, Integer>();
			   visitedW = new HashMap<Integer, Integer>();
			   distV = new ArrayList<Integer>();
			   distW = new ArrayList<Integer>();
			   Queue<Integer> qV = new Queue<Integer>();
			   Queue<Integer> qW = new Queue<Integer>();
			   qV.enqueue(v);
			   visitedV.put(v, 0);
			   distV.add(0);
			   qW.enqueue(w);
			   visitedW.put(w, 0);
			   distW.add(0);
			   sapBFS(qV, qW);
		   }
		   
		   public FindSAP(Iterable<Integer> v, Iterable<Integer> w) {
			   // trivial case test
			   for (int v1 : v) {
				   for (int w1 : w) {
					   if (v1 == w1) {
						   ancestor = v1;
						   minDist = 0;
						   return;
					   }
				   }
			   }
			   visitedV = new HashMap<Integer, Integer>();
			   visitedW = new HashMap<Integer, Integer>();
			   Queue<Integer> qV = new Queue<Integer>();
			   Queue<Integer> qW = new Queue<Integer>();
			   for (int s : v) {
				   qV.enqueue(s);
				   distV.add(0);
				   visitedV.put(s, distV.size() - 1);
			   }
			   for (int s : w) {
				   qW.enqueue(s);
				   distW.add(0);
				   visitedW.put(s, distW.size() - 1);
			   }			   
			   sapBFS(qV, qW);

		   }
		   private void sapBFS(Queue<Integer> qV, Queue<Integer> qW) {
			   int v1, w1;
			   int indV, indW;
			   int prevDist;
			   Integer otherInd;
			   boolean exitV = false, exitW = false;
			   while((!qV.isEmpty() & !exitV) | (!qW.isEmpty() & !exitW)) {
				   if (!qV.isEmpty() & !exitV) {
					   v1 = qV.dequeue();
					   prevDist = distV.get(visitedV.get(v1));
					   // early exit condition
					   if (prevDist == minDist) {						  
						   exitV = true;						  					  
					   }
					   if (!exitV) {
						   for (int v2 : G.adj(v1)) {							   
							   if (!visitedV.containsKey(v2)) {
								   distV.add(prevDist + 1);
								   visitedV.put(v2, distV.size() - 1);
								   otherInd = visitedW.get(v2);
								   if (otherInd != null) {
									   tmpDist = (prevDist + 1) + distW.get(otherInd);
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
					   prevDist = distW.get(visitedW.get(w1));
					   // early exit condition
					   if (prevDist == minDist) {
						   exitW = true;						   
					   }
					   if (!exitW) {
						   for (int w2 : G.adj(w1)) {
							   if (!visitedW.containsKey(w2)) {
								   distW.add(prevDist + 1);
								   visitedW.put(w2, distW.size() - 1);
								   otherInd = visitedV.get(w2);
								   if (otherInd != null) {
									   tmpDist = (prevDist + 1) + distV.get(otherInd);
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
		   }
		   long time = System.nanoTime() - startTime;
		   double callSec = numCalls  / (double) time;
		   StdOut.println("time elapsed in during calls: " + time);
		   StdOut.println("calls / sec: " + callSec* 1000000000);
		   StdOut.println(numVisited);
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
