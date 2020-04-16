package custom.jellyfish;

import common.StdOut;
import common.Tuple;
import graph.Graph;
import network.Packet;
import org.jetbrains.kotlin.ir.expressions.IrConstKind;
import routing.RoutingAlgorithm;
import routing.RoutingPath;

import java.util.*;

public class KShortestPathsRoutingAlgorithm implements RoutingAlgorithm {
    private Graph graph;

    public static int K;
    private Map<Integer, Map<Integer, List<List<Integer>>>> allPaths;
    private Map<Integer, Map<Integer, Integer>> counts;
    private Map<Integer, Integer> packetMapping;

    private List<Tuple> removeList;
    //private Map<Integer, Map<Integer, Integer>> decisions;

    public KShortestPathsRoutingAlgorithm(Graph graph, int K) {
        this.graph = graph;
        this.K = K;
        removeList = new ArrayList<>();
        this.allPaths = new HashMap<>();
        this.counts = new HashMap<>();
        this.packetMapping = new HashMap<>();

        //init();
    }

    private void kshortestpath(int u, int v){
        List<List<Integer>> A = new ArrayList<>(K);
        List<List<Integer>> B = new ArrayList<>(K);
        A.add(graph.shortestPath(u, v));
        for (int k = 1; k < K; k++) {
            for (int i = 0; i < A.get(k - 1).size() - 1; i++) {
                int spurNode = A.get(k - 1).get(i);
                List<Integer> rootPath = new ArrayList<>();
                rootPath.addAll(A.get(k - 1).subList(0, i + 1));

                for (List<Integer> path : A) {
                    if (path.size()<i+1) continue;

                    if (pathEqual(rootPath, path.subList(0, i + 1))) {
                        if (path.size()<=1){
                            System.out.printf("\n____________________ ");
                            for (int ii : path) System.out.printf("%d ",ii);
                        }
                        if (graph.hasEdge(path.get(i), path.get(i + 1))){
                            graph.removeEdge(path.get(i), path.get(i + 1));
                            removeList.add(new Tuple(path.get(i), path.get(i + 1)));
                        }
                    }
                }

                for (List<Integer> path : B) {
                    if (path.size()<i+1) continue;
                    if (pathEqual(rootPath, path.subList(0, i + 1))) {
                        if (graph.hasEdge(path.get(i), path.get(i + 1))){
                            graph.removeEdge(path.get(i), path.get(i + 1));
                            removeList.add(new Tuple(path.get(i), path.get(i + 1)));
                        }
                    }
                }

                for (int rootPathNode : rootPath) {
                    if (rootPathNode == spurNode) continue;
                    List<Integer> adj = new ArrayList<>();
                    adj.addAll(graph.adj(rootPathNode));
                    for (int node : adj) {
                        if (graph.hasEdge(rootPathNode, node)){
                            graph.removeEdge(rootPathNode, node);
                            removeList.add(new Tuple(rootPathNode, node));
                        }
                    }
                }

                List<Integer> spurPath = graph.shortestPath(spurNode, v);
                if (spurPath.isEmpty()) continue;


                List<Integer> totalPath = new ArrayList<>();
                totalPath.addAll(rootPath.subList(0,rootPath.size()-1));
                totalPath.addAll(spurPath);

                if (B.size() < K){
                    B.add(totalPath);
                }
                else if (totalPath.size() < B.get(K - 1).size()){
                    B.set(K - 1, totalPath);
                }

                for (Tuple<Integer, Integer> edge : removeList) {
                    if (graph.hasEdge(edge.a, edge.b)) continue;
                    graph.addEdge(edge.a, edge.b);
                }
                removeList.clear();

            }
            if (B.isEmpty()) break;
            Collections.sort(B, (Comparator<List>) (b1, b2) -> {
                return b1.size() - b2.size();
            });

            A.add(B.get(0));
            B.remove(0);
        }
        allPaths.get(u).put(v, A);
        counts.get(u).put(v,0);
    }

    private void init() {
        long startTime = System.currentTimeMillis();
        for (int u : graph.switches()) {
            allPaths.put(u, new HashMap<>());
            counts.put(u, new HashMap<>());
            for (int v : graph.switches()) {
                if (v==u) continue;
                kshortestpath(u,v);
            }
            StdOut.printProgress("Building ra", startTime, graph.switches().size(), u+1);
        }
    }

    private boolean pathEqual(List<Integer> A, List<Integer> B){
        if (A.size()!=B.size()) return false;
        for (int i=0;i<A.size();i++){
            if (A.get(i)!=B.get(i)) return false;
        }
        return true;
    }

    @Override
    public int next(int source, int current, int destination) {
        return -1;
    }

    public int next(int current, Packet p) {
        int source = p.getSource();
        int destination = p.getDestination();
        if (graph.isHostVertex(current)) {
            return graph.adj(current).get(0);
        } else if (graph.adj(current).contains(destination)) {
            packetMapping.remove(p.id);
            return destination;
        } else {
            int srcSwitch = graph.isHostVertex(source) ? graph.adj(source).get(0) : source;
            int desSwitch = graph.isHostVertex(destination) ? graph.adj(destination).get(0) : destination;
            int routeId;
            if (!packetMapping.containsKey(p.id)) {
                if (!allPaths.containsKey(srcSwitch)) {
                    allPaths.put(srcSwitch, new HashMap<>());
                    counts.put(srcSwitch, new HashMap<>());
                }
                if (!allPaths.get(srcSwitch).containsKey(desSwitch))
                    kshortestpath(srcSwitch, desSwitch);
                int count = counts.get(srcSwitch).get(desSwitch);
                routeId =  count % allPaths.get(srcSwitch).get(desSwitch).size();
                packetMapping.put(p.id, routeId);
                counts.get(srcSwitch).put(desSwitch, count+1);
            }
            else routeId = packetMapping.get(p.id);
            if (allPaths.get(srcSwitch).get(desSwitch).isEmpty()) return -1;
            int index = allPaths.get(srcSwitch).get(desSwitch).get(routeId).indexOf(current)+1;
            return allPaths.get(srcSwitch).get(desSwitch).get(routeId).get(index);
        }
    }

    @Override
    public RoutingPath path(int source, int destination) {
        RoutingPath path = new RoutingPath();
        int srcSwitch = graph.isHostVertex(source) ? graph.adj(source).get(0) : source;
        int desSwitch = graph.isHostVertex(destination) ? graph.adj(destination).get(0) : destination;
        if (graph.isHostVertex(source)) path.path.add(source);
        if (srcSwitch == desSwitch) path.path.add(srcSwitch);
        else {
            if (!allPaths.containsKey(srcSwitch)) {
                allPaths.put(srcSwitch, new HashMap<>());
                counts.put(srcSwitch, new HashMap<>());
            }
            if (!allPaths.get(srcSwitch).containsKey(desSwitch))
                kshortestpath(srcSwitch, desSwitch);
            path.path.addAll(allPaths.get(srcSwitch).get(desSwitch).get(0));
        }
        if (graph.isHostVertex(destination)) path.path.add(destination);
        return path;
    }

    public double getAvgRTS() {
        int n = 0;
        for (Map<Integer, List<List<Integer>>> m1 : allPaths.values())
            for (List <List<Integer>> l1 : m1.values())
                for (List l : l1) n+=l.size();
        return (float) n/graph.switches().size();
    }
}
