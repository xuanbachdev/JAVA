package custom.jellyfish;

import common.StdOut;
import graph.Graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class RandomRegularGraph extends Graph {
    private List<Integer> switches;
    private List<Integer> hosts;
    private int k;  //n port per switch
    private int N;
    private int r;

    private int nHost;
    private int nSwitch;

    private int numMissed[];

    //start: son'code on 26/08/2019
    public int getN(){
        return N;
    }

    public int getK(){
        return k;
    }

    public int getR(){
        return r;
    }

    public RandomRegularGraph(int nSwitch, int nHost){
        this.nHost = nHost;
        this.nSwitch = nSwitch;
        adj = (List<Integer>[]) new List[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<Integer>();
        }
        this.switches();
        this.hosts();

    }

    public int getnHost(){
        return nHost;
    }

    public int getnSwitch(){
        return nSwitch;
    }

    public RandomRegularGraph(int nSwitch, int nHost, int k){
        this.nSwitch = nSwitch;
        this.nHost = nHost;
        this.k = k;
        this.V = nHost + nSwitch;
        adj = (List<Integer>[]) new List[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
//        if (nHost/nSwitch > k) return;

        int hostId = nSwitch;
        while (hostId < V){
            for (int i=0;i<nSwitch;i++) {
                addEdge(i, hostId);
                hostId++;
                if (hostId == V) break;
            }
        }

        int numMissed[] = new int[nSwitch];
        for (int i=0;i<nSwitch;i++) numMissed[i] = k - adj[i].size();

        creatRandomLink(numMissed);
    }

    public RandomRegularGraph(String fileEdge) {
        this.nHost = 0;
        this.nSwitch = 0;
        this.k = 0;
        try (Stream<String> stream = Files.lines(Paths.get(fileEdge))) {
            stream.forEach(line -> {
                if (line.split(" ").length > 2){
                    this.nSwitch = Integer.parseInt(line.split(" ")[0]);
                    this.nHost = Integer.parseInt(line.split(" ")[1]);
                    this.k = Integer.parseInt(line.split(" ")[2]);
                    this.V = nHost + nSwitch;
                    adj = (List<Integer>[]) new List[V];
                    for (int v = 0; v < V; v++) {
                        adj[v] = new ArrayList<>();
                    }
                }
                else {
                    int u = Integer.parseInt(line.split(" ")[0]);
                    int v = Integer.parseInt(line.split(" ")[1]);

                    if (!this.hasEdge(u, v)) {
                        this.addEdge(u, v);
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void creatRandomLink(int numMissed[]) {
        Random r = new Random();
        int[] thr = new int[(k * nSwitch - nHost)/2];
        int[] rcv = new int[(k * nSwitch - nHost)/2];
        int thr_index = 0;
        int rcv_index = 0;
        boolean b = true;

        for (int i=0;i<nSwitch;i++){
            for (int j=0;j<numMissed[i];j++){
                if (b){
                    thr[thr_index] = i;
                    thr_index++;
                    b = !b;
                }
                else{
                    rcv[rcv_index] = i;
                    rcv_index++;
                    b = !b;
                }
            }
        }

        int last = rcv.length;;
        for (int i=0;i<thr.length;i++){
            int j = r.nextInt(last);
            int try_time = 0;
            while(try_time<last){
                if ((thr[i]==rcv[j])||(hasEdge(thr[i],rcv[j]))){
                    //                System.out.printf("\nFail (%d,%d)",thr[i],rcv[j]);
                    j = (j+1)%last;
                    try_time++;
                    continue;
                }
                addEdge(thr[i],rcv[j]);
                rcv[j] = rcv[last-1];
                last--;
                break;
            }
        }
    }

    @Override
    public List<Integer> hosts() {
        if (hosts != null) return hosts;

        hosts = new ArrayList<>();
        for (int i = nSwitch; i < V; i++)
            hosts.add(i);

        return hosts;
    }

    @Override
    public List<Integer> switches() {
        if (switches != null) return switches;

        switches= new ArrayList<>();
        for (int i = 0; i < nSwitch; i++)
            switches.add(i);

        return switches;
    }

    @Override
    public boolean isHostVertex(int v) {
        return v >= nSwitch;
    }

    @Override
    public boolean isSwitchVertex(int v) {
        return (v < nSwitch)&&(v>=0);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges \n");
        int sumDegree = 0;

        for (int v = 0; v < V; v++) {
            s.append(String.format("%2d:", v));
            s.append(String.format(" degree = %2d -- ", degree(v)));
            sumDegree += degree(v);
            for (int w : adj[v]) {
                s.append(String.format(" %2d", w));
            }
            s.append("\n");
        }
        s.append("\n");
        s.append(String.format("Average degree = %f", 1.0 * sumDegree / V));
        return s.toString();
    }

    public void writeFileEdges(String fileName) {
        try {
            File file = new File(fileName);
            // creates the file
            file.createNewFile();

            FileWriter writer = new FileWriter(file);

            // Writes the content to the file
            writer.write(this.nSwitch + " " + this.nHost + " " + this.k + "\n");
            for (int i : this.switches()) {
                for (int j : this.adj(i)) {
                    if (i < j)
                        writer.write(i + " " + j + "\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RandomRegularGraph graph = new RandomRegularGraph(20, 16, 4);
        StdOut.println(graph);
        graph.writeFileEdges("aaaa.txt");
    }
}
