package Algo;

import RoadMap.*;

import java.util.*;

public class NeighborManager {

    private Roadmap roadmap;

    private int generationNumber = 0;
    private ArrayList<Roadmap> neighbours = new ArrayList<Roadmap>();
    private Roadmap bestRoadmap;

    public NeighborManager(){}

    public void setRoadmap(Roadmap roadmap) {
        this.roadmap = roadmap;
    }

    public Roadmap getClone(Roadmap r){
        return r.clone();
    }

    public Roadmap getRoadmap() {
        return roadmap;
    }

    public Roadmap getNextGenRoadmap() {
        this.bestRoadmap = this.roadmap;
        for(Roadmap r : getAllNeighbours()){
            if(r.getDistance() < this.bestRoadmap.getDistance()){
                this.bestRoadmap = r;
            }
        }
        this.generationNumber++;
        this.roadmap = this.bestRoadmap;
        return this.bestRoadmap;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public ArrayList<Roadmap> getAllNeighbours(){
        ArrayList<Roadmap> allNeighbours = new ArrayList<Roadmap>();
        //allNeighbours.addAll(getReverseNeighbours());
        allNeighbours.addAll(getRelocateIntraNeighbours());
        allNeighbours.addAll(getRelocateInterNeighbours());
        allNeighbours.addAll(getTwoOptInterNeighbours());
        allNeighbours.addAll(getTwoOptIntraNeighbours());
        allNeighbours.addAll(getExchangeInterNeighbours());
        allNeighbours.addAll(getCrossExchangeInterNeighbours());
        return allNeighbours;
    }

    public HashMap<String, Integer> getAllNeighboursNumber(){
        HashMap<String, Integer> allNeighbours = new HashMap<String, Integer>();
        //allNeighbours.addAll(getReverseNeighbours());
        allNeighbours.put("relocateIntra", getRelocateIntraNeighbours().size());
        allNeighbours.put("relocateInter", getRelocateInterNeighbours().size());
        allNeighbours.put("TwoOptInter", getTwoOptInterNeighbours().size());
        allNeighbours.put("TwoOptIntra", getTwoOptIntraNeighbours().size());
        allNeighbours.put("getExchangeInter", getExchangeInterNeighbours().size());
        allNeighbours.put("exchangeIntre", getCrossExchangeInterNeighbours().size());
        return allNeighbours;
    }
    public ArrayList<Roadmap> getReverseNeighbours(){
        ArrayList<Roadmap> reverseNeighbours = new ArrayList<Roadmap>();
        // Pour chaque route de la roadmap initiale
        for(Road r : roadmap.getRoads()){
            // On crée un voisin qui reverse la route actuelle
            Roadmap reverseNeighbour = getClone(roadmap);
            Collections.reverse(reverseNeighbour.getRoads().get(roadmap.getRoads().indexOf(r)).getNodesList());
            reverseNeighbours.add(reverseNeighbour);
        }
        return null;
    }

    public ArrayList<Roadmap> getTwoOptIntraNeighbours(){
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
                    Roadmap neighbour = getClone(roadmap);
                    neighbour.getRoads().get(roadmap.getRoads().indexOf(road)).setNodesList(newClients);
                    if(neighbour.isRoadmapValid()){
                        neighbours.add(neighbour);
                    }
                }
            }
        }
        return neighbours;
    }

    public ArrayList<Roadmap> getRelocateIntraNeighbours(){
        ArrayList<Roadmap> neighbours = new ArrayList<Roadmap>();
        for(Road road : this.roadmap.getRoads()){
            List<LinkedList<Node>> allNewClients = new ArrayList<>();
            LinkedList<Node> nodes = road.getNodesList();
            // Pour chaque point on teste les différentes combinaisons
            for(int i=0; i<nodes.size()-1; i++){
                if(i != 0){
                    ArrayList<Integer> indexToInsert = new ArrayList<>();
                    for(int j=0; j<nodes.size(); j++){
                        if(j != i && j != i-1 && j != i+1 && j != 0){
                            indexToInsert.add(j);
                        }
                    }
                    for(int k=0; k<indexToInsert.size(); k++){
                        LinkedList<Node> newClients = new LinkedList<>();
                        for(int l=0; l<nodes.size(); l++){
                            if(l == i){
                                continue;
                            }
                            if(l == indexToInsert.get(k)){
                                newClients.add(nodes.get(i));
                            }
                            newClients.add(nodes.get(l));
                        }
                        allNewClients.add(newClients);
                    }
                }
            }
            for(LinkedList<Node> newClients : allNewClients){
                Roadmap neighbour = getClone(this.roadmap);
                neighbour.getRoads().get(this.roadmap.getRoads().indexOf(road)).setNodesList(newClients);
                if(neighbour.isRoadmapValid()){
                    neighbours.add(neighbour);
                }
            }
        }


        return neighbours;
    }

    public ArrayList<Roadmap> getRelocateInterNeighbours(){
        ArrayList<Roadmap> neighbours = new ArrayList<Roadmap>();
        for(Road currentRoad : roadmap.getRoads()){
            for(Node currentNode : currentRoad.getNodesList()){
                // Si c'est un client
                if(currentNode instanceof Client){
                    // On parcourt toutes les autres routes
                    for(Road otherRoad : roadmap.getRoads()){
                        if(currentRoad.getId() != otherRoad.getId()){
                            for(int i=1; i<otherRoad.getNodesList().size()-1; i++){
                                Roadmap neighbour = getClone(this.roadmap);
                                LinkedList<Node> newCurrentRoad = new LinkedList<Node>(neighbour.getRoads().get(this.roadmap.getRoads().indexOf(currentRoad)).getNodesList());
                                newCurrentRoad.remove(currentNode);
                                LinkedList<Node> newOtherRoad = new LinkedList<Node>(neighbour.getRoads().get(this.roadmap.getRoads().indexOf(otherRoad)).getNodesList());
                                newOtherRoad.add(i, currentNode);
                                //System.out.println("Current road : " + currentRoad.getId() + " Other road : " + otherRoad.getId() + " " + i);
                                neighbour.getRoads().get(this.roadmap.getRoads().indexOf(currentRoad)).setNodesList(newCurrentRoad);
                                neighbour.getRoads().get(this.roadmap.getRoads().indexOf(otherRoad)).setNodesList(newOtherRoad);
                                if(neighbour.isRoadmapValid()){
                                    neighbours.add(neighbour);
                                }
                            }
                        }
                    }
                }
            }
        }

        return neighbours;
    }

    public ArrayList<Roadmap> getTwoOptInterNeighbours(){
        ArrayList<Roadmap> neighbours = new ArrayList<Roadmap>();
        for(int i=0; i<roadmap.getRoads().size()-1; i++){
            Road road1 = roadmap.getRoads().get(i);
            for(int j=i+1; j<roadmap.getRoads().size(); j++){
                Road road2 = roadmap.getRoads().get(j);
                LinkedList<Node> nodes1 = road1.getNodesList();
                LinkedList<Node> nodes2 = road2.getNodesList();
                // Pour chaque paire de points, un dans chaque route, on teste l'échange
                for(int k=1; k<nodes1.size()-1; k++){
                    for(int l=1; l<nodes2.size()-1; l++){
                        LinkedList<Node> newNodes1 = new LinkedList<Node>(nodes1);
                        LinkedList<Node> newNodes2 = new LinkedList<Node>(nodes2);
                        Node node1 = newNodes1.get(k);
                        Node node2 = newNodes2.get(l);
                        newNodes1.set(k, node2);
                        newNodes2.set(l, node1);
                        Roadmap neighbour = getClone(this.roadmap);
                        neighbour.getRoads().get(i).setNodesList(newNodes1);
                        neighbour.getRoads().get(j).setNodesList(newNodes2);
                        if(neighbour.isRoadmapValid()){
                            neighbours.add(neighbour);
                        }
                    }
                }
            }
        }
        return neighbours;
    }

    public ArrayList<Roadmap> getExchangeInterNeighbours(){
        ArrayList<Roadmap> neighbours = new ArrayList<Roadmap>();
        for (int i = 0; i < this.roadmap.getRoads().size(); i++) {
            Road road1 = this.roadmap.getRoads().get(i);
            for (int j = i + 1; j < this.roadmap.getRoads().size(); j++) {
                Road road2 = this.roadmap.getRoads().get(j);
                for (int k = 1; k < road1.getNodesList().size() - 1; k++) {
                    for (int l = 1; l < road2.getNodesList().size() - 1; l++) {
                        Roadmap neighbour = getClone(this.roadmap);
                        LinkedList<Node> newRoad1 = new LinkedList<Node>(neighbour.getRoads().get(i).getNodesList());
                        LinkedList<Node> newRoad2 = new LinkedList<Node>(neighbour.getRoads().get(j).getNodesList());
                        Node node1 = newRoad1.get(k);
                        Node node2 = newRoad2.get(l);
                        newRoad1.set(k, node2);
                        newRoad2.set(l, node1);
                        neighbour.getRoads().get(i).setNodesList(newRoad1);
                        neighbour.getRoads().get(j).setNodesList(newRoad2);
                        if (neighbour.isRoadmapValid()) {
                            neighbours.add(neighbour);
                        }
                    }
                }
            }
        }
        return neighbours;
    }

    public ArrayList<Roadmap> getCrossExchangeInterNeighbours() {
        ArrayList<Roadmap> neighbours = new ArrayList<Roadmap>();
        for (int i = 0; i < roadmap.getRoads().size() - 1; i++) {
            for (int j = i + 1; j < roadmap.getRoads().size(); j++) {
                Roadmap neighbour = getClone(this.roadmap);
                Road road1 = neighbour.getRoads().get(i);
                Road road2 = neighbour.getRoads().get(j);
                for (int k = 1; k < road1.getNodesList().size() - 1; k++) {
                    for (int l = 1; l < road2.getNodesList().size() - 1; l++) {
                        LinkedList<Node> newNodes1 = new LinkedList<Node>(road1.getNodesList());
                        LinkedList<Node> newNodes2 = new LinkedList<Node>(road2.getNodesList());
                        Node node1 = newNodes1.remove(k);
                        Node node2 = newNodes2.remove(l);
                        newNodes1.add(k, node2);
                        newNodes2.add(l, node1);
                        if (neighbour.isRoadmapValid()) {
                            Road temp1 = new Road(road1.getDepot());
                            temp1.setNodesList(newNodes1);
                            temp1.setId(road1.getId());
                            Road temp2 = new Road(road2.getDepot());
                            temp2.setNodesList(newNodes2);
                            temp2.setId(road2.getId());
                            neighbour.getRoads().set(i, temp1);
                            neighbour.getRoads().set(j, temp2);
                            neighbours.add(neighbour);
                        }
                    }
                }
            }
        }
        return neighbours;
    }





}
