import RoadMap.*;

import java.io.*;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Arrays.setAll;

public class Main {

    public static void main(String[] args) throws IOException {
        Client c1 = new Client("c1", 1, 1, 1, 1, 1, 1);
        Client c2 = new Client("c2", 1, 1, 1, 1, 1, 1);
        Client c3 = new Client("c3", 1, 1, 1, 1, 1, 1);
        Client c4 = new Client("c4", 1, 1, 1, 1, 1, 1);
        Client c5 = new Client("c5", 1, 1, 1, 1, 1, 1);
        Depot depot = new Depot("d1", 1, 1, 1, 1, 0);

        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(depot);
        nodes.add(c1);
        nodes.add(c2);
        nodes.add(c3);
        nodes.add(c4);
        nodes.add(depot);

        Road road = new Road(depot);
        road.setNodesList(nodes);

        Roadmap roadmap = new Roadmap();
        roadmap.addRoad(road);


        twoopt(roadmap);
    }



    public static void twoopt(Roadmap roadmap){
        ArrayList<Roadmap> neighbours = new ArrayList<Roadmap>();
        for(Road road : roadmap.getRoads()){
            LinkedList<Node> nodes = road.getNodesList();
            // Pour chaque paire de points on teste l'échange
            for(int i=1; i<nodes.size()-2; i++){
                for(int j=i+1; j<nodes.size()-1; j++){
                    LinkedList<Node> newClients = new LinkedList<>();
                    // On inverse la séquence des points entre i et j
                    for(int k=0; k<i; k++){
                        newClients.add(nodes.get(k));
                    }
                    for(int k=j; k>=i; k--){
                        newClients.add(nodes.get(k));
                    }
                    for(int k=j+1; k<nodes.size(); k++){
                        newClients.add(nodes.get(k));
                    }
                    System.out.println();
                    for(Node n : newClients){
                        System.out.print(n.getIdName());
                    }
                }
            }
        }
    }
}