package client;

import java.util.Arrays;
import java.util.Scanner;

import model.*;
import server.ServerFacade;
import requests.*;
import results.*;

import static ui.EscapeSequences.*;

public class PreLoginClient {
    private final ServerFacade server;
    public String authToken;
    public boolean loggedIn = false;
    public boolean quitted = false;

    public PreLoginClient(String serverUrl) throws Exception {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to chess");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit") && !loggedIn) {
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
        if (result.equals("quit")) {
            quitted = true;
        }
        System.out.println();
    }


//    public void notify(Notification notification) {
//        System.out.println(SET_TEXT_COLOR_RED + notification.message());
//        printPrompt();
//    }

    private void printPrompt() {
        System.out.print("\n[LOGGED OUT]>>> " + SET_TEXT_COLOR_GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterRequest registerRequest = new RegisterRequest(username, password, email);
            RegisterResult registerResult = server.register(registerRequest);
            authToken = registerResult.authToken();
            loggedIn = true;
            return String.format("Registration success! Welcome, %s\n", registerResult.username());
        }
        throw new Exception("Expected: <username> <password> <email>\n");
    }

    public String login(String... params) throws Exception {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            LoginRequest loginRequest = new LoginRequest(username, password);
            LoginResult loginResult = server.login(loginRequest);
            authToken = loginResult.authToken();
            loggedIn = true;
            return String.format("Logged in. Welcome back, %s", loginResult.username());
        }
        throw new Exception("Expected: <username> <password>");
    }

    public String help() {
        return """
                - register <username> <password> <email> - create an account
                - login <username> <password> - existing account
                - quit
                - help - see these options again
                """;
    }
}

