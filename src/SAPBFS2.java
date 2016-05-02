import edu.princeton.cs.algs4.*;
public class SAPBFS2 {
	private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s->v path?
    private int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
    private int[] distTo;      // distTo[v] = length of shortest s->v path
	private int[] from;
    private int anscetor = -1;
    private int sapDist = -1;
	public SAPBFS2(Digraph G, int s1, int s2) {
		marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        from = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
        sapbfs(G, s1, s2);
	}
	private void sapbfs(Digraph G, int s1, int s2) {
        Queue<Integer> q = new Queue<Integer>();
        marked[s1] = true;
        distTo[s1] = 0;
        from[s1] = s1;
        q.enqueue(s1);
        marked[s2] = true;
        distTo[s2] = 0;
        from[s2] = s2;
        q.enqueue(s2);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    from[w] = v;
                    q.enqueue(w);
                }
                else {
                	if (from[w] != v) {
                		anscetor = w;
                		sapDist = distTo[w] + distTo[v] + 1;
                	}
                }
            }
        }
    }
	public int getSapDist() {
		return sapDist;
	}
	public int getAnscetor() {
		return anscetor;
	}
}
