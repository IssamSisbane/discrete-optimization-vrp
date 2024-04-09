package RoadMap;

public class Client extends Node implements Cloneable{
    private int demand;

    public int roadNumber;

    public Client(String idName, int x, int y, int readyTime, int dueTime, int demand, int service) {
        super(idName, x, y, readyTime, dueTime, service);
        this.demand = demand;
    }

    public int getDemand() {
        return demand;
    }


}
