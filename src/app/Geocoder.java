package app;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Geocoder {
    private static final String API_URL = "https://geocode-maps.yandex.ru/1.x/";

    public static void main(String[] args) {
        String apiKey = "f2206a66-2fdf-432e-b948-9b64ddffc204"; // Ваш API ключ

        // Ввод адреса с клавиатуры
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите адрес: ");
        String inputAddress = scanner.nextLine();

        // Получение координат для введённого адреса
        String inputCoordinates = getCoordinatesForAddress(inputAddress, apiKey);
        if (inputCoordinates == null) {
            System.err.println("Не удалось получить координаты для введённого адреса.");
            return;
        }

        System.out.println("Координаты введённого адреса: " + inputCoordinates);

        // Чтение точек из файла
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/addresses.txt"))) {
            String nearestPoint = null;
            double minDistance = Double.MAX_VALUE;

            String line;
            while ((line = reader.readLine()) != null) {
                // Получение координат из строки адреса в файле
                String fileCoordinates = line.trim(); // Извлекаем координаты, они должны быть в формате "широта,долгота"
                if (!fileCoordinates.isEmpty()) {
                    // Расчёт расстояния
                    double distance = calculateDistance(inputCoordinates, fileCoordinates);
                    System.out.printf("Расстояние до %s: %.2f км%n", fileCoordinates, distance);

                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestPoint = fileCoordinates;
                    }
                }
            }

            // Вывод ближайшей точки
            if (nearestPoint != null) {
                System.out.println("Ближайшая точка: " + nearestPoint + " (расстояние: " + minDistance + " км)");
            } else {
                System.err.println("Не удалось определить ближайшую точку.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    private static String getCoordinatesForAddress(String address, String apiKey) {
        try {
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String requestUrl = API_URL + "?apikey=" + apiKey + "&format=json&geocode=" + encodedAddress;

            HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    return parseCoordinates(response.toString());
                }
            } else {
                System.err.println("Ошибка HTTP-запроса: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            System.err.println("Ошибка при получении координат: " + e.getMessage());
        }
        return null;
    }

    private static String parseCoordinates(String jsonResponse) {
        try {
            int posIndex = jsonResponse.indexOf("\"pos\"");
            if (posIndex != -1) {
                int startIndex = jsonResponse.indexOf(":", posIndex) + 1;
                int endIndex = jsonResponse.indexOf("}", startIndex);
                if (startIndex > 0 && endIndex > startIndex) {
                    String coordinatesLine = jsonResponse.substring(startIndex, endIndex)
                            .replace("\"", "").trim();
                    String[] coords = coordinatesLine.split(" ");
                    if (coords.length == 2) {
                        return coords[1] + "," + coords[0]; // координаты: долгота, широта
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка парсинга JSON-ответа: " + e.getMessage());
        }
        return null;
    }

    private static double calculateDistance(String coords1, String coords2) {
        String[] split1 = coords1.split(",");
        String[] split2 = coords2.split(",");

        double lat1 = Math.toRadians(Double.parseDouble(split1[0])); // широта
        double lon1 = Math.toRadians(Double.parseDouble(split1[1])); // долгота
        double lat2 = Math.toRadians(Double.parseDouble(split2[0])); // широта
        double lon2 = Math.toRadians(Double.parseDouble(split2[1])); // долгота

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double earthRadius = 6371.0; // Радиус Земли в км

        return earthRadius * c;
    }
}
