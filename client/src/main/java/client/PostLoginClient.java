package client;


import model.GameData;
import requests.*;
import results.CreateResult;
import results.ListResult;
import server.ServerFacade;

import java.util.*;
import static ui.EscapeSequences.*;

public class PostLoginClient {
    private final ServerFacade server;
    private final String authToken;
    public boolean joinedGame = false;
    public boolean loggedOut = false;
    public String playerColor;
    public GameData joinedGameData;
    Map<String, Integer> gameNumberMap;

    public PostLoginClient(String serverUrl, String authToken, Map<String, Integer> gameNumberMap) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.gameNumberMap = gameNumberMap;
    }

    public void run() {
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var outcome = "";
        while (!loggedOut && !joinedGame) {
            printPrompt();
            String lines = scanner.nextLine();

            try {
                outcome = eval(lines);
                System.out.print(SET_TEXT_COLOR_BLUE + outcome);
            } catch (Throwable e) {
                var message = e.toString();
                System.out.print(message);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n[LOGGED IN]>>> " + SET_TEXT_COLOR_GREEN);
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
                case "watch" -> watch(params);
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String logout() throws Exception {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        server.logout(logoutRequest);
        loggedOut = true;
        return "Logged out.\n";
    }

    //debug
    public String create(String... params) throws Exception {
        if (params.length == 1) {
            String gameName = params[0];
            CreateRequest createRequest = new CreateRequest(gameName, authToken);
            CreateResult createResult = server.create(createRequest);
            list();
            return String.format("Game added. New game ID: %d\n", createResult.gameID());
        }
        throw new Exception("Expected: <gamename>");
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
            displayList.append((i+1)).append(". ").append(game.gameName())
                    .append(" - white: ").append(whiteUserName)
                    .append("; black: ").append(blackUserName)
                    .append("\n");
            gameNumberMap.put(String.valueOf(i+1), gamesList.get(i).gameID());
        }
        return displayList;
    }

    // investigate playing game 1... or as white?
    // Cannot invoke "java.lang.Integer.intValue()" because the return value of "java.util.Map.get(Object)" is null
    public String join(String... params) throws Exception {
        try {
            list();
            if ((params.length == 2) && (Integer.parseInt(params[0]) > 0) &&
                    (Integer.parseInt(params[0]) <= gameNumberMap.size())) {
                String gameNumber = params[0];
                playerColor = params[1];
                if ((!Objects.equals(playerColor, "white")) && (!Objects.equals(playerColor, "black"))) {
                    throw new Exception("player color must be white or black");
                }

                int gameID = gameNumberMap.get(gameNumber);
                JoinRequest joinRequest = new JoinRequest(playerColor, gameID, authToken);
                server.join(joinRequest);

                ListRequest listRequest = new ListRequest(authToken);
                ArrayList<GameData> gamesList = server.list(listRequest).games();
                for (GameData gameData : gamesList) {
                    if (gameData.gameID() == gameID) {
                        joinedGameData = gameData;
                        joinedGame = true;
                    }
                }

                return String.format("Joined game: %s", gameNumber);
            }
            throw new Exception("Expected: <game number listed> <player color>");
        } catch (NumberFormatException e) {
            System.out.println(SET_TEXT_COLOR_BLUE + params[0] +
                    "is not a number. To play a game, type 'play <game number as listed> <color you want to play as>");
        } throw new Exception(" ");
    }

    public String watch(String... params) throws Exception {
        list();
        if ((params.length == 1) && (Integer.parseInt(params[0]) > 0) &&
                (Integer.parseInt(params[0]) <= gameNumberMap.size())) {
            String gameNumber = params[0];
            int gameID = gameNumberMap.get(gameNumber);

            ArrayList<GameData> gamesList = server.list(new ListRequest(authToken)).games();
            for (GameData gameData : gamesList) {
                if (gameData.gameID() == gameID) {
                    joinedGame = true;
                    joinedGameData = gameData;
                }
            }
            return String.format("Joined game as observer: %s", gameNumber);
        }
        throw new Exception("Expected: <game number>");
    }

    public String help() {
        return """
                - logout
                - create <game name> - a new game
                - list - all existing games
                - play <gameID> [WHITE|BLACK]
                - watch <gameID>
                - help - see these options again
                """;
    }
}

