package app;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapWithJFXPanel extends JFrame {
    private WebEngine webEngine;
    private List<String> addresses = new ArrayList<>();

    public MapWithJFXPanel() {
        setTitle("OpenStreetMap Integration");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Platform.startup(() -> {});

        // Загрузка адресов из файла
        loadAddresses();

        // Инициализация JFXPanel
        JFXPanel jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);

        // Инициализация JavaFX UI
        Platform.runLater(() -> {
            WebView webView = new WebView();
            webEngine = webView.getEngine();

            // Загрузка HTML-файла карты
            URL resource = getClass().getClassLoader().getResource("map.html");
            if (resource == null) {
                System.err.println("Error: map.html not found! Current working directory: " + System.getProperty("user.dir"));
                return;
            }

            webEngine.load(resource.toExternalForm());
            Scene scene = new Scene(webView);
            jfxPanel.setScene(scene);
        });

        // Панель управления (Swing)
        JPanel controlPanel = new JPanel();
        JComboBox<String> startComboBox = new JComboBox<>(addresses.toArray(new String[0]));
        JTextField endField = new JTextField(15);
        JButton drawRouteButton = new JButton("Draw Route");

        drawRouteButton.addActionListener(e -> {
            String start = (String) startComboBox.getSelectedItem();
            String end = endField.getText();

            // Отправка данных в WebView для прорисовки маршрута
            Platform.runLater(() -> webEngine.executeScript(
                    "window.postMessage({type: 'route', start: '" + start + "', end: '" + end + "'}, '*')"
            ));
        });

        controlPanel.add(new JLabel("Start:"));
        controlPanel.add(startComboBox);
        controlPanel.add(new JLabel("End:"));
        controlPanel.add(endField);
        controlPanel.add(drawRouteButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    // Загрузка адресов из файла address.txt
    private void loadAddresses() {
    try (BufferedReader reader = new BufferedReader(
            new FileReader(getClass().getClassLoader().getResource("addresses.txt").getFile()))) {
        String line;
        while ((line = reader.readLine()) != null) {
            addresses.add(line.trim());
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Ошибка при загрузке адресов из файла.");
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MapWithJFXPanel mapApp = new MapWithJFXPanel();
            mapApp.setVisible(true);
        });
    }
}
