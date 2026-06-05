package client;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import requests.*;
import results.CreateResult;
import results.ListResult;
import results.LoginResult;
import results.RegisterResult;
import server.Server;
import server.ServerFacade;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void clear() throws Exception {
        ClearRequest clrRequest = new ClearRequest();
        facade.clear(clrRequest);
    }

    @AfterAll
    static void stopServer() throws Exception {
        ClearRequest clrRequest = new ClearRequest();
        facade.clear(clrRequest);
        server.stop();
    }


    @Test
    void registerTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        RegisterResult regResult = facade.register(regRequest);
        assertTrue(regResult.authToken().length() > 10);
    }

    @Test
    void registerBadRequestTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("", "password", "p1@email.com");
        assertThrows(Exception.class, () -> facade.register(regRequest));
    }

    @Test
    void loginTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        facade.register(regRequest);

        LoginRequest loginRequest = new LoginRequest("player1", "password");
        LoginResult loginResult = facade.login(loginRequest);
        assertTrue(loginResult.authToken().length() > 10);
    }

    @Test
    void loginUnauthorizedTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        facade.register(regRequest);

        LoginRequest loginRequest = new LoginRequest("player1", "passwork");
        assertThrows(Exception.class, () -> facade.login(loginRequest));
    }

    @Test
    void createGameTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        String authToken = facade.register(regRequest).authToken();
        CreateRequest createRequest = new CreateRequest("gametime!", authToken);

        CreateResult createResult = facade.create(createRequest);
        assertEquals(1, createResult.gameID());
    }

    @Test
    void createGameUnauthorizedTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        facade.register(regRequest);
        CreateRequest createRequest = new CreateRequest("gametime!", "pee pee poo poo");

        assertThrows(Exception.class, () -> facade.create(createRequest));
    }

    @Test
    void joinGameTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        String authToken = facade.register(regRequest).authToken();
        CreateRequest createRequest = new CreateRequest("gametime!", authToken);
        int id = facade.create(createRequest).gameID();
        JoinRequest joinRequest = new JoinRequest("WHITE", id, authToken);
        facade.join(joinRequest);

        ListRequest listRequest = new ListRequest(authToken);
        ListResult listResult = facade.list(listRequest);
        GameData gameData = listResult.games().getFirst();

        assertEquals("player1", gameData.whiteUsername());
    }

    @Test
    void joinGameAlreadyTakenTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        String authToken = facade.register(regRequest).authToken();

        RegisterRequest regRequest2 = new RegisterRequest("player2", "password2", "p2@email.com");
        String authToken2 = facade.register(regRequest2).authToken();

        CreateRequest createRequest = new CreateRequest("gametime!", authToken);
        int id = facade.create(createRequest).gameID();
        JoinRequest joinRequest = new JoinRequest("white", id, authToken);
        facade.join(joinRequest);
        JoinRequest joinRequest2 = new JoinRequest("white", id, authToken2);

        assertThrows(Exception.class, () -> facade.join(joinRequest2));
    }

    @Test
    void joinOldGameTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        String authToken = facade.register(regRequest).authToken();


        CreateRequest createRequest = new CreateRequest("gametime!", authToken);
        int id = facade.create(createRequest).gameID();

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        facade.logout(logoutRequest);

        LoginRequest loginRequest = new LoginRequest("player1", "password");
        LoginResult loginResult = facade.login(loginRequest);
        String newAuthtoken = loginResult.authToken();

        JoinRequest joinRequest = new JoinRequest("white", id, newAuthtoken);
        assertDoesNotThrow(() -> facade.join(joinRequest));

    }

    @Test
    void logoutTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        String authToken = facade.register(regRequest).authToken();

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        facade.logout(logoutRequest);
        ListRequest listRequest = new ListRequest(authToken);
        assertThrows(Exception.class, () -> facade.list(listRequest));
    }

    @Test // this one is so annoying
    void logoutUnsuccessfulTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        String authToken = facade.register(regRequest).authToken();

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        facade.logout(logoutRequest);

        ListRequest listRequest = new ListRequest(authToken);
        assertThrows(Exception.class, () -> facade.list(listRequest));
    }

    @Test
    void listGamesTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        String authToken = facade.register(regRequest).authToken();
        CreateRequest createRequest = new CreateRequest("gametime!", authToken);
        facade.create(createRequest);
        CreateRequest createRequest2 = new CreateRequest("randomExtra!", authToken);
        facade.create(createRequest2);
        ListRequest listRequest = new ListRequest(authToken);
        ArrayList<GameData> listyBoy = facade.list(listRequest).games();
        assertEquals("gametime!", listyBoy.getFirst().gameName());
        assertEquals("randomExtra!", listyBoy.get(1).gameName());
    }

    @Test
    void listGamesUnauthorizedTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("player1", "password", "p1@email.com");
        String authToken = facade.register(regRequest).authToken();
        CreateRequest createRequest = new CreateRequest("gametime!", authToken);
        facade.create(createRequest);
        CreateRequest createRequest2 = new CreateRequest("randomExtra!", authToken);
        facade.create(createRequest2);
        ListRequest listRequest = new ListRequest("yo mama");
        assertThrows(Exception.class, () -> facade.list(listRequest));
    }

    @Test
    void clearTest() throws Exception {
        RegisterRequest regRequest1 = new RegisterRequest("player1", "password", "p1@email.com");
        facade.register(regRequest1);
        RegisterRequest regRequest2 = new RegisterRequest("player2", "password2", "p2@email.com");
        facade.register(regRequest2);
        facade.clear(new ClearRequest());

        LoginRequest loginRequest = new LoginRequest("player1", "password");

        assertThrows(Exception.class, () -> facade.login(loginRequest));
    }

}
