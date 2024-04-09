package Algo;

import RoadMap.Roadmap;

import java.util.ArrayList;
import java.util.HashMap;

public class HillClimbing {
    private Roadmap roadmap;

    private NeighborManager nm = new NeighborManager();

    public HillClimbing(Roadmap r) {
        this.roadmap = r;
        nm.setRoadmap(r);
    }

    public Roadmap run() {
        int i = 0;
        while(true){
            ArrayList<Roadmap> neighbors = nm.getAllNeighbours();
            System.out.println("Nombre de voisins : " + neighbors.size());
            Roadmap bestNeighbor = neighbors.get(0);
            for (Roadmap r : neighbors) {
                if (r.getDistance() < bestNeighbor.getDistance()) {
                    bestNeighbor = r;
                }
            }
            if (bestNeighbor.getDistance() < roadmap.getDistance()) {
                roadmap = bestNeighbor;
            }else{
                break;
            }
            nm.setRoadmap(roadmap);
            i++;
        }
        System.out.println("Nombre d'itération : " + i);
        return roadmap;
    }

    public HashMap<String, Integer> run2() {
        return nm.getAllNeighboursNumber();
    }
}
