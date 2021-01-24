package io.lungchen.sbipbm;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class RequestClient {

    private static RequestClient requestClient;
    private HttpClient httpClient;

    private RequestClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public static RequestClient getRequestClient() {

        if (requestClient == null) {
            requestClient = new RequestClient();
        }

        return requestClient;
    }

    public void get(String url, RequestBody requestbody) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(requestbody.toJSONString())).build();

        httpClient.sendAsync(request,  HttpResponse.BodyHandlers.ofString())
                .thenApplyAsync(HttpResponse::body)
                .thenAcceptAsync(System.out::println).join();
    }

    public void post(String url, RequestBody requestBody) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody.toJSONString())).build();

        httpClient.sendAsync(request,  HttpResponse.BodyHandlers.ofString())
                .thenApplyAsync(HttpResponse::body)
                .thenAcceptAsync(System.out::println).join();
    }

}
