import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.*;
public class WordNet {
	// constructor takes the name of the two input files
	Map<String, Integer> synMap;
	Digraph hyperGraph;
	String[] synArray;
	public WordNet(String synsets, String hypernyms) {
		   String[] synLine;
		   String[] hyperLine;
		   int synId; 
		   int maxSynId = Integer.MIN_VALUE;
		   synMap = new HashMap<String, Integer>();		   
		   In synIn = new In(synsets);
		   while (synIn.hasNextLine()) {
			   synLine = synIn.readLine().split(",");
			   synId = Integer.parseInt(synLine[0]);
			   if (synId >= maxSynId) {
				   maxSynId = synId;
			   }
			   synMap.put(synLine[1], synId);
		   }
		   In hyperIn = new In(hypernyms);
		   hyperGraph = new Digraph(maxSynId + 1);
		   synArray = new String[maxSynId + 1];
		   for (String noun : synMap.keySet()) {
			   synArray[synMap.get(noun)] = noun; 
		   }
		   while (hyperIn.hasNextLine()) {
			   hyperLine = hyperIn.readLine().split(",");
			   synId = Integer.parseInt(hyperLine[0]);
			   for (int i = 1; i < hyperLine.length ; i++) {
				   hyperGraph.addEdge(synId, Integer.parseInt(hyperLine[i]));
			   }			   
		   }
	   }

	   // returns all WordNet nouns
	   public Iterable<String> nouns() {
		   return synMap.keySet();
	   }

	   // is the word a WordNet noun?
	   public boolean isNoun(String word) {
		   for (String key : synMap.keySet()) {
			   if (key.toLowerCase().contains(word)) {
				   return true;
			   }
		   }
		   return false;
	   }

	   // distance between nounA and nounB (defined below)
	   public int distance(String nounA, String nounB) {
		   if (!(isNoun(nounA) & isNoun(nounB))) {
			   throw new java.lang.IllegalArgumentException();
		   }
		   int s1 = synMap.get(nounA);
		   int s2 = synMap.get(nounB);
		   StdOut.println("nounA: " + s1 + " nounB: " + s2); 
		   SAP sap = new SAP(hyperGraph);		   
		   return sap.length(s1, s2);
		   
	   }

	   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	   // in a shortest ancestral path (defined below)
	   public String sap(String nounA, String nounB) {	
		   if (!(isNoun(nounA) & isNoun(nounB))) {
			   throw new java.lang.IllegalArgumentException();
		   }
		   int s1 = synMap.get(nounA);
		   int s2 = synMap.get(nounB);
		   SAP sap = new SAP(hyperGraph);
		   int ancestor = sap.ancestor(s1, s2);
		   StdOut.println(synArray[ancestor]);
		   return synArray[ancestor];
	   }

	   // do unit testing of this class
	   public static void main(String[] args) {
		   WordNet wn = new WordNet("wordnet\\synsets.txt", "wordnet\\hypernyms.txt");
		   double avgDeg = 0;
//		   for (int i = 0; i < wn.hyperGraph.V(); i++) {
//			   if (wn.hyperGraph.indegree(i) > 1) {
//				 StdOut.println(wn.hyperGraph.indegree(i));
//			   }
//		   }
//		   StdOut.println(avgDeg / wn.hyperGraph.V());
		   StdOut.println(wn.isNoun("sister"));
		   StdOut.println(wn.isNoun("brother"));
		   StdOut.println(wn.sap("worm", "bird"));
//		   StdOut.println(wn.sap("antihistamine", "nasal_decongestant"));
	   }
}
