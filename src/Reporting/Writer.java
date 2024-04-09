package Reporting;

import Algo.HillClimbing;
import Algo.RandomAlgo;
import Algo.SimulatedAnnealing;
import Algo.TabouSearch;
import RoadMap.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Writer {

    public static void main(String[] args) throws IOException {
        writeTabouVSRecuit();
    }
    public static void writeTabouVSRecuit() {
        try {
            int nbExecutions = 2;

            List<File> files = new ArrayList<File>();
            files.add(new File("data101.vrp"));
            files.add(new File("data102.vrp"));
            files.add(new File("data111.vrp"));
            files.add(new File("data112.vrp"));
            files.add(new File("data201.vrp"));
            files.add(new File("data202.vrp"));
            files.add(new File("data1101.vrp"));
            files.add(new File("data1102.vrp"));
            files.add(new File("data1201.vrp"));
            files.add(new File("data1202.vrp"));

            // Créer un tableau de threads
            Queue<Thread> threads = new LinkedList<>();

            int nbTests = 5;
            ArrayList<Roadmap> roadmapsInit = new ArrayList<>();


            final int[] rowNumber = {1};

            for (File file : files) {
                for (int j = 0; j < nbTests; j++) {
                    Roadmap roadmap = new Roadmap();
                    roadmap.fillRoadmap(file.getName());
                    RandomAlgo ra = new RandomAlgo(roadmap);
                    ra.calculRoads();
                    roadmap = ra.getRoadmap();
                    roadmapsInit.add(roadmap);
                }

                for (int i = 0; i < nbTests; i++) {

                    for (int j = 0; j<2; j++) {
                        if (j == 0) {
                            int finalI = i;
                            Thread thread = new Thread(() -> {
                                try {
                                    System.out.println("\nFichier : " + file.getName());
                                    System.out.println("Nombre d'itérations : " + 1000);
                                    System.out.println("Taille de la liste tabou : " + 10);

                                    long startTime = System.currentTimeMillis();
                                    TabouSearch ts = new TabouSearch(roadmapsInit.get(finalI), 1000, 100);
                                    Roadmap roadmap = ts.run();
                                    long endTime = System.currentTimeMillis();

                                    Workbook workbook = new XSSFWorkbook();

                                    // Obtenir la feuille spécifique sur laquelle vous souhaitez écrire
                                    Sheet sheet = workbook.createSheet("Feuille1");

                                    // Utiliser synchronized pour écrire dans le fichier de manière thread-safe
                                    Row row = sheet.createRow(rowNumber[0]);

                                    Cell cell = row.createCell(0);
                                    cell.setCellValue(file.getName());

                                    cell = row.createCell(1);
                                    cell.setCellValue(roadmap.getClients().size());

                                    cell = row.createCell(2);
                                    cell.setCellValue("Tabou");

                                    cell = row.createCell(3);
                                    cell.setCellValue(ts.getTotalGeneratedNeighbors());

                                    cell = row.createCell(4);
                                    cell.setCellValue(roadmap.getNombreMinimal());

                                    cell = row.createCell(5);
                                    cell.setCellValue(roadmap.getRoadNumber());

                                    cell = row.createCell(6);
                                    cell.setCellValue(roadmapsInit.get(finalI).getDistance());

                                    cell = row.createCell(7);
                                    cell.setCellValue(roadmap.getDistance());

                                    cell = row.createCell(8);
                                    cell.setCellValue(100 - roadmap.getDistance() / roadmapsInit.get(finalI).getDistance() * 100);


                                    cell = row.createCell(9);
                                    cell.setCellValue((double) (endTime - startTime) / 1000);

                                    rowNumber[0] += 1;

                                    FileOutputStream outputStream = new FileOutputStream("src/Reporting/reports/Tabou_"+finalI+".xlsx");
                                    workbook.write(outputStream);
                                    outputStream.close();
                                } catch (IOException e) {
                                    System.out.println("Une erreur s'est produite lors de l'écriture dans le fichier.");
                                    e.printStackTrace();
                                }
                            });

                            // Créer un nouveau thread qui exécute une itération de la boucle
                            System.out.println("\nLancement du thread " + threads.size() + 1 + "/" + (nbTests * files.size()));
                            threads.add(thread);
                            thread.start();

                        } else {
                            int finalI = i;
                            Thread thread = new Thread(() -> {
                                try {
                                    System.out.println("\nFichier : " + file.getName());
                                    System.out.println("Nombre d'itérations : " + 1000);
                                    System.out.println("Taille de la liste tabou : " + 10);

                                    long startTime = System.currentTimeMillis();
                                    SimulatedAnnealing sa = new SimulatedAnnealing(roadmapsInit.get(finalI), 5, 0.3, 0.99);
                                    Roadmap roadmap = sa.run();
                                    long endTime = System.currentTimeMillis();

                                    Workbook workbook = new XSSFWorkbook();

                                    // Obtenir la feuille spécifique sur laquelle vous souhaitez écrire
                                    Sheet sheet = workbook.createSheet("Feuille1");

                                    // Utiliser synchronized pour écrire dans le fichier de manière thread-safe
                                    Row row = sheet.createRow(rowNumber[0]);

                                    Cell cell = row.createCell(0);
                                    cell.setCellValue(file.getName());

                                    cell = row.createCell(1);
                                    cell.setCellValue(roadmap.getClients().size());

                                    cell = row.createCell(2);
                                    cell.setCellValue("Recuit");

                                    cell = row.createCell(3);
                                    cell.setCellValue(sa.getTotalGeneratedNeighbors());

                                    cell = row.createCell(4);
                                    cell.setCellValue(roadmap.getNombreMinimal());

                                    cell = row.createCell(5);
                                    cell.setCellValue(roadmap.getRoadNumber());

                                    cell = row.createCell(6);
                                    cell.setCellValue(roadmapsInit.get(finalI).getDistance());

                                    cell = row.createCell(7);
                                    cell.setCellValue(roadmap.getDistance());

                                    cell = row.createCell(8);
                                    cell.setCellValue(100 - roadmapsInit.get(finalI).getDistance() / roadmap.getDistance() * 100);


                                    cell = row.createCell(9);
                                    cell.setCellValue((double) (endTime - startTime) / 1000);

                                    rowNumber[0] += 1;

                                    FileOutputStream outputStream = new FileOutputStream("src/Reporting/reports/Recuit_"+finalI+".xlsx");
                                    workbook.write(outputStream);
                                    outputStream.close();
                                } catch (IOException e) {
                                    System.out.println("Une erreur s'est produite lors de l'écriture dans le fichier.");
                                    e.printStackTrace();
                                }
                            });

                            // Créer un nouveau thread qui exécute une itération de la boucle
                            System.out.println("\nLancement du thread " + threads.size() + 1 + "/" + (nbTests * files.size() * nbExecutions));
                            threads.add(thread);
                            thread.start();
                        }
                    }
                }
            }

            // Attendre la fin de tous les threads
            int nbThreads = 0;
            long startTime = System.currentTimeMillis();
            for (Thread thread : threads) {
                thread.join();
                System.out.println("Fin du Thread " + nbThreads++ + "/" + threads.size());
            }
            long endTime = System.currentTimeMillis();

            System.out.println("\nTemps d'exécution total : " + (double) (endTime - startTime) / 1000 + " secondes");
            System.out.println("Les informations sur les executions ont été écrites dans Tabou.xlsx");
        } catch (IOException | InterruptedException e) {
            System.out.println("Une erreur s'est produite lors de l'écriture dans le fichier.");
            e.printStackTrace();
        }
    }

    public static void writeTabou() {
        try {
            int nbExecutions = 2;

            List<File> files = new ArrayList<File>();
            files.add(new File("data101.vrp"));
            files.add(new File("data102.vrp"));
            files.add(new File("data111.vrp"));
            files.add(new File("data112.vrp"));
            files.add(new File("data201.vrp"));
            files.add(new File("data202.vrp"));
            files.add(new File("data1101.vrp"));
            files.add(new File("data1102.vrp"));
            files.add(new File("data1201.vrp"));
            files.add(new File("data1202.vrp"));

            // Créer un tableau de threads
            Queue<Thread> threads = new LinkedList<>();

            int nbTests = 5;
            ArrayList<Roadmap> roadmapsInit = new ArrayList<>();


            final int[] rowNumber = {1};

            for (File file : files) {
                for (int j = 0; j < nbTests; j++) {
                    Roadmap roadmap = new Roadmap();
                    roadmap.fillRoadmap(file.getName());
                    RandomAlgo ra = new RandomAlgo(roadmap);
                    ra.calculRoads();
                    roadmap = ra.getRoadmap();
                    roadmapsInit.add(roadmap);
                }

                for (int i = 0; i < nbTests; i++) {
                    for (int j = 0; j<2; j++) {
                        int finalI = i;
                        Thread thread = new Thread(() -> {
                            try {
                                System.out.println("\nFichier : " + file.getName());
                                System.out.println("Nombre d'itérations : " + 1000);
                                System.out.println("Taille de la liste tabou : " + 10);

                                long startTime = System.currentTimeMillis();
                                TabouSearch ts = new TabouSearch(roadmapsInit.get(finalI), 1000, 100);
                                Roadmap roadmap = ts.run();
                                long endTime = System.currentTimeMillis();

                                Workbook workbook = new XSSFWorkbook();

                                // Obtenir la feuille spécifique sur laquelle vous souhaitez écrire
                                Sheet sheet = workbook.createSheet("Feuille1");

                                // Utiliser synchronized pour écrire dans le fichier de manière thread-safe
                                Row row = sheet.createRow(rowNumber[0]);

                                Cell cell = row.createCell(0);
                                cell.setCellValue(file.getName());

                                cell = row.createCell(1);
                                cell.setCellValue(roadmap.getClients().size());

                                cell = row.createCell(2);
                                cell.setCellValue(1000);

                                cell = row.createCell(3);
                                cell.setCellValue(100);

                                cell = row.createCell(4);
                                cell.setCellValue(roadmap.getNombreMinimal());

                                cell = row.createCell(5);
                                cell.setCellValue(roadmap.getRoadNumber());

                                cell = row.createCell(6);
                                cell.setCellValue(roadmapsInit.get(finalI).getDistance());

                                cell = row.createCell(7);
                                cell.setCellValue(roadmap.getDistance());

                                cell = row.createCell(8);
                                cell.setCellValue(100 - roadmap.getDistance() / roadmapsInit.get(finalI).getDistance() * 100);


                                cell = row.createCell(9);
                                cell.setCellValue((double) (endTime - startTime) / 1000);

                                rowNumber[0] += 1;

                                FileOutputStream outputStream = new FileOutputStream("src/Reporting/reports/Tabou_"+finalI+".xlsx");
                                workbook.write(outputStream);
                                outputStream.close();
                            } catch (IOException e) {
                                System.out.println("Une erreur s'est produite lors de l'écriture dans le fichier.");
                                e.printStackTrace();
                            }
                        });

                        // Créer un nouveau thread qui exécute une itération de la boucle
                        System.out.println("\nLancement du thread " + threads.size() + 1 + "/" + (nbTests * files.size()));
                        threads.add(thread);
                        thread.start();
                    }
                }
            }

            // Attendre la fin de tous les threads
            int nbThreads = 0;
            long startTime = System.currentTimeMillis();
            for (Thread thread : threads) {
                thread.join();
                System.out.println("Fin du Thread " + nbThreads++ + "/" + threads.size());
            }
            long endTime = System.currentTimeMillis();

            System.out.println("\nTemps d'exécution total : " + (double) (endTime - startTime) / 1000 + " secondes");
            System.out.println("Les informations sur les executions ont été écrites dans Tabou.xlsx");
        } catch (IOException | InterruptedException e) {
            System.out.println("Une erreur s'est produite lors de l'écriture dans le fichier.");
            e.printStackTrace();
        }
    }

    public static void writeRecuit() {
        try {
            int nbExecutions = 2;
            // Hashmap qui contient en clés le nombre d'itérations et en valeur les différentes taille de liste tabou à tester
            int[] iterations = new int[]{10, 100, 500, 1000};
            double[] acceptances = new double[]{0.3, 0.5, 0.8};
            double[] coolRates = new double[]{0.80, 0.90, 0.99};

            List<File> files = new ArrayList<File>();
            files.add(new File("data101.vrp"));
            files.add(new File("data102.vrp"));
            files.add(new File("data111.vrp"));
            files.add(new File("data112.vrp"));
            files.add(new File("data201.vrp"));
            files.add(new File("data202.vrp"));
            files.add(new File("data1101.vrp"));
            files.add(new File("data1102.vrp"));
            files.add(new File("data1201.vrp"));
            files.add(new File("data1202.vrp"));

            // Créer un tableau de threads
            Queue<Thread> threads = new LinkedList<>();

            FileInputStream fileInputStream = new FileInputStream("src/Reporting/reports/Recuit.xlsx");
            Workbook workbook = WorkbookFactory.create(fileInputStream);

            // Obtenir la feuille spécifique sur laquelle vous souhaitez écrire
            Sheet sheet = workbook.getSheet("Recuit_1");

            int nbTests = 2;
            ArrayList<Roadmap> roadmapsInit = new ArrayList<>();


            final int[] rowNumber = {1};

            for (File file : files) {

                for (int j = 0; j < nbTests; j++) {
                    Roadmap roadmap = new Roadmap();
                    roadmap.fillRoadmap(file.getName());
                    RandomAlgo ra = new RandomAlgo(roadmap);
                    ra.calculRoads();
                    roadmap = ra.getRoadmap();
                    roadmapsInit.add(roadmap);
                }

                for (int iteration: iterations) {
                    for (double acceptance: acceptances) {
                        for(double coolRate: coolRates) {
                            for (int i = 0; i < nbTests; i++) {
                                int finalI = i;
                                Thread thread = new Thread(() -> {
                                    try {
                                        System.out.println("\nFichier : " + file.getName());
                                        System.out.println("Nombre d'itérations : " + iteration);
                                        System.out.println("ProbaInit : " + acceptance);
                                        System.out.println("CoolRate : " + coolRate);

                                        long startTime = System.currentTimeMillis();
                                        SimulatedAnnealing re = new SimulatedAnnealing(roadmapsInit.get(finalI), iteration, acceptance, coolRate);
                                        Roadmap roadmap = re.run();
                                        long endTime = System.currentTimeMillis();


                                        // Utiliser synchronized pour écrire dans le fichier de manière thread-safe
                                        synchronized (sheet) {
                                            Row row = sheet.createRow(rowNumber[0]);

                                            Cell cell = row.createCell(0);
                                            cell.setCellValue(file.getName());

                                            cell = row.createCell(1);
                                            cell.setCellValue(roadmap.getClients().size());

                                            cell = row.createCell(2);
                                            cell.setCellValue(iteration);

                                            cell = row.createCell(3);
                                            cell.setCellValue(acceptance);

                                            cell = row.createCell(4);
                                            cell.setCellValue(re.getTemperatureInitiale());

                                            cell = row.createCell(5);
                                            cell.setCellValue(coolRate);


                                            cell = row.createCell(6);
                                            cell.setCellValue(roadmap.getNombreMinimal());

                                            cell = row.createCell(7);
                                            cell.setCellValue(roadmap.getRoadNumber());

                                            cell = row.createCell(8);
                                            cell.setCellValue(roadmapsInit.get(finalI).getDistance());

                                            cell = row.createCell(9);
                                            cell.setCellValue(roadmap.getDistance());

                                            cell = row.createCell(10);
                                            cell.setCellValue(roadmapsInit.get(finalI).getRoadNumber() / roadmap.getDistance() * 100);


                                            cell = row.createCell(11);
                                            cell.setCellValue((double) (endTime - startTime) /1000);

                                            rowNumber[0] += 1;

                                            FileOutputStream outputStream = new FileOutputStream("src/Reporting/reports/Recuit.xlsx");
                                            workbook.write(outputStream);
                                            outputStream.close();

                                        }
                                    } catch (IOException e) {
                                        System.out.println("Une erreur s'est produite lors de l'écriture dans le fichier.");
                                        e.printStackTrace();
                                    }
                                });

                                // Créer un nouveau thread qui exécute une itération de la boucle
                                System.out.println("\nLancement du thread " + threads.size()+1 + "/" + (files.size() * acceptances.length * iterations.length * coolRates.length * nbTests));
                                threads.add(thread);
                                thread.start();
                            }
                        }
                    }
                }
            }

            // Attendre la fin de tous les threads
            int nbThreads = 0;
            for (Thread thread : threads) {
                thread.join();
                System.out.println("Fin du Thread " + nbThreads++ + "/" + threads.size());
            }

            workbook.close();
            fileInputStream.close();
            System.out.println("Les informations sur les executions ont été écrites dans Recuit.xlsx");
        } catch (IOException | InterruptedException e) {
            System.out.println("Une erreur s'est produite lors de l'écriture dans le fichier.");
            e.printStackTrace();
        }
    }

    public static void writeCountvoisins() throws IOException {
        List<File> files = new ArrayList<File>();
        files.add(new File("data101.vrp"));
        files.add(new File("data102.vrp"));
        files.add(new File("data111.vrp"));
        files.add(new File("data112.vrp"));
        files.add(new File("data201.vrp"));
        files.add(new File("data202.vrp"));
        files.add(new File("data1101.vrp"));
        files.add(new File("data1102.vrp"));
        files.add(new File("data1201.vrp"));
        files.add(new File("data1202.vrp"));

        HashMap<String, Integer> allNeighbours = new HashMap<String, Integer>();
        //allNeighbours.addAll(getReverseNeighbours());
        allNeighbours.put("relocateIntra", 0);
        allNeighbours.put("relocateInter", 0);
        allNeighbours.put("TwoOptInter", 0);
        allNeighbours.put("TwoOptIntra", 0);
        allNeighbours.put("getExchangeInter", 0);
        allNeighbours.put("exchangeIntre", 0);

        for (File file: files) {
            for (int i=0; i<100; i++) {
                Roadmap roadmap = new Roadmap();
                roadmap.fillRoadmap(file.getName());
                RandomAlgo ra = new RandomAlgo(roadmap);
                ra.calculRoads();
                roadmap = ra.getRoadmap();
                HillClimbing hc = new HillClimbing(roadmap);
                HashMap<String, Integer> currentNeigbours = hc.run2();

                for(Map.Entry<String, Integer> entry : currentNeigbours.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    allNeighbours.put(key, allNeighbours.get(key) + value);
                }
            }
        }

        for(Map.Entry<String, Integer> entry : allNeighbours.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key + " : " + value / 1000);
        }
    }


}
