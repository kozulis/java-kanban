package ru.yandex.praktikum.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String apiToken;
    private final URI url;
    private final HttpClient client;
    private HttpResponse<String> response;

    public KVTaskClient(URI url) throws IOException, InterruptedException {
        this.url = url;
        client = HttpClient.newHttpClient();
        register();
    }

    /**
     * регистрация и получение токена
     */
    private void register() {
        try {
            URI uri = URI.create(url + "/register");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-type", "application/json")
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            checkStatusCode(response);
            apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Возникла ошибка при регистрации.\n" + e.getMessage());
        }
    }

    /**
     * сохранение менеджера задач
     */
    public void put(String key, String json) {
        URI newUri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(newUri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-type", "application/json")
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            checkStatusCode(response);
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка." +
                    "Проверьте, пожалуйста, URL-адрес и повторите попытку.\n" + e.getMessage());
        }
    }

    /**
     * загрузка менеджера задач
     */
    public String load(String key) {
        URI newUrl = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(newUrl)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-type", "application/json")
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            checkStatusCode(response);
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + newUrl + "', возникла ошибка.\n"
                    + e.getMessage());
        }
        return response.body();
    }

    /**
     * проверка кода
     */
    private void checkStatusCode(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        if (statusCode != 200 && statusCode != 204) {
            System.out.println("Сервер сообщил о проблеме с запросом. Код состояния: " + statusCode);
            System.out.println(response.body());
        }
    }
}

