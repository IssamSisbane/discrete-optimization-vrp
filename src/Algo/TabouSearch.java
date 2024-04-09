package Algo;

import RoadMap.Roadmap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TabouSearch {
    private int tabuListSize;
    private int iterations;

    private Roadmap initialRoadmap;
    private NeighborManager nm = new NeighborManager();

    private int totalGeneratedNeighbors;
    public TabouSearch(Roadmap initialRoadmap, int iterations, int tabuListSize) {
        this.iterations = iterations;
        this.tabuListSize = tabuListSize;
        nm.setRoadmap(initialRoadmap);
        this.initialRoadmap = initialRoadmap;
    }

    public Roadmap run() {
        totalGeneratedNeighbors = 0;
        Roadmap bestRoadmap = initialRoadmap;
        double bestCost = initialRoadmap.getDistance();
        Queue<Double> tabuList = new LinkedList<>();
        Roadmap last = null;

        for (int i=0; i<iterations; i++) {
            ArrayList<Roadmap> neighbors = nm.getAllNeighbours();
            totalGeneratedNeighbors += neighbors.size();

            Roadmap bestNeighbor = neighbors.get(0);
            double bestNeighborCost = bestNeighbor.getDistance();

            for (Roadmap neighbor: neighbors) {
                double neighborCost = neighbor.getDistance();
                if (neighborCost < bestNeighborCost) {
                    boolean found = false;
                    for(Double d : tabuList){
                        if (d.equals(neighborCost)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        bestNeighborCost = neighborCost;
                        bestNeighbor = neighbor;
                    }
                }
            }



            if (bestNeighborCost < bestCost) {
                bestCost = bestNeighborCost;
                bestRoadmap = bestNeighbor;
            }

            //System.out.println("Best neighboor : " + bestNeighborCost + " / " + nm.getRoadmap().getDistance());
            if (bestNeighborCost > nm.getRoadmap().getDistance()) {
                if (tabuList.size() >= tabuListSize) {
                    tabuList.remove();
                }
                boolean found = false;
                for(Double d : tabuList){
                    if (d.equals(nm.getRoadmap().getDistance())) {
                        found = true;
                        break;
                    }
                }
                if(!found){
                    tabuList.add(nm.getRoadmap().getDistance());
                }
            }
            nm.setRoadmap(bestNeighbor);
        }


        return bestRoadmap;
    }

    public int getTotalGeneratedNeighbors(){
        return totalGeneratedNeighbors;
    }

}
