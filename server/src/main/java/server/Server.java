package server;

import com.google.gson.Gson;
import model.*;
import service.*;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;

import java.util.ArrayList;
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
        try { //ask TAs about this one
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);

            // works?
            if (isNullOrBlank(user.username()) || isNullOrBlank(user.password()) || isNullOrBlank(user.email())) {
                throw new BadRequestException("Error: Username, password, and email are required");
            }

            // works?
            if (userService.getUser(user.username()) != null) {
                throw new AlreadyTakenException("Error: Username is taken");
            }

            user = userService.createUser(user);
            AuthData authdata = authService.createAuth(new AuthData(UUID.randomUUID().toString(), user.username()));
            RegisterResult registerResult = new RegisterResult(user.username(), authdata.authToken());
            ctx.result(new Gson().toJson(registerResult));
            ctx.status(200);

        }
        catch (AlreadyTakenException ex) {
            ctx.status(403);
        } catch (BadRequestException ex) {
            ctx.status(400);
        } catch (Exception ex) {
            ctx.status(500);
        }
    }

    private void login(Context ctx) throws DataAccessException {
        try { //ask TAs about this one
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);

            // works?
            if (isNullOrBlank(user.username()) || isNullOrBlank(user.password())) {
                throw new BadRequestException("Error: Username & password are required");
            }

            String givenPassword = user.password();
            user = userService.getUser(user.username());

            if (user == null) {
                throw new Exception("User not found");
            }

            if (!user.password().equals(givenPassword)) {
                throw new WrongPasswordException("Password incorrect");
            }

            AuthData authdata = authService.createAuth(new AuthData(UUID.randomUUID().toString(), user.username()));
            LoginResult loginResult = new LoginResult(user.username(), authdata.authToken());
            ctx.result(new Gson().toJson(loginResult));
            ctx.status(200);

        } catch (BadRequestException ex) {
            ctx.status(400);
        } catch (WrongPasswordException ex) {
            ctx.status(401);
        } catch (Exception ex) {
            ctx.status(500);
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
        } catch (Exception ex) {
            ctx.status(500);
        }
    }

    private void create(Context ctx) throws DataAccessException {
        try {
            GameData game = new Gson().fromJson(ctx.body(), GameData.class);
            String authToken = ctx.header("authorization");
            AuthData authData = authService.getAuth(authToken);

            if (game.gameName().isEmpty()) {
                throw new BadRequestException("Error: Need a game name");
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
        } catch (WrongPasswordException ex) {
            ctx.status(401);
        } catch (Exception ex) {
            ctx.status(500);
        }
    }

    private void join(Context ctx) throws DataAccessException {
        try {
            GameData game = new Gson().fromJson(ctx.body(), GameData.class);
            String authToken = ctx.header("authorization");
            AuthData authData = authService.getAuth(authToken);

            // figure out how to get playerColor to translate to current player's color or set whiteUsername to their
            // username if they're white and vice versa

            if (game.gameName().isEmpty()) {
                throw new BadRequestException("Error: Need a game name");
            }

            if (authData == null) {
                throw new WrongPasswordException("Error: unauthorized");
            }

            game = gameService.getGame(game.gameID());

            if ((game.whiteUsername() == null) && ) // AlreadyTakenException

        } catch (BadRequestException ex) {
            ctx.status(400);
        } catch (WrongPasswordException ex) {
            ctx.status(401);
        } catch (Exception ex) {
            ctx.status(500);
        }
    }

    public void clear(Context ctx) throws DataAccessException {
        authService.clearAllAuthTokens();
        gameService.clearAllGames();
        userService.clearAllUsers();
        ctx.status(200);
    }


}
