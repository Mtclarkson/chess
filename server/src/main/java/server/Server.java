package server;

import com.google.gson.Gson;
import model.*;
import service.*;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Server {

    private final Javalin javalin;
    private final AuthService authService;
    private final UserService userService;
    private final GameService gameService;

    record RegisterResult(String username, String authToken) {}
    record LoginResult(String username, String authToken) {}
    record ListResult(ArrayList<GameData> games) {}
    record CreateResult(int gameID) {}
    record JoinRequest(String playerColor, int gameID) {}



    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clear)
                .post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::list)
                .post("/game", this::create)
                .put("/game", this::join);
        AuthDAO authDAO = new AuthMemory();
        this.authService = new AuthService(authDAO);
        UserDAO userDAO = new UserMemory();
        this.userService = new UserService(userDAO);
        GameDAO gameDAO = new GameMemory();
        this.gameService = new GameService(gameDAO);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private boolean isNullOrBlank(String input) {
        return (input == null) || (input.isEmpty());
    }

    private void register(Context ctx) throws DataAccessException {
        try {//ask TAs about this one
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);

            // works?
            if (isNullOrBlank(user.username()) || isNullOrBlank(user.password()) || isNullOrBlank(user.email())) {
                throw new BadRequestException("Error: bad request");
            }

            // works?
            if (userService.getUser(user.username()) != null) {
                throw new AlreadyTakenException("Error: already taken");
            }

            user = userService.createUser(user);
            AuthData authdata = authService.createAuth(new AuthData(UUID.randomUUID().toString(), user.username()));
            RegisterResult registerResult = new RegisterResult(user.username(), authdata.authToken());
            ctx.result(new Gson().toJson(registerResult));
            ctx.status(200);

        }
        catch (AlreadyTakenException ex) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (Exception ex) {
            ctx.status(500);
        }
    }

    private void login(Context ctx) throws DataAccessException {
        try { //ask TAs about this one
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);

            // works?
            if (isNullOrBlank(user.username()) || isNullOrBlank(user.password())) {
                throw new BadRequestException("Error: bad request");
            }

            String givenPassword = user.password();
            user = userService.getUser(user.username());

            if ((user == null) || (!user.password().equals(givenPassword))) {
                throw new WrongPasswordException("Error: unauthorized");
            }

            AuthData authdata = authService.createAuth(new AuthData(UUID.randomUUID().toString(), user.username()));
            LoginResult loginResult = new LoginResult(user.username(), authdata.authToken());
            ctx.result(new Gson().toJson(loginResult));
            ctx.status(200);

        } catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (WrongPasswordException ex) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (Exception ex) {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        }
    }

    private void logout(Context ctx) throws DataAccessException {
        try { //ask TAs about this one
            String authToken = ctx.header("authorization");
            AuthData authData = authService.getAuth(authToken);

            if (authData == null) {
                throw new WrongPasswordException("Error: unauthorized");
            }

            authService.deleteAuth(authData);
            ctx.status(200);

        } catch (WrongPasswordException ex) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (Exception ex) {
            ctx.status(500);
        }
    }

    private void list(Context ctx) throws DataAccessException {
        try { //ask TAs about this one
            String authToken = ctx.header("authorization");
            AuthData authData = authService.getAuth(authToken);

            if (authData == null) {
                throw new WrongPasswordException("Error: unauthorized");
            }

            ListResult listResult = new ListResult(gameService.listGames());
            ctx.result(new Gson().toJson(listResult));
            ctx.status(200);

        } catch (WrongPasswordException ex) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (Exception ex) {
            ctx.status(500);
        }
    }

    private void create(Context ctx) throws DataAccessException {
        try {
            GameData game = new Gson().fromJson(ctx.body(), GameData.class);
            String authToken = ctx.header("authorization");
            AuthData authData = authService.getAuth(authToken);

            if (isNullOrBlank(game.gameName())) {
                throw new BadRequestException("Error: bad request");
            }

            if (authData == null) {
                throw new WrongPasswordException("Error: unauthorized");
            }

            game = gameService.createGame(game.gameName());
            CreateResult createResult = new CreateResult(game.gameID());
            ctx.result(new Gson().toJson(createResult));
            ctx.status(200);

        } catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (WrongPasswordException ex) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (Exception ex) {
            ctx.status(500);
        }
    }

    private void join(Context ctx) throws DataAccessException {
        try {
            JoinRequest joinRequest = new Gson().fromJson(ctx.body(), JoinRequest.class);
            String authToken = ctx.header("authorization");
            AuthData authData = authService.getAuth(authToken);

            if (authData == null) {
                throw new WrongPasswordException("Error: unauthorized");
            }

            String joiningPlayerColor = joinRequest.playerColor();

            if (joinRequest.gameID()==0 || joiningPlayerColor == null ||
                    (!joiningPlayerColor.equals("WHITE") && !joiningPlayerColor.equals("BLACK"))) {
                throw new BadRequestException("Error: bad request");
            }

            GameData game = gameService.getGame(joinRequest.gameID());

            if (game == null) {
                throw new Exception("Error: game doesn't exist");
            }

            if ((game.whiteUsername()!=null) && (game.blackUsername()!=null)) {
                throw new AlreadyTakenException("Error: already taken");
            }

            if (joiningPlayerColor.equals("WHITE")) {
                if (game.whiteUsername()!=null) {
                    throw new AlreadyTakenException("Error: already taken");
                } gameService.updateGame(joiningPlayerColor, authData.username(), game.gameID());
            }

            else {
                if (game.blackUsername()!=null) {
                    throw new AlreadyTakenException("Error: already taken");
                } gameService.updateGame(joiningPlayerColor, authData.username(), game.gameID());
            }

            ctx.status(200);

        } catch (BadRequestException ex) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (WrongPasswordException ex) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (AlreadyTakenException ex) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        } catch (Exception ex) {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        }
    }

    public void clear(Context ctx) throws DataAccessException {
        authService.clearAllAuthTokens();
        gameService.clearAllGames();
        userService.clearAllUsers();
        ctx.status(200);
    }


}
