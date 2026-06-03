package client;

import chess.*;
import model.GameData;

public class ClientMain {

    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        String authToken;
        String playerColor;
        GameData gameData;

        try {

            PreLoginClient preloginClient = new PreLoginClient(serverUrl);
            preloginClient.run();
            authToken = preloginClient.authToken;

            PostLoginClient postLoginClient = new PostLoginClient(serverUrl, authToken);
            postLoginClient.run();
            gameData = postLoginClient.joinedGameData;
            playerColor = postLoginClient.playerColor;

            new GameplayClient(serverUrl, authToken, gameData, playerColor).run();

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }

}
