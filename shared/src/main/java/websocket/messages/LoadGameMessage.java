package websocket.messages;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import java.util.*;

/**
 * game from server to client
 */
public class LoadGameMessage extends ServerMessage {

    GameData game;

    public LoadGameMessage(ServerMessageType type, GameData game) {
        super(type);
        this.game = game;
    }

    public GameData getGame() {return game;}


}
