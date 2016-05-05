import java.util.ArrayList;
import edu.princeton.cs.algs4.*;
public class WordNet {
	// constructor takes the name of the two input files
	ArrayList<String> synMap;
	Digraph hyperGraph;
	String[] synArray;
	SAP sap;
	public WordNet(String synsets, String hypernyms) {
		   String[] synLine;
		   String[] hyperLine;
		   int synId; 
		   int maxSynId = Integer.MIN_VALUE;
		   synMap = new ArrayList<String>();		   
		   In synIn = new In(synsets);
		   while (synIn.hasNextLine()) {
			   synLine = synIn.readLine().split(",");
			   synId = Integer.parseInt(synLine[0]);
			   if (synId >= maxSynId) {
				   maxSynId = synId;
			   }
			   synMap.add(synLine[1]);
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
		   sap = new SAP(hyperGraph);
	   }

	   // returns all WordNet nouns
	   public Iterable<String> nouns() {
		   ArrayList<String> output = new ArrayList<String>();
		   for (String s : synMap) {
			   for(String n : s.split(" ")) {
				   output.add(n);
			   }
		   }
		   return output;
	   }

	   // is the word a WordNet noun?
	   public boolean isNoun(String word) {
		   for (String s : synMap) {
			   for (String n : s.split(" ")) {
				   if (n.equals(word)) {
					   return true;
				   }
			   }			   
		   }
		   return false;
	   }

	   // distance between nounA and nounB (defined below)
	   private int getSynId(String noun) {
		   for (int i = 0; i < synMap.size(); i++) {
			   for (String n : synMap.get(i).split(" ")) {
				   //StdOut.println(n + ", " + i);
				   if (n.equals(noun)) {
					   return i;
				   }
			   }
		   }
		   return -1;
	   }
	   public int distance(String nounA, String nounB) {		   
		   int s1 = getSynId(nounA);
		   int s2 = getSynId(nounB);
		   if ((s1 == -1 | s2 == -1)) {
			   throw new java.lang.IllegalArgumentException();
		   }
		   StdOut.println("nounA: " + s1 + " nounB: " + s2); 
//		   SAP sap = new SAP(hyperGraph);		   
		   return sap.length(s1, s2);
		   
	   }

	   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	   // in a shortest ancestral path (defined below)
	   public String sap(String nounA, String nounB) {	
		   int s1 = getSynId(nounA);
		   int s2 = getSynId(nounB);
		   if ((s1 == -1 | s2 == -1)) {
			   throw new java.lang.IllegalArgumentException();
		   }
//		   SAP sap = new SAP(hyperGraph);
//		   StdOut.println("nounA: " + s1 + " nounB: " + s2); 
		   int ancestor = sap.ancestor(s1, s2);		   
		   return synMap.get(ancestor);
	   }
	   public String sapAncestorDebug(int s1, int s2) {		   
		   return synMap.get(sap.ancestor(s1, s2));
	   }
	   public int sapDistDebug(int s1, int s2) {		   
		   return sap.length(s1, s2);
	   }
	   // do unit testing of this class
	   public static void main(String[] args) {
		   WordNet wn = new WordNet("wordnet\\synsets.txt", "wordnet\\hypernyms.txt");
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
		   StdOut.println(wn.sap("demotion", "variation"));
//		   StdOut.println(wn.sapAncestorDebug(33756, 50757));
//		   StdOut.println(wn.distance("American_water_spaniel", "histology"));
	   }
}
