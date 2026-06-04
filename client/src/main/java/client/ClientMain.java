package client;

import chess.*;
import model.GameData;

public class ClientMain {

    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        State state = State.PRE_LOGIN;
        String authToken = "";
        String playerColor = "";
        GameData gameData = null;
        boolean quit = false;
        PreLoginClient preloginClient;

        try {
            while (!quit) {

                switch (state) {
                    //state actions and transitions
                    case PRE_LOGIN:
                        preloginClient = new PreLoginClient(serverUrl);
                        preloginClient.run();
                        quit = preloginClient.quitted;
                        authToken = preloginClient.authToken;

                        if (preloginClient.loggedIn) {
                            state = State.POST_LOGIN;
                        }

                        break;
                    case POST_LOGIN:
                        PostLoginClient postLoginClient = new PostLoginClient(serverUrl, authToken);
                        postLoginClient.run();
                        gameData = postLoginClient.joinedGameData;
                        playerColor = postLoginClient.playerColor;

                        if (postLoginClient.joinedGame) {
                            state = State.GAMEPLAY;
                        }
                        else if (postLoginClient.loggedOut) {
                            state = State.PRE_LOGIN;
                        }

                        break;
                    case GAMEPLAY:
                        new GameplayClient(serverUrl, authToken, gameData, playerColor).run();
                        state = State.POST_LOGIN;
                        break;
                }

            }
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }

}
