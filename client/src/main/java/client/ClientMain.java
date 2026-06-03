package client;

import chess.*;

public class ClientMain {

    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        String authToken;
        String username;

        try {

            PreLoginClient preloginClient = new PreLoginClient(serverUrl);
            preloginClient.run();
            authToken = preloginClient.authToken;
            new PostLoginClient(serverUrl, authToken).run();
            new GameplayClient(serverUrl, authToken).run();

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }

}
