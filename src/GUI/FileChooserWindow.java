package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class FileChooserWindow extends JFrame {

    private JComboBox<String> fileComboBox;
    private JButton okButton;
    private JButton cancelButton;
    private List<File> files;

    public FileChooserWindow(List<File> files) {
        super("Paramètres");
        this.files = files;
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(450, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));
        getContentPane().add(mainPanel);

        JLabel fileLabel = new JLabel("Choisissez un fichier :");
        fileLabel.setHorizontalAlignment(JLabel.CENTER);
        fileLabel.setVerticalAlignment(JLabel.CENTER);
        fileLabel.setFont(new Font("Arial", Font.BOLD, 17));
        fileLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(fileLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));

        fileComboBox = new JComboBox<String>();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
        for (File file : files) {
            model.addElement(file.getName());
        }
        fileComboBox.setModel(model);
        centerPanel.add(fileComboBox);
        JLabel algoLabel = new JLabel("Choisissez un algorithme :");
        algoLabel.setHorizontalAlignment(JLabel.CENTER);
        algoLabel.setVerticalAlignment(JLabel.CENTER);
        algoLabel.setFont(new Font("Arial", Font.BOLD, 17));
        algoLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        centerPanel.add(algoLabel);

        JPanel radioPanel = new JPanel(new FlowLayout());
        ButtonGroup radioGroup = new ButtonGroup();
        JRadioButton geneticButton = new JRadioButton("Méthode Descente");
        JRadioButton simulatedButton = new JRadioButton("Recuit simulé");
        JRadioButton tabooButton = new JRadioButton("Tabou");
        geneticButton.setSelected(true);
        radioGroup.add(geneticButton);
        radioGroup.add(simulatedButton);
        radioGroup.add(tabooButton);
        radioPanel.add(geneticButton);
        radioPanel.add(simulatedButton);
        radioPanel.add(tabooButton);
        centerPanel.add(radioPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFile = (String) fileComboBox.getSelectedItem();
                String selectedAlgorithm = "";
                if (geneticButton.isSelected()) {
                    selectedAlgorithm = "hillclimbing";
                } else if (simulatedButton.isSelected()) {
                    selectedAlgorithm = "simulatedannealing";
                } else if (tabooButton.isSelected()) {
                    selectedAlgorithm = "taboo";
                }
                setVisible(false);
                dispose();
                // passer le choix de l'utilisateur à la fenêtre de graphique
                GUIRoadmap graphWindow = new GUIRoadmap();
                try {
                    graphWindow.launchWindow(selectedFile, selectedAlgorithm);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(okButton);

        cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        buttonPanel.add(cancelButton);
    }




    public static void main(String[] args) {
        // exemple d'utilisation
        List<File> files = new ArrayList<File>();
        files.add(new File("data101.vrp"));
        files.add(new File("data101noTW.vrp"));
        files.add(new File("data102.vrp"));
        files.add(new File("data111.vrp"));
        files.add(new File("data112.vrp"));
        files.add(new File("data201.vrp"));
        files.add(new File("data202.vrp"));
        files.add(new File("data1101.vrp"));
        files.add(new File("data1102.vrp"));
        files.add(new File("data1201.vrp"));
        files.add(new File("data1202.vrp"));
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FileChooserWindow fileChooser = new FileChooserWindow(files);
            }
        });
    }
}

