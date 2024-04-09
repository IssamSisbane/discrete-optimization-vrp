package Algo;

import RoadMap.Roadmap;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    private Roadmap currentRoadmap;
    private NeighborManager nm = new NeighborManager();
    private int iterations;
    private double acceptance;
    private double coolingRate;

    private double temperatureInitiale;
    private Random random = new Random();

    private int totalGeneratedNeighbors;

    public SimulatedAnnealing(Roadmap initialRoadmap, int iterations, double acceptance, double coolingRate) {
        this.iterations = iterations;
        this.acceptance = acceptance;
        this.coolingRate = coolingRate;
        this.currentRoadmap = initialRoadmap;
        nm.setRoadmap(initialRoadmap);
    }

    public Roadmap run() {
        totalGeneratedNeighbors = 0;

        double temperature = 0;
        Roadmap bestRoadmap = currentRoadmap;
        double bestCost = currentRoadmap.getDistance();

        // On initialise la température
        double biggestDelta = 0;
        ArrayList<Roadmap> neighborsForTemp = nm.getAllNeighbours();
        totalGeneratedNeighbors += neighborsForTemp.size();

        for(Roadmap r : neighborsForTemp){
            double delta = r.getDistance() - bestCost;
            if(delta > biggestDelta){
                biggestDelta = delta;
            }
        }

        temperature = -biggestDelta / Math.log(acceptance);
        temperatureInitiale = temperature;

        System.out.println("Temperature initiale : " + temperature);

        int iterationActuelle = 0;
        while (temperature > 0.001) {
            for (int i =0; i<iterations; i++) {

                currentRoadmap = nm.getRoadmap();

                // On récupère un voisin aléatoire
                ArrayList<Roadmap> neighbors = nm.getAllNeighbours();
                Roadmap nextRoadmap = neighbors.get(random.nextInt(neighbors.size()));

                // On calcule le delta
                double delta = nextRoadmap.getDistance() - currentRoadmap.getDistance();

                if (delta < 0) {
                    // Si le delta est négatif, on accepte le voisin
                    nm.setRoadmap(nextRoadmap);
                    if (nextRoadmap.getDistance() < bestCost) {
                        bestCost = nextRoadmap.getDistance();
                        bestRoadmap = nextRoadmap;
                    }
                } else {
                    // Si le delta est positif, on accepte le voisin avec une probabilité
                    double proba = Math.exp(-delta / temperature);
                    if (random.nextDouble() < proba) {
                        nm.setRoadmap(nextRoadmap);
                    }
                }

                // Mise à jour de la temperature
                temperature *= coolingRate;
            }
        }

        return bestRoadmap;
    }


    public double getTemperatureInitiale(){
        return temperatureInitiale;
    }

    public int getTotalGeneratedNeighbors(){
        return totalGeneratedNeighbors;
    }
}
