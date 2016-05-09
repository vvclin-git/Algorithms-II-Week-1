import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.*;
public class WordNet {
	// constructor takes the name of the two input files
	//private ArrayList<String> synMap;
	private HashMap<String, Bag<Integer>> synMap;
	private HashMap<Integer, String> synMapRev;
	private Digraph hyperGraph;
	private String[] synArray;
	private SAP sap;
	public WordNet(String synsets, String hypernyms) {
		   String[] synLine;
		   String[] hyperLine;
		   int synId; 
		   int maxSynId = Integer.MIN_VALUE;
//		   synMap = new ArrayList<String>();		   
		   synMap = new HashMap<String, Bag<Integer>>();
		   synMapRev = new HashMap<Integer, String>();
		   In synIn = new In(synsets);
		   while (synIn.hasNextLine()) {
			   synLine = synIn.readLine().split(",");
			   synId = Integer.parseInt(synLine[0]);
			   if (synId >= maxSynId) {
				   maxSynId = synId;
			   }
			   //synMap.add(synLine[1]);
			   synMapRev.put(synId, synLine[1]);
			   for (String n : synLine[1].split(" ")) {
				   if (synMap.containsKey(n)) {
					   synMap.get(n).add(synId);
				   }
				   else {
					   synMap.put(n, new Bag<Integer>());
					   synMap.get(n).add(synId);
				   }
			   }
		   }
		   In hyperIn = new In(hypernyms);
		   hyperGraph = new Digraph(maxSynId + 1);		   
		   synArray = new String[maxSynId + 1];		   
		   while (hyperIn.hasNextLine()) {
			   hyperLine = hyperIn.readLine().split(",");
			   synId = Integer.parseInt(hyperLine[0]);
			   for (int i = 1; i < hyperLine.length ; i++) {
				   hyperGraph.addEdge(synId, Integer.parseInt(hyperLine[i]));
			   }			   
		   }
		   if (!isValidGraph(hyperGraph)) {
			   throw new java.lang.IllegalArgumentException();
		   }
		   sap = new SAP(hyperGraph);
	   }

	   // returns all WordNet nouns
	   public Iterable<String> nouns() {
//		   ArrayList<String> output = new ArrayList<String>();
//		   for (String s : synMap) {
//			   for(String n : s.split(" ")) {
//				   output.add(n);
//			   }
//		   }		   
		   return synMap.keySet();
	   }

	   // is the word a WordNet noun?
	   public boolean isNoun(String word) {
//		   for (String s : synMap) {
//			   for (String n : s.split(" ")) {
//				   if (n.equals(word)) {
//					   return true;
//				   }
//			   }			   
//		   }
//		   return false;
		   if (word == null) {
			   throw new java.lang.NullPointerException(); 
		   }
		   return synMap.containsKey(word);
	   }

