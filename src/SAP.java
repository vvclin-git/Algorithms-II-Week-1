import java.awt.List;
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
	private HashMap<String, Integer> sapDist;
	private HashMap<String, Integer> sapAncestor;
	private int[] distV, distW;
	private boolean[] markedV, markedW;
	private String lastQuery;
	private int lastDist, lastAncestor;
	//	public int numVisited;
	   // constructor takes a digraph (not necessarily a DAG)
	   public SAP(Digraph G) {
		   this.G = new Digraph(G);	   
		   this.sapDist = new HashMap<String, Integer>();
		   this.sapAncestor = new HashMap<String, Integer>();
		   this.distV = new int[G.V()];
		   this.distW = new int[G.V()];
		   this.markedV = new boolean[G.V()];
		   this.markedW = new boolean[G.V()];
		   for (int i = 0; i < G.V(); i++) {
			   distV[i] = INFINITY;
			   distW[i] = INFINITY;			   
		   }
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
//		   long startTime = System.nanoTime();
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
	   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
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
		   public FindSAP(int v, int w) {
			   // trivial case test
			   if (v == w) {
				   ancestor = v;
				   minDist = 0;
				   return;
			   }			   
			   Queue<Integer> qV = new Queue<Integer>();
			   Queue<Integer> qW = new Queue<Integer>();
			   qV.enqueue(v);
			   distV[v] = 0;
			   markedV[v] = true;
			   qW.enqueue(w);
			   distW[w] = 0;
			   markedW[w] = true;
			   sapBFS(qV, qW);
		   }
		   //			   StdOut.println(System.nanoTime() - startTime);
		   
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
			   Queue<Integer> qV = new Queue<Integer>();
			   Queue<Integer> qW = new Queue<Integer>();
			   for (int s : v) {
				   qV.enqueue(s);
				   distV[s] = 0;
				   markedV[s] = true;
			   }
			   for (int s : w) {
				   qW.enqueue(s);
				   distW[s] = 0;
				   markedW[s] = true;
			   }			   
			   sapBFS(qV, qW);

		   }
		   private void sapBFS(Queue<Integer> qV, Queue<Integer> qW) {
			   int v1, w1;
			   int s;
			   boolean exitV = false, exitW = false;
			   Queue<Integer> sQW = new Queue<Integer>();
			   Queue<Integer> sQV = new Queue<Integer>();
			   for (int v : qV) {
				   sQV.enqueue(v);
			   }
			   for (int w : qW) {
				   sQW.enqueue(w);
			   }
			   // empty set case test
			   if (qV.isEmpty() | qW.isEmpty()) {
				   ancestor = -1;
				   minDist = -1;
				   return;
			   }
			   // lock step
			   while((!qV.isEmpty() & !exitV) | (!qW.isEmpty() & !exitW)) {
				   if (!qV.isEmpty() & !exitV) {
					   v1 = qV.dequeue();
					   // early exit condition
					   if (distV[v1] == minDist) {	   
						   exitV = true;						  					  
					   }
					   if (!exitV) {
						   for (int v2 : G.adj(v1)) {							   
//							   if (!visitedV.contains(v2)) {
							   if (!markedV[v2]) {
								   markedV[v2] = true;
								   distV[v2] = distV[v1] + 1;							   
								   qV.enqueue(v2);
								   if (distW[v2] != INFINITY) {
									   tmpDist = distV[v2] + distW[v2];
									   if (tmpDist <= minDist) {
										   ancestor = v2;
										   minDist = tmpDist;
									   }
								   }
							   }							   
						   }
					   }

				   }
				   if (!qW.isEmpty() & !exitW) {
					   w1 = qW.dequeue();
					   // early exit condition
					   if (distW[w1] == minDist) {
						   exitW = true;						   
					   }
					   if (!exitW) {
						   for (int w2 : G.adj(w1)) {							   
//							   if (!visitedW.contains(w2)) {
							   if (!markedW[w2]) {
								   markedW[w2] = true;
								   distW[w2] = distW[w1] + 1;
								   qW.enqueue(w2);
								   if (distV[w2] != INFINITY) {
									   tmpDist = distV[w2] + distW[w2];
									   if (tmpDist <= minDist) {
										   ancestor = w2;
										   minDist = tmpDist;
									   }
								   }
							   }
							   
						   }
					   }
				   }
			   }
			   // reset these long arrays
			   for (int s1 : sQV) {
				   distV[s1] = INFINITY;
				   markedV[s1] = false;
			   }
			   for (int s1 : sQW) {
				   distW[s1] = INFINITY;
				   markedW[s1] = false;
			   }
			   while (!sQV.isEmpty()) {
				   s = sQV.dequeue();
				   for (int s1 : G.adj(s)) {
					   if (markedV[s1]) {
						   distV[s1] = INFINITY;
						   markedV[s1] = false;
						   sQV.enqueue(s1);
					   }
				   }
			   }
			   while (!sQW.isEmpty()) {
				   s = sQW.dequeue();
				   for (int s1 : G.adj(s)) {
					   if (markedW[s1]) {
						   distW[s1] = INFINITY;
						   markedW[s1] = false;
						   sQW.enqueue(s1);
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
		   
//		   In in = new In("wordnet\\digraph1.txt");
//		   Digraph G = new Digraph(in);		   
//		   SAP sap = new SAP(G);		   
//		   StdOut.println("length: " + sap.length(1, 7) + " ancestor: "+ sap.ancestor(1, 7));
//		   StdOut.println("length: " + sap.length(7, 1) + " ancestor: "+ sap.ancestor(7, 1));

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
		   Out out = new Out("wordnet\\records.txt"); // for result recordings
		   Digraph G = new Digraph(in);		   
		   SAP sap = new SAP(G);
		   Random rand = new Random();		   
		   int min = 0;
		   int max = 82191;
		   int numCalls = 10000;
		   int randomNum1, randomNum2;
		   int numVisited = 0;
		   int length, ancestor;
		   long startTime = System.nanoTime();
		   for (int i = 0; i < numCalls; i++) {			   
			   randomNum1 = rand.nextInt((max - min) + 1) + min;
			   randomNum2 = rand.nextInt((max - min) + 1) + min;
			   length = sap.length(randomNum1, randomNum2);
			   ancestor = sap.ancestor(randomNum1, randomNum2);
			   // v, w, length, ancestor
			   out.println(randomNum1 + "," + randomNum2 + "," + length + "," + ancestor);
//			   numVisited += sap.numVisited;
		   }
		   out.close();
		   long time = System.nanoTime() - startTime;
		   double callSec = numCalls  / (double) time;
		   StdOut.println("time elapsed in during calls: " + time);
		   StdOut.println("calls / sec: " + callSec* 1000000000);		   
		   StdOut.println("avg. vertices visited per call: " +  (numVisited / (double) numCalls));
		   
		   

	   }
}
