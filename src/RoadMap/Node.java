package RoadMap;

public abstract class  Node {
    private String idName;
    private int x;
    private int y;
    private int readyTime;
    private int dueTime;

    private int service;

    public Node(String idName, int x, int y, int readyTime, int dueTime, int service) {
        this.idName = idName;
        this.x = x;
        this.y = y;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
        this.service = service;
    }

    public String getIdName() {
        return idName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getReadyTime() {
        return readyTime;
    }

    public int getDueTime() {
        return dueTime;
    }

    public int getService() {
        return service;
    }
}
