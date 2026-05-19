package server;

import model.*;
import service.*;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clear);


    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void clear(Context ctx) throws DataAccessException {
        AuthService.clearAllAuthTokens();
        GameService.clearAllGames();
        UserService.clearAllUsers();
        ctx.status(200);
    }

}
