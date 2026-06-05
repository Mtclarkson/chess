package server;

import com.google.gson.Gson;
import org.junit.jupiter.api.function.Executable;
import requests.*;
import results.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    public ServerFacade(String url) {
        serverUrl = url;
    }

    private String authToken;

    public RegisterResult register (RegisterRequest request) throws Exception {
        var req = buildRequest("POST", "/user", request);
        var response = sendRequest(req);
        return handleResponse(response, RegisterResult.class);
        }

    public LoginResult login (LoginRequest request) throws Exception {
        var req = buildRequest("POST", "/session", request);
        var response = sendRequest(req);
        return handleResponse(response, LoginResult.class);
    }

    public CreateResult create (CreateRequest request) throws Exception {
        authToken = request.authToken();
        var req = buildRequest("POST", "/game", request);
        var response = sendRequest(req);
        return handleResponse(response, CreateResult.class);
    }

    public JoinResult join (JoinRequest request) throws Exception {
        authToken = request.authToken();
        request = new JoinRequest(request.playerColor().toUpperCase(), request.gameID(), request.authToken());
        var req = buildRequest("PUT", "/game", request);
        var response = sendRequest(req);
        return handleResponse(response, JoinResult.class);
    }

    public LogoutResult logout (LogoutRequest request) throws Exception {
        authToken = request.authToken();
        var req = buildRequest("DELETE", "/session", request);
        var response = sendRequest(req);
        return handleResponse(response, LogoutResult.class);
    }

    public ListResult list (ListRequest request) throws Exception {
        authToken = request.authToken();
        var req = buildRequest("GET", "/game", request);
        var response = sendRequest(req);
        return handleResponse(response, ListResult.class);
    }

    public void clear (ClearRequest request) throws Exception {
        var req = buildRequest("DELETE", "/db", request);
        sendRequest(req);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
            if (authToken != null) {
                request.setHeader("authorization", authToken);
            }
        }
        return request.build();
    }
//
    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }
//
    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
//
    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (status == 401) {
                throw new Exception("username or password is incorrect.");
            }
            if (status == 403) {
                throw new Exception("Sorry, that's already taken");
            }
            if (body != null) {
                throw new Exception("Input is empty");
            }
            throw new Exception("Error");
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status == 200;
    }
}
