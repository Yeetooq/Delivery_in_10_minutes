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
import java.util.ArrayList;
import java.util.List;

public class MapWithJFXPanel extends JFrame {
    private WebEngine webEngine;
    private List<String> addresses = new ArrayList<>();

    public MapWithJFXPanel() {
        setTitle("Яндекс Карты");
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
            webEngine.load(getClass().getResource("/resources/map.html").toExternalForm());
            Scene scene = new Scene(webView);
            jfxPanel.setScene(scene);
        });

        // Панель управления (Swing)
        JPanel controlPanel = new JPanel();
        JTextField addressField = new JTextField(20);
        JButton autoButton = new JButton("Auto");

        // Обработка события для ввода адреса
        addressField.addActionListener(e -> {
            String address = addressField.getText().trim();
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите адрес!");
                return;
            }

            // Отправка адреса в WebView для поиска ближайшей точки и построения маршрута
            Platform.runLater(() -> webEngine.executeScript(
                    "window.postMessage({type: 'findNearest', address: '" + address + "'}, '*')"
            ));
        });

        // Обработка кнопки "Auto"
        autoButton.addActionListener(e -> Platform.runLater(() -> webEngine.executeScript("showZonesAndMarkers()")));

        controlPanel.add(new JLabel("Введите адрес:"));
        controlPanel.add(addressField);
        controlPanel.add(autoButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    // Загрузка адресов из файла
    private void loadAddresses() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/addresses.txt"))) {
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
