package RoadMap;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Roadmap implements Cloneable {
    private ArrayList<Road> roads = new ArrayList<Road>();
    private ArrayList<Client> clients = new ArrayList<Client>();
    public HashMap<Client, Boolean> visited = new HashMap<Client, Boolean>();
    private Depot depot;
    private int maxQuantity;
    public Roadmap(){}

    public void addRoad(Road road){
        this.roads.add(road);
    }
    public void printRoadmap(){
        for (Road road : this.roads) {
            road.printRoad();
        }
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public Depot getDepot() {
        return depot;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    public ArrayList<Map<Node, Node>> getRoadsLinks() {
        ArrayList<Map<Node, Node>> roadsLinks = new ArrayList<>();
        for (Road road : this.roads) {
            roadsLinks.add(road.getEdges());
        }
        return roadsLinks;
    }


    public void fillRoadmap(String filename) throws IOException {
        File file = new File("src\\Datasets\\" + filename);
        BufferedReader br = null;
        br = new BufferedReader(new FileReader(file));
        String st;
        int i = 0;
        while ((st = br.readLine()) != null) {
            if(st.contains("MAX_QUANTITY")){
                this.maxQuantity = Integer.parseInt(st.split(": ")[1]);
            }
            if(st.contains("DATA_DEPOTS")){
                st = br.readLine();
                String[] depotData = st.split(" ");
                this.depot = new Depot(depotData[0], Integer.parseInt(depotData[1]), Integer.parseInt(depotData[2]), Integer.parseInt(depotData[3]), Integer.parseInt(depotData[4]), 0);
            }
            if(st.contains("DATA_CLIENTS")){
                st = br.readLine();
                while(st != null){
                    String[] clientData = st.split(" ");
                    Client client = new Client(clientData[0], Integer.parseInt(clientData[1]), Integer.parseInt(clientData[2]), Integer.parseInt(clientData[3]), Integer.parseInt(clientData[4]), Integer.parseInt(clientData[5]), Integer.parseInt(clientData[6]));
                    this.clients.add(client);
                    this.visited.put(client, false);
                    st = br.readLine();
                }
            }
        }
    }

    public double getDistanceBetweenCoords(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public double getDistance() {
        double distance = 0;
        for (Road road : this.roads) {
            distance += road.calcDistance();
        }
        return distance;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public boolean isRoadmapValid(){
        int nbClientsVisited = 0;
        for (Road road : this.roads) {
            double uniteTemps = 0;

            for(int i = 0; i < road.getNodesList().size() - 1; i++){
                if(road.getNodesList().get(i) instanceof Client){
                    nbClientsVisited++;
                }
                // Vérifier si l'unité de temps ne dépasse pas le dueTime du prochain client
                uniteTemps += getDistanceBetweenCoords(road.getNodesList().get(i).getX(), road.getNodesList().get(i).getY(), road.getNodesList().get(i + 1).getX(), road.getNodesList().get(i + 1).getY());
                if(road.getNodesList().get(i +1).getDueTime() < uniteTemps){
                    return false;
                }
                if(uniteTemps < road.getNodesList().get(i+1).getReadyTime()){
                    uniteTemps = road.getNodesList().get(i+1).getReadyTime();
                }
                uniteTemps += road.getNodesList().get(i+1).getService();
            }
            if(road.calcUniteTemps() > this.depot.getDueTime()){
                return false;
            }
            if(road.calcQuantity() > this.maxQuantity){
                return false;
            }
            if(road.getNodesList().getFirst() instanceof Client || road.getNodesList().getLast() instanceof Client){
                return false;
            }
        }
        if(nbClientsVisited != this.clients.size()){
            return false;
        }
        return true;
    }

    public int getNombreMinimal() {
        double demandTotal = 0;
        for (Client c: clients) {
            demandTotal += c.getDemand();
        }

        return (int) Math.ceil(demandTotal / getMaxQuantity());
    }

    @Override
    public Roadmap clone() {
        Roadmap clone = new Roadmap();
        clone.roads = new ArrayList<Road>();
        for(Road r : this.roads){
            clone.roads.add(r.clone());
        }
        clone.clients = new ArrayList<Client>(this.clients);
        clone.visited = new HashMap<Client, Boolean>();
        for(Client c : this.clients){
            clone.visited.put(c, false);
        }
        clone.depot = this.depot;
        clone.maxQuantity = this.maxQuantity;
        return clone;
    }

    public int getRoadNumber(){
        int roadNumber = 0;
        for(Road r : roads){
            if(r.getNodesList().size() > 2){
                roadNumber++;
            }
        }
        return roadNumber;
    }

}
