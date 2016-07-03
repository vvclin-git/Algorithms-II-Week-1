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
			int minDist = INFINITY;		   
			int ancestor = -1;		   		   

			HashMap<Integer, Integer> alien2me;
			ArrayList<Integer> dist;
			ArrayList<Integer> me2alien;
			ArrayList<Integer> root;

			int numVisited = 0;

			public FindSAP(int v, int w) {
				// trivial case test
				if (v == w) {
					ancestor = v;
					minDist = 0;
					return;
				}	

				alien2me = new HashMap<Integer, Integer>();
				me2alien = new ArrayList<Integer>();
				root = new ArrayList<Integer>();
				dist = new ArrayList<Integer>();
				Queue<Integer> q = new Queue<Integer>();

				q.enqueue(0);
				alien2me.put(v, 0);
				me2alien.add(v);
				dist.add(0);
				root.add(v);

				q.enqueue(1);
				alien2me.put(w, 1);
				me2alien.add(w);
				dist.add(0);
				root.add(w);

				sapBFS(q);
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

				alien2me = new HashMap<Integer, Integer>();
				me2alien = new ArrayList<Integer>();
				root = new ArrayList<Integer>();
				Queue<Integer> q = new Queue<Integer>();
				for (int s : v) {					
					me2alien.add(s);
					dist.add(0);
					root.add(0);
					q.enqueue(dist.size() - 1);
					alien2me.put(s, dist.size() - 1);
				}
				for (int s : w) {
					me2alien.add(s);
					dist.add(0);
					root.add(0);
					q.enqueue(dist.size() - 1);
					alien2me.put(s, dist.size() - 1);
				}			

				sapBFS(q);
			}
			private void sapBFS(Queue<Integer> q) {
				while (!q.isEmpty()) {
					int v = q.dequeue();

					for(int alien: G.adj(me2alien.get(v))) {
						Integer me = alien2me.get(alien);

						if (me == null) {
							alien2me.put(alien, me = me2alien.size());
//							alien2me.put(alien, me); // why?
							me2alien.add(alien);
							dist.add(dist.get(v) + 1);
							root.add(root.get(v));

							q.enqueue(me);
						}
						else if (root.get(me) != root.get(v)) {
							// eureka!
							minDist = dist.get(me) + dist.get(v) + 1;
//							ancestor = me;
							ancestor = me2alien.get(me);
//							StdOut.println(alien2me.keySet().toString());
							return;
						}
						else {
							// cyclic 8-(
						}
					}
				}
				
			}

			public int getSAPDist() {
				return (minDist != INFINITY)? minDist : -1;
			}

			public int getSAPAncestor() {
				return (minDist != INFINITY)? ancestor : -1; 
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
		   
		   // correctenss tester		   
		   In inGraph = new In("wordnet\\digraph-wordnet.txt");
		   In inRecord = new In("wordnet\\records.txt");
		   String recordLine;
		   String[] record;
		   Digraph G = new Digraph(inGraph);		   
		   SAP sap = new SAP(G);
		   int v, w, length, ancestor, numTests = 0, numFails = 0;
		   while (inRecord.hasNextLine()) {
			   recordLine = inRecord.readLine();
			   numTests ++;
			   record = recordLine.split(",");
			   v = Integer.parseInt(record[0]);
			   w = Integer.parseInt(record[1]);
			   length = sap.length(v, w);
			   ancestor = sap.ancestor(v, w);
			   if (Integer.parseInt(record[2]) != length | Integer.parseInt(record[3]) != ancestor) {
				   StdOut.print("error with v = " + v + ", w = " + w);
				   if (Integer.parseInt(record[2]) != length) {
					   StdOut.print("| expected length: " + Integer.parseInt(record[2]));
					   StdOut.print(" actual length: " + length);
				   }
				   if (Integer.parseInt(record[3]) != ancestor) {
					   StdOut.print("| expected ancestor: " + Integer.parseInt(record[3]));
					   StdOut.print(" actual ancestor: " + ancestor);
				   }
				   StdOut.println();
				   numFails ++;
			   }
		   }
		   StdOut.println("test result: " + (numTests - numFails) + " / " + numTests + " passed");
		   
//		   int numVisited = 0;;
//		   In in = new In("wordnet\\digraph-wordnet.txt");
//		   Digraph G = new Digraph(in);		   
//		   SAP sap = new SAP(G);		   
//		   StdOut.println("length: " + sap.length(23231, 23302) + " ancestor: "+ sap.ancestor(23231, 23302));
//		   StdOut.println(numVisited);
		   
		   // performance tester
//		   In in = new In("wordnet\\digraph-wordnet.txt");
//		   Digraph G = new Digraph(in);		   
//		   SAP sap = new SAP(G);
//		   Random rand = new Random();		   
//		   int min = 0;
//		   int max = 82191;
//		   int numCalls = 100000;
//		   int randomNum1, randomNum2;
//		   int numVisited = 0;
//		   long startTime = System.nanoTime();
//		   for (int i = 0; i < numCalls; i++) {			   
//			   randomNum1 = rand.nextInt((max - min) + 1) + min;
//			   randomNum2 = rand.nextInt((max - min) + 1) + min;
//			   sap.length(randomNum1, randomNum2);
//		   }
//		   long time = System.nanoTime() - startTime;
//		   double callSec = numCalls  / (double) time;
//		   StdOut.println("time elapsed in during calls: " + time);
//		   StdOut.println("calls / sec: " + callSec* 1000000000);
//		   StdOut.println(numVisited);
//		   StdOut.println("avg. vertices visited per call: " +  (numVisited / (double) numCalls));
		   
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
