package RoadMap;

import org.w3c.dom.html.HTMLAnchorElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Road implements Cloneable {
    private int id;
    private static int idCounter = 1;
    private Depot depot;
    private LinkedList<Node> nodes = new LinkedList<>();

    public Road(Depot depot) {
        this.id = idCounter;
        idCounter++;
        this.depot = depot;
    }

    public Depot getDepot() {
        return depot;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void printRoad(){
        System.out.println("Road " + this.id + " :");
        for (int i = 0; i< nodes.size(); i++){
            if(i == nodes.size()-1){
                System.out.printf("%s", nodes.get(i).getIdName());
                continue;
            }
            System.out.printf("%s --> ", nodes.get(i).getIdName());
        }
        System.out.println();
    }

    public HashMap<Node, Node> getEdges() {
        HashMap<Node, Node> nodeMap = new HashMap<>();
        int i = 0;
        for(i = 0; i < nodes.size() - 1; i++) {
            nodeMap.put(nodes.get(i), nodes.get(i + 1));
        }
        nodeMap.put(nodes.get(i-1), nodes.getLast());

        return nodeMap;
    }

    public double calcDistance(){
        double distance = 0;
        for(int i = 0; i < nodes.size() - 1; i++) {
            distance += getDistanceBetweenCoords(nodes.get(i).getX(), nodes.get(i).getY(), nodes.get(i + 1).getX(), nodes.get(i + 1).getY());
        }
        return distance;
    }

    public double calcUniteTemps(){
        double uniteTemps = 0;
        for(int i = 0; i < nodes.size() - 1; i++) {
            uniteTemps += getDistanceBetweenCoords(nodes.get(i).getX(), nodes.get(i).getY(), nodes.get(i + 1).getX(), nodes.get(i + 1).getY());
            if(uniteTemps < nodes.get(i+1).getReadyTime()){
                uniteTemps = nodes.get(i+1).getReadyTime();
            }
            uniteTemps += nodes.get(i+1).getService();
        }
        return uniteTemps;
    }

    private double getDistanceBetweenCoords(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public int getId() {
        return id;
    }

    public Node getLastNode() {
        return this.nodes.getLast();
    }

    public int calcQuantity() {
        int quantity = 0;
        for (Node node : this.nodes) {
            if(node instanceof Client)
                quantity += ((Client) node).getDemand();
        }
        return quantity;
    }

    public LinkedList<Node> getNodesList() {
        return this.nodes;
    }

    public void setNodesList(LinkedList<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public Road clone() {
        try {
            Road clone = (Road) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void addNode(Node node) {
        this.nodes.add(node);
        if(node instanceof Client)
            ((Client) node).roadNumber = this.id;
    }

    public void removeNode(Node node) {
        this.nodes.remove(node);
    }
}
