package custom.smallworld;
import java.io.FileWriter;

public class Torus3D extends GridGraph {

    private int degree;
    public Torus3D(int N, int degree, String baseType)
    {
        super(N, N, N, baseType);
        this.degree = degree;

        baseType = baseType.toLowerCase();
        if (baseType.equals("torus3d"))
        {
            for(int i = 0; i < N; i++)
            {
                for(int j = 0; j < N; j++)
                {
                    for(int k = 0; k < N; k++)
                    {
                        addTorus3DEdges(i, j, k, N);
                    }
                }
            }
        }
    }

    public void addTorus3DEdges(int i, int j, int k, int N)
    {
        int curr = (j + i*N + k*N*N);
        int [] listOfNeighbors = vertexIndexes(i, j, k, N);
        for(int t = 0; t < listOfNeighbors.length; t++)
        {
            if(!hasEdge(curr, listOfNeighbors[t]))
                addEdge(curr, listOfNeighbors[t]);
        }
    }

    public int[] vertexIndexes(int i, int j, int k, int N)
    {
        int first = (i >= 1 ? (i - 1): N - 1); first = j + first * N + k*N*N;
        int second = (j >= 1 ? (j - 1): N - 1); second = second + i * N + k*N*N;
        int third = (k >= 1 ? (k - 1): N - 1); third = j + i * N + third*N*N;

        int forth = (i < N - 1 ? (i + 1): 0); forth = j + forth * N + k*N*N;
        int fifth = (j < N - 1 ? (j + 1): 0); fifth = fifth + i * N + k*N*N;
        int sixth = (k < N - 1 ? (k + 1): 0); sixth = j + i * N + sixth*N*N;
        int [] listOfNeighbors = {first, second, third, forth, fifth, sixth};
        return listOfNeighbors;
    }

    public void writeFile()
    {
        try {
            FileWriter fw = new FileWriter("torus3D_" + this.V +
                    "_nodes_" + this.degree +
                    ".edges");
            // Write strings to the file
            for (int i = 0; i < this.adj.length; i++) {
                for(int j = 0; j < this.adj[i].size(); j++)
                    fw.write(i + " " +  (int)adj[i].get(j) + "\n");
            }
            // Close file writer
            fw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String [] args)
    {
        Torus3D torrus3D = new Torus3D(36,6, "TORUS3D");
        torrus3D.writeFile();
    }
}
