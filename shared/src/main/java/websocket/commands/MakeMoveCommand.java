package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a move command
 */
public class MakeMoveCommand extends UserGameCommand {
    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        super(commandType, authToken, gameID);
    }
}
