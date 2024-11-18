package app;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MapWithJFXPanel extends JFrame {
    private WebEngine webEngine;

    public MapWithJFXPanel() {
        setTitle("OpenStreetMap Integration");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Platform.startup(() -> {});

        // Инициализация JFXPanel
        JFXPanel jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);

        // Инициализация JavaFX UI
        Platform.runLater(() -> {
            WebView webView = new WebView();
            webEngine = webView.getEngine();

            // Загрузка HTML-файла карты
            URL resource = getClass().getResource("/map.html");
            if (resource == null) {
                System.err.println("Error: map.html not found!");
                System.err.println("Ensure the file exists in the resources folder.");
                return; // Прекращаем выполнение, если файл не найден
            }

            // Если файл найден, загружаем его
            webEngine.load(resource.toExternalForm());

            // Установка JavaFX сцены в JFXPanel
            Scene scene = new Scene(webView);
            jfxPanel.setScene(scene);
        });

        // Панель управления (Swing)
        JPanel controlPanel = new JPanel();
        JTextField startField = new JTextField(15);
        JTextField endField = new JTextField(15);
        JButton drawRouteButton = new JButton("Draw Route");

        drawRouteButton.addActionListener(e -> {
            String start = startField.getText();
            String end = endField.getText();
            // Отправка данных в WebView для прорисовки маршрута
            Platform.runLater(() -> webEngine.executeScript(
                    "window.postMessage({type: 'route', start: '" + start + "', end: '" + end + "'}, '*')"
            ));
        });

        controlPanel.add(new JLabel("Start:"));
        controlPanel.add(startField);
        controlPanel.add(new JLabel("End:"));
        controlPanel.add(endField);
        controlPanel.add(drawRouteButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MapWithJFXPanel mapApp = new MapWithJFXPanel();
            mapApp.setVisible(true);
        });
    }
}
