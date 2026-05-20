package server;

import com.google.gson.Gson;
import model.*;
import service.*;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import java.util.UUID;

public class Server {

    private final Javalin javalin;

    record RegisterResult(String username, String authToken) {}

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clear)
                .post("/user", this::register);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void register(Context ctx) throws DataAccessException {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData authdata = new AuthData(UUID.randomUUID().toString(), user.username());
        if (UserService.getUser(user.username()) == null) {
            user = UserService.createUser(user);
            authdata = AuthService.createAuth(authdata);
            RegisterResult registerResult = new RegisterResult(user.username(), authdata.authToken());
            ctx.result(new Gson().toJson(registerResult));
            ctx.status(200);
        }
        else {
            ctx.status(403);
        }
    }

    private void clear(Context ctx) throws DataAccessException {
        AuthService.clearAllAuthTokens();
        GameService.clearAllGames();
        UserService.clearAllUsers();
        ctx.status(200);
    }


}
