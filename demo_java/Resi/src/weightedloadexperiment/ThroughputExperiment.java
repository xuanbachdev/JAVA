package weightedloadexperiment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Knuth;
import config.Constant;
import custom.fattree.FatTreeGraph;
import custom.fattree.FatTreeRoutingAlgorithm;
import network.Topology;
import simulator.DiscreteEventSimulator;;

public class ThroughputExperiment {
	private Topology network;
	
	public ThroughputExperiment(Topology network) {
        this.network = network;
    }

    public double[][] calThroughput(Map<Integer, Integer> trafficPattern, boolean verbose) {
        DiscreteEventSimulator simulator =
                new DiscreteEventSimulator(true, Constant.MAX_TIME, verbose);
        network.clear(); // clear all the data, queue, ... in switches, hosts
        network.setSimulator(simulator);
        
        int count = 0;
        for (Integer source : trafficPattern.keySet()) {
            Integer destination = trafficPattern.get(source);
            count++;
            network.getHostById(source).generatePacket(destination);
        }
        simulator.start();
        
        return null;
    }
    
    
    public static void main(String[] args) {

        //for(int timeOfRun = 0; timeOfRun < 100-3; timeOfRun++)
        {
            FatTreeGraph G = new FatTreeGraph(4);
            FatTreeRoutingAlgorithm ra = new FatTreeRoutingAlgorithm(G, false);
        
        
            Topology network = new Topology(G, ra);
            
            ThroughputExperiment experiment = new ThroughputExperiment(network);
            Integer[] hosts = G.hosts().toArray(new Integer[0]);
            
            Knuth.shuffle(hosts);
            List<Integer> sources = new ArrayList<>();
            List<Integer> destination = new ArrayList<>();
            sources.addAll(Arrays.asList(hosts).subList(0, hosts.length / 2));


            destination.addAll(Arrays.asList(hosts).subList(hosts.length / 2, hosts.length));

            Map<Integer, Integer> traffic = new HashMap<>();
            int sizeOfFlow = //1;
                                sources.size();
            for (int i = 0; i < sizeOfFlow; i++) {
                traffic.put(sources.get(i), destination.get(i));
                //traffic.put(destination.get(i), sources.get(i));
                //StdOut.printf("From source: %d To destination: %d\n", sources.get(i), destination.get(i));
            }

            experiment.calThroughput(traffic, false);
        }
    }

}
