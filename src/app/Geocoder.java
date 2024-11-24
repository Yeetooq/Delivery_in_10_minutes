package app;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Geocoder {
    private static final String API_URL = "https://geocode-maps.yandex.ru/1.x/";

    public static void main(String[] args) {
        String apiKey = "f2206a66-2fdf-432e-b948-9b64ddffc204"; // Ваш API ключ
        String address = "Зеленоград к435";

        String coordinates = getCoordinatesForAddress(address, apiKey);
        if (coordinates != null) {
            System.out.println("Координаты для адреса \"" + address + "\": " + coordinates);
        } else {
            System.err.println("Не удалось получить координаты для адреса \"" + address + "\".");
        }
    }

    private static String getCoordinatesForAddress(String address, String apiKey) {
        try {
            // Кодируем адрес для URL
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String requestUrl = API_URL + "?apikey=" + apiKey + "&format=json&geocode=" + encodedAddress;

            //System.out.println("URL запроса: " + requestUrl);

            // Создание HTTP-запроса
            HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
            connection.setRequestMethod("GET");

            // Получение кода ответа
            int responseCode = connection.getResponseCode();
            //System.out.println("HTTP код ответа: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    //System.out.println("Ответ API: " + response.toString());
                    return parseCoordinates(response.toString());
                }
            } else {
                System.err.println("Ошибка HTTP-запроса: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            System.err.println("Ошибка при получении координат: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static String parseCoordinates(String jsonResponse) {
        try {
            // Ищем "pos" в JSON
            int posIndex = jsonResponse.indexOf("\"pos\"");
            if (posIndex != -1) {
                // Извлекаем координаты из строки
                int startIndex = jsonResponse.indexOf(":", posIndex) + 1;
                int endIndex = jsonResponse.indexOf("}", startIndex);
                if (startIndex > 0 && endIndex > startIndex) {
                    String coordinatesLine = jsonResponse.substring(startIndex, endIndex)
                            .replace("\"", "").trim();
                    String[] coords = coordinatesLine.split(" ");

                    // Проверяем, что координаты корректны
                    if (coords.length == 2) {
                        return coords[1] + ", " + coords[0]; // Поменять местами широту и долготу
                    } else {
                        System.err.println("Неверный формат координат: " + coordinatesLine);
                    }
                } else {
                    System.err.println("Координаты не найдены в указанном диапазоне строки JSON.");
                }
            } else {
                System.err.println("Ключ \"pos\" не найден в JSON-ответе.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка парсинга JSON-ответа: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
