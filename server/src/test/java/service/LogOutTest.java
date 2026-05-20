package service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import server.*;
import service.*;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;

class LogOutTest {

    static private Server server;

    @BeforeAll
    static void setupServer() {
        server = new Server();
        server.run(8080);

    }
    
    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() {
       // assertDoesNotThrow(() -> server.clear());
    }
}