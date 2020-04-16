package network;

//import simulator.DiscreteEventSimulator;

public abstract class Device {
    public int id;

    //protected static DiscreteEventSimulator sim;

    public Device(int id) {
        this.id = id;
    }

    public void clear() {}

    //public static void setSim(DiscreteEventSimulator sim) {
    //    NetworkObject.sim = sim;
    //}
}
