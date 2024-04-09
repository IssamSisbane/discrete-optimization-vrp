package Algo;

import RoadMap.Client;
import RoadMap.Road;
import RoadMap.Roadmap;

import java.util.Collections;
import java.util.Random;

public class RandomAlgo {
    private Roadmap roadmap;

    public RandomAlgo(Roadmap roadmap) {
        this.roadmap = roadmap;
    }

    public void calculRoads(){
        int clientNotVisited = this.roadmap.getClients().size();
        while(clientNotVisited > 0 ){
            Random rand = new Random();
            Road road = new Road(this.roadmap.getDepot());
            double distance = 0;
            double capacity = 0;
            road.addNode(this.roadmap.getDepot());
            int i = 0;
            while(true){
                // Sélectionner au hasard le premier Client qui peut être ajouté à la route
                Collections.shuffle(this.roadmap.getClients());
                Client client = null;
                for(Client c : this.getRoadmap().getClients()){
                    if(!this.roadmap.visited.get(c) && (distance + this.roadmap.getDistanceBetweenCoords(road.getLastNode().getX(), road.getLastNode().getY(), c.getX(), c.getY())) <= c.getDueTime()){
                        client = c;
                        break;
                    }
                }

                if(client == null){
                    break;
                }
                if(i == 0){
                    distance += this.roadmap.getDistanceBetweenCoords(this.roadmap.getDepot().getX(), this.roadmap.getDepot().getY(), client.getX(), client.getY());
                    if(distance < client.getReadyTime()){
                        distance = client.getReadyTime();
                    }
                    distance += client.getService();
                }
                else{
                    distance += this.roadmap.getDistanceBetweenCoords(road.getLastNode().getX(), road.getLastNode().getY(), client.getX(), client.getY());
                    if(distance < client.getReadyTime()){
                        distance = client.getReadyTime();
                    }
                    distance += client.getService();
                }
                double distanceToDepot = this.roadmap.getDistanceBetweenCoords(client.getX(), client.getY(), this.roadmap.getDepot().getX(), this.roadmap.getDepot().getY());
                capacity += client.getDemand();
                if(capacity <= this.roadmap.getMaxQuantity() && distance <= this.roadmap.getDepot().getDueTime() - distanceToDepot){
                    road.addNode(client);
                    this.roadmap.visited.put(client, true);
                    clientNotVisited--;
                    i++;
                }
                else{
                    break;
                }
            }
            road.addNode(this.roadmap.getDepot());
            this.roadmap.addRoad(road);
        }
    }

    public Roadmap getRoadmap() {
        return roadmap;
    }
}
