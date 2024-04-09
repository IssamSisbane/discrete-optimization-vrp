import RoadMap.Client;
import RoadMap.Depot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class SandBox {
    public static void main(String[] args) {
        Client c1 = new Client("c1", 1, 1, 1, 1, 1, 1);
        Client c2 = new Client("c2", 1, 1, 1, 1, 1, 1);
        Client c3 = new Client("c3", 1, 1, 1, 1, 1, 1);
        Client c4 = new Client("c4", 1, 1, 1, 1, 1, 1);
        Client c5 = new Client("c5", 1, 1, 1, 1, 1, 1);
        Depot depot = new Depot("d1", 1, 1, 1, 1, 0);


        LinkedList<Client> clients = new LinkedList<>();
        clients.add(c1);
        clients.add(c2);
        clients.add(c3);
        //clients.add(c4);

        ArrayList<String> circuit = new ArrayList<>();
        circuit.add(depot.getIdName());
        for(Client client : clients){
            circuit.add(client.getIdName());
        }
        circuit.add(depot.getIdName());
        System.out.println("Initial circuit: " + circuit);
        ArrayList<ArrayList<String>> allCircuits = new ArrayList<>();
        // 2-opt permutations
        for (int i = 1; i < circuit.size() - 2; i++) {
            for (int j = i + 1; j < circuit.size() - 1; j++) {
                ArrayList<String> newCircuit = new ArrayList<>(circuit.subList(0, i));
                ArrayList<String> reversedSublist = new ArrayList<>(circuit.subList(i, j + 1));
                Collections.reverse(reversedSublist);
                newCircuit.addAll(reversedSublist);
                newCircuit.addAll(circuit.subList(j + 1, circuit.size()));

                if(!allCircuits.contains(newCircuit)){
                    allCircuits.add(newCircuit);
                }

                // Reverse permutation
                newCircuit = new ArrayList<>(circuit.subList(0, i));
                reversedSublist = new ArrayList<>(circuit.subList(i, j + 1));
                Collections.reverse(reversedSublist);
                newCircuit.addAll(reversedSublist);
                newCircuit.addAll(circuit.subList(j + 1, circuit.size()));
                Collections.swap(newCircuit, i, j);

                if(!allCircuits.contains(newCircuit)){
                    allCircuits.add(newCircuit);
                }

            }
        }
        System.out.println("All circuits: " + allCircuits);
    }
}
