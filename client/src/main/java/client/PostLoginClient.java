package client;


import model.GameData;
import requests.*;
import results.CreateResult;
import results.ListResult;
import results.RegisterResult;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class PostLoginClient {
    private final ServerFacade server;
    private final String authToken;
    private boolean joinedGame = false;

    public PostLoginClient(String serverUrl, String authToken) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
    }

    public void run() {
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit") && !joinedGame) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n>>> " + SET_TEXT_COLOR_GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list().toString();
                case "play" -> join(params);
//                case "watch" -> watch(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String logout() throws Exception {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        server.logout(logoutRequest);
        return "Logged out.\n";
    }

    public String create(String... params) throws Exception {
        if (params.length == 1) {
            String gameName = params[0];
            CreateRequest createRequest = new CreateRequest(gameName, authToken);
            CreateResult createResult = server.create(createRequest);
            return String.format("Game added. New game ID: %d\n", createResult.gameID());
        }
        throw new Exception("Expected: <username> <password> <email>");
    }

    public StringBuilder list() throws Exception {
        StringBuilder displayList = new StringBuilder();
        ListRequest listRequest = new ListRequest(authToken);
        ListResult listResult = server.list(listRequest);
        ArrayList<GameData> gamesList = listResult.games();
        for (int i=0; i < gamesList.size(); i++) {
            GameData game = gamesList.get(i);
            String whiteUserName = (game.whiteUsername() == null) ? "empty" : game.whiteUsername();
            String blackUserName = (game.blackUsername() == null) ? "empty" : game.blackUsername();
            displayList.append((i + 1)).append(". ").append(game.gameName())
                    .append(" - white: ").append(whiteUserName)
                    .append("; black: ").append(blackUserName)
                    .append("\n");
        }
        return displayList;
    }

    public String join(String... params) throws Exception {
        if (params.length == 2) {
            int gameID = Integer.parseInt(params[0]);
            String playerColor = params[1];
            JoinRequest joinRequest = new JoinRequest(playerColor, gameID, authToken);
            server.join(joinRequest);
            joinedGame = true;
            return ("Joined game:");
        }
        throw new Exception("Expected: <game number> <player color>");
    }

    public String help() {
        return """
                - logout
                - create <game name> - a new game
                - list - all existing games
                - play <gameID> [WHITE|BLACK]
                - watch <gameID>
                - quit
                - help - see these options again
                """;
    }
}

