package GUI;

import Algo.HillClimbing;
import Algo.RandomAlgo;
import Algo.SimulatedAnnealing;
import Algo.TabouSearch;
import RoadMap.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUIRoadmap extends Application {
    private Map<String, Circle> circles = new HashMap<>();
    private static ArrayList<Client> clients = new ArrayList<>();

    private static Map<String, Label> labels = new HashMap<>();
    private static int roadNumberVisible = -1;
    public static Roadmap roadmap = new Roadmap();

    private static String algo;
    private static double ratio;
    private static int window_size = 900;
    public static void createRoadmap(String file){
        try {
            roadmap.fillRoadmap(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Label infoLabel4;


    @Override
    public void start(Stage stage) {
        // Créer des cercles et des étiquettes pour chaque point
        for(Client c : clients){
            Circle point = new Circle(c.getX()*ratio, c.getY()*ratio, 5, Color.RED);
            Label text = new Label(c.getIdName());
            text.setLayoutX(c.getX()*ratio);
            text.setLayoutY(c.getY()*ratio + 10);
            text.setStyle("-fx-font-size: 18px;-fx-text-fill: white;");
            labels.put(c.getIdName(), text);
            point.setOnMouseClicked(e -> {
                System.out.println(c.getIdName() + " | readyTime : " + c.getReadyTime() + " | dueTime : " + c.getDueTime());
            });
            circles.put(c.getIdName(), point);
        }

        Circle depotPoint = new Circle(roadmap.getDepot().getX()*ratio, roadmap.getDepot().getY()*ratio, 10, Color.BLUE);
        circles.put(roadmap.getDepot().getIdName(), depotPoint);
        Pane pane = new Pane();
        pane.getChildren().addAll(circles.values());
        //pane.getChildren().addAll(labels.values());

        ArrayList<Map<Node, Node>> roadsLinks = roadmap.getRoadsLinks();
        int roadNumber = 1;
        for (Map<Node, Node> road : roadsLinks) {
            if(roadNumberVisible == -1 || (roadNumberVisible != -1 && roadNumberVisible == roadNumber)){
                for (Map.Entry<Node, Node> entry : road.entrySet()) {
                    Line line = createLine(entry.getKey().getIdName(), entry.getValue().getIdName(), roadNumber);
                    pane.getChildren().add(line);
                }
            }
            roadNumber++;
        }

        VBox vBox = new VBox(pane);
        vBox.setStyle("-fx-background-color: #303030;");

        // Créer un HBox contenant les labels
        VBox hBox = new VBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 20, 10, 20));
        hBox.setStyle("-fx-background-color: #19191a;");

        Label infoLabel2 = new Label("Distance Totale : " + Math.round(roadmap.getDistance()));
        infoLabel2.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center;");
        hBox.getChildren().add(infoLabel2);

        Label infoLabel3 = new Label("Nombre de routes : " + roadmap.getRoadNumber());
        infoLabel3.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center;");
        hBox.getChildren().add(infoLabel3);

        infoLabel4 = new Label("Calcul en cours...");
        infoLabel4.setVisible(false);

        Label infoLabel = new Label("Run Algorithm");

        infoLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-weight: bold; -fx-alignment: center;");
        infoLabel.setBackground(new Background(new BackgroundFill(Color.web("#bababa"), CornerRadii.EMPTY, Insets.EMPTY)));
        infoLabel.setOnMouseClicked(e -> {
            infoLabel4.setVisible(true);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    nextGeneration();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Platform.runLater(() -> {
                    infoLabel4.setVisible(false);
                    start(stage);
                });
            });
        });
        hBox.getChildren().add(infoLabel);

//        for(Road r : roadmap.getRoads()){
//            Color routeColor = getColor(r.getId());
//            Rectangle colorSquare = new Rectangle(10, 18, routeColor);
//            Label label = new Label("Route " + r.getId() + " : " + Math.round(r.calcDistance()) + " / " + roadmap.getDepot().getDueTime());
//            label.setStyle("-fx-font-size: 18px; -fx-alignment: center; -fx-text-fill: white;");
//            label.setOnMouseClicked(e -> {
//                setRoadVisible(r.getId());
//                start(stage);
//            });
//            HBox hbox = new HBox(colorSquare, label);
//            hbox.setSpacing(10);
//            hBox.getChildren().add(hbox);
//        }

        // Placer le VBox à l'ouest et le HBox à l'est
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(vBox);
        borderPane.setRight(hBox);
        borderPane.setStyle("-fx-background-color: #303030;");


        infoLabel4.setStyle("-fx-font-size: 40px; -fx-text-fill: black; -fx-font-weight: bold; -fx-alignment: center;");
        infoLabel4.setBackground(new Background(new BackgroundFill(Color.web("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY)));
        infoLabel4.setPadding(new Insets(30, 50, 30, 50));


        StackPane layeredPane = new StackPane();
        layeredPane.getChildren().addAll(borderPane, infoLabel4);
        borderPane.toBack();

        // Créer une scène avec le conteneur et la dimension souhaitée
        Scene scene = new Scene(layeredPane, window_size + 250, window_size);


        // Configurer la fenêtre principale avec la scène
        stage.setScene(scene);
        stage.setTitle("Roadmap");
        stage.show();

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    // Attente de 500ms
                    Thread.sleep(500);
                    // Modification du texte du label dans la thread JavaFX
                    Platform.runLater(() -> {
                        String text = infoLabel4.getText();
                        if (text.endsWith("...")) {
                            text = "Exécution en cours";
                        } else {
                            text += ".";
                        }
                        infoLabel4.setText(text);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // Lancement du thread
        thread.start();


    }



    private static void setRoadVisible(int id){
        if(roadNumberVisible == id){
            roadNumberVisible = -1;
        }else{
            roadNumberVisible = id;
        }
    }

    private Line createLine(String start, String end, int roadNumber) {
        // Récupérer les cercles de début et de fin à partir des clés
        Circle startCircle = circles.get(start);
        Circle endCircle = circles.get(end);

        double slope = (startCircle.getCenterY() - endCircle.getCenterY()) / (startCircle.getCenterX() - endCircle.getCenterX());
        double lineAngle = Math.atan(slope);

        double arrowAngle = startCircle.getCenterX() > endCircle.getCenterX() ? Math.toRadians(45) : -Math.toRadians(225);

        startCircle.setFill(getColor(roadNumber));
        endCircle.setFill(getColor(roadNumber));

        // Créer la ligne reliant les deux cercles
        Line line = new Line(startCircle.getCenterX(), startCircle.getCenterY(),
                endCircle.getCenterX(), endCircle.getCenterY());

        // changer la couleur de la ligne
        line.setStroke(getColor(roadNumber));

        return line;
    }

    public static Color getColor(int id) {
        List<Color> colors = Arrays.asList(Color.web("#1f77b4"), Color.web("#ff7f0e"), Color.web("#2ca02c"), Color.web("#9467bd"), Color.web("#8c564b"), Color.web("#e377c2"), Color.web("#7f7f7f"), Color.web("#bcbd22"), Color.web("#17becf"), Color.web("#8dd3c7"), Color.web("#ffffb3"), Color.web("#bebada"), Color.web("#fb8072"), Color.web("#80b1d3"), Color.web("#fdb462"), Color.web("#b3de69"), Color.web("#fccde5"), Color.web("#d9d9d9"), Color.web("#bc80bd"), Color.web("#ccebc5"), Color.web("#ffed6f"), Color.web("#ff00ff"), Color.web("#00ffff"), Color.web("#ffff00"), Color.web("#00ff00"), Color.web("#ff0000"), Color.web("#0000ff"), Color.web("#000000"));
        return colors.get(id % colors.size());
    }



    public void launchWindow(String file, String algotype) throws IOException {
        createRoadmap(file);
        RandomAlgo ra = new RandomAlgo(roadmap);
        ra.calculRoads();
        roadmap = ra.getRoadmap();

        algo = algotype;


        clients = roadmap.getClients();
        double maxX = clients.stream().mapToDouble(Client::getX).max().orElse(0);
        double maxY = clients.stream().mapToDouble(Client::getY).max().orElse(0);
        double ratioX = maxX > 0 ? (window_size-50) / maxX : 1;
        double ratioY = maxY > 0 ? (window_size-50) / maxY : 1;
        ratio = Math.min(ratioX, ratioY);

        System.out.println("Nombre de clients : " + clients.size());
        System.out.println("Capacité d'un camion : " + roadmap.getMaxQuantity());
        System.out.println("Nombre minimal de camion nécessaire : " + (int) Math.ceil((double) roadmap.getNombreMinimal() /roadmap.getMaxQuantity()));

        System.out.println(roadmap.isRoadmapValid());


        System.out.println("Nombre de routes : " + roadmap.getRoadNumber());
        launch();
    }

    private static void nextGeneration() throws IOException {
        System.out.println("Génération en cours...");
        long startTime = System.currentTimeMillis();

        switch(algo){
            case "hillclimbing":
                HillClimbing hc = new HillClimbing(roadmap);
                roadmap = hc.run();
                break;
            case "simulatedannealing" :
                SimulatedAnnealing sa = new SimulatedAnnealing(roadmap, 100, 100, 0.999);
                roadmap = sa.run();
                break;
            case "taboo" :
                TabouSearch ts = new TabouSearch(roadmap, 100, 100);
                roadmap = ts.run();
                break;
            default :
                System.out.println("Aucun algorithme sélectionné");
                break;

        }

        long endTime = System.currentTimeMillis();
        System.out.println("Génération terminée, distance totale : " + roadmap.getDistance());
        System.out.println("Temps d'exécution : " + (endTime - startTime)/1000 + "s");
    }

}