	   // distance between nounA and nounB (defined below)
//	   private int getSynId(String noun) {
////		   for (int i = 0; i < synMap.size(); i++) {
////			   for (String n : synMap.get(i).split(" ")) {
////				   //StdOut.println(n + ", " + i);
////				   if (n.equals(noun)) {
////					   return i;
////				   }
////			   }
////		   }
////		   return -1;
//	   }
	   private Bag<Integer> getSynIds(String noun) {
//		   ArrayList<Integer> output = new ArrayList<Integer>();
//		   for (int i = 0; i < synMap.size(); i++) {
////			   if (synMap.get(i).contains(" " + noun + " ")) {
////				   output.add(i);
////			   }
//			   for (String n : synMap.get(i).split(" ")) {
//				   //StdOut.println(n + ", " + i);
//				   if (n.equals(noun)) {
//					   output.add(i);
//				   }
//			   }
//		   }		   
//		   return output;
		   return synMap.get(noun);
	   }
	   public int distance(String nounA, String nounB) {		   
//		   ArrayList<Integer> s1 = getSynIds(nounA);
//		   ArrayList<Integer> s2 = getSynIds(nounB);
		   if (!(isNoun(nounA) & isNoun(nounB))) {
			   throw new java.lang.IllegalArgumentException();
		   }
		   Bag<Integer> s1 = getSynIds(nounA);
		   Bag<Integer> s2 = getSynIds(nounB);
		   
//		   if ((s1.size() == 0 | s2.size() == 0)) {
//			   StdOut.println("nounA: " + nounA);
//			   StdOut.println("nounB: " + nounB);
//			   throw new java.lang.IllegalArgumentException;
//		   }
//		   StdOut.println("nounA: " + s1 + " nounB: " + s2); 
//		   SAP sap = new SAP(hyperGraph);		   
		   return sap.length(s1, s2);
		   
	   }

	   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	   // in a shortest ancestral path (defined below)
	   public String sap(String nounA, String nounB) {	
//		   ArrayList<Integer> s1 = getSynIds(nounA);
//		   ArrayList<Integer> s2 = getSynIds(nounB);
		   if (!(isNoun(nounA) & isNoun(nounB))) {
			   throw new java.lang.IllegalArgumentException();
		   }
		   Bag<Integer> s1 = getSynIds(nounA);
		   Bag<Integer> s2 = getSynIds(nounB);
//		   if ((s1.size() == 0 | s2.size() == 0)) {
//			   throw new java.lang.IllegalArgumentException();
//		   }
//		   SAP sap = new SAP(hyperGraph);
//		   StdOut.println("nounA: " + s1 + " nounB: " + s2); 
		   int ancestor = sap.ancestor(s1, s2);		   
		   
		   return synMapRev.get(ancestor);
	   }
	   private String sapAncestorDebug(int s1, int s2) {		   
		   return synMapRev.get(sap.ancestor(s1, s2));
	   }
	   private int sapDistDebug(int s1, int s2) {		   
		   return sap.length(s1, s2);
	   }
	   private void printSynIds(String noun) {
		   for (int s : synMap.get(noun)) {
			   StdOut.println(s);
		   }		   
	   }
	   private boolean isValidGraph(Digraph G) {
		   // test if the graph is a rooted DAG
		   Topological to = new Topological(G);
		   int rootNum = 0;
		   if (to.hasOrder()) {
			   for (int v = 0; v < G.V(); v++) {
				   if (G.outdegree(v) == 0) {
					   rootNum += 1;
				   }
				   if (rootNum > 1) {
					   return false;
				   }
			   }
			   return true;
		   }
		   return false;		   
	   }
	   // do unit testing of this class
	   public static void main(String[] args) {
//		   WordNet wn = new WordNet("wordnet\\synsets.txt", "wordnet\\hypernyms6InvalidTwoRoots.txt");
//		   WordNet wn = new WordNet("wordnet\\synsets.txt", "wordnet\\hypernyms.txt");
		   WordNet wn = new WordNet("wordnet\\synsets15.txt", "wordnet\\hypernyms15Tree.txt");
//		   StdOut.println(wn.sapAncestorDebug(81681, 24306));
//		   StdOut.println(wn.sapDistDebug(80917, 54384)); //white_marlin, mileage
//		   StdOut.println(wn.sapAncestorDebug(80917, 54384));
//		   for (int i = 0; i < wn.hyperGraph.V(); i++) {
//			   if (wn.hyperGraph.indegree(i) > 1) {
//				 StdOut.println(wn.hyperGraph.indegree(i));
//			   }
//		   }
//		   StdOut.println(avgDeg / wn.hyperGraph.V());
//		   StdOut.println(wn.isNoun("sister"));
//		   StdOut.println(wn.isNoun("brother"));
//		   StdOut.println(wn.sap("white_marlin", "mileage"));
//		   StdOut.println(wn.sap("demotion", "variation"));
//		   StdOut.println(wn.sapDistDebug(33756, 50757));
//		   StdOut.println(wn.sapDistDebug(50757, 33756));
//		   StdOut.println(wn.distance("American_water_spaniel", "histology"));
//		   for (int i : wn.getSynIds("horse")) {
//			   StdOut.println(i);
//		   };
//		   StdOut.println(wn.distance("worm", "bird"));
//		   StdOut.println(wn.sap("worm", "bird"));
//		   StdOut.println(wn.sapDistDebug(81682, 33764));
		   StdOut.println(wn.isNoun("simple_protein"));
		   StdOut.println(wn.isNoun("work_stoppage"));
		   StdOut.println(wn.isNoun("b"));
		   StdOut.println(wn.distance("invalid", "b"));
		   
	   }
}

