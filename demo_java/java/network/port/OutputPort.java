package network.port;

import config.Constant;

import java.util.HashSet;
import java.util.Set;

public class OutputPort extends Port{
    private int creditCount = Constant.QUEUE_SIZE;
    public boolean swFlag = false;//bang true nghia la OutputPort van con ranh roi, no van chua duoc them goi tin khac
    public boolean stFlag = false;//bang true nghia la OutputPort ko ban, no san sang gui goi tin tren link
    private Set<Integer> requestList;

    public OutputPort(int id) {
        super(id);
        this.requestList = new HashSet<>();
    }

    public int getCreditCount() {
        return creditCount;
    }

    public void increaseCreditCount() {
        this.creditCount++;
    }

    public void decreaseCreditCount() { this.creditCount--; }

    public void addRequest(int inPortId) { requestList.add(inPortId); }

    public void removeRequest(int inPortId) { requestList.remove(inPortId); }

    public Set<Integer> getRequestList() {
        return requestList;
    }
}
