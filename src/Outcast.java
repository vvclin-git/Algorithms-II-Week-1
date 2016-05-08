import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
   private WordNet wordnet;
   public Outcast(WordNet wordnet) {
	   this.wordnet = wordnet; // constructor takes a WordNet object
   }
   public String outcast(String[] nouns) {
	// given an array of WordNet nouns, return an outcast
	   int tmpDist, maxDist = Integer.MIN_VALUE;
	   String outcast = "";
	   for (String n1 : nouns) {
		   tmpDist = 0;
		   for (String n2 : nouns) {
			   tmpDist += wordnet.distance(n1, n2);			   
		   }
		   if (tmpDist >= maxDist) {
			   maxDist = tmpDist;
			   outcast = n1;
		   }
	   }
	   return outcast;
	   // finding outcasts
   }
   public static void main(String[] args) {
	   // see test client below
	   WordNet wordnet = new WordNet("wordnet\\synsets.txt", "wordnet\\hypernyms.txt");
	    Outcast outcast = new Outcast(wordnet);
	    In in;
	    String[] nouns;
	    in = new In("wordnet\\outcast5.txt");
        nouns = in.readAllStrings();
        StdOut.println("wordnet\\outcast5.txt" + ": " + outcast.outcast(nouns));
        in = new In("wordnet\\outcast8.txt");
        nouns = in.readAllStrings();
        StdOut.println("wordnet\\outcast8.txt" + ": " + outcast.outcast(nouns));
        in = new In("wordnet\\outcast11.txt");
        nouns = in.readAllStrings();
        StdOut.println("wordnet\\outcast11.txt" + ": " + outcast.outcast(nouns));
//	    for (int t = 2; t < args.length; t++) {
//	        In in = new In(args[t]);
//	        String[] nouns = in.readAllStrings();
//	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
//	    }
   }
}

