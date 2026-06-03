package client;

import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.Gson;
import model.*;
import server.ServerFacade;
import requests.*;
import results.*;

import javax.management.Notification;

import static ui.EscapeSequences.*;

public class PreLoginClient {
    private ServerFacade server;
    private State state = State.PRE_LOGIN;

    public PreLoginClient(String serverUrl) throws Exception {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to chess");
        //System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
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


//    public void notify(Notification notification) {
//        System.out.println(SET_TEXT_COLOR_RED + notification.message());
//        printPrompt();
//    }

    private void printPrompt() {
        System.out.print("\n>>> " + SET_TEXT_COLOR_GREEN);
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

    // make new register request
    public String register(String... params) throws Exception {
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterRequest registerRequest = new RegisterRequest(username, password, email);
            RegisterResult registerResult = server.register(registerRequest);
            return String.format("Registration success! Welcome, %s", registerResult.username());
        }
        throw new Exception("Expected: <yourname>");
    }

    public String login(String... params) throws Exception {
        return "";
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

