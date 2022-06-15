package com.example.pshenitsyn_maxim_203_hw5_tetris;

import engine.Engine;
import engine.Figure;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Server main class
 */
public class Server {
    public static Player p1, p2;
    private static List<Figure> figures;

    /**
     * Get figure from figure list
     * @param index - index of next figure
     * @return next figure
     */
    public static Figure getFigureByIndex(int index) {
        return figures.get(index);
    }


    /**
     * Fills figures list with instances
     */
    private static void generateFigures() {
        int count = 84;
        figures = new ArrayList<>();
        while (count-- > 0) {
            figures.add(Engine.generateFigure());
        }
    }

    public static void main(String[] args) {
        generateFigures();
        try {
            int serverPort = 5000;
            p1 = new Player();
            p2 = new Player();
            if (args.length > 0) {
                serverPort = Integer.parseInt(args[0]);  // int serverPort = 5000;
            }
            ServerSocket gameServer = new ServerSocket(serverPort);
            System.out.println("Server started");
            while (true) {
                Socket socket = gameServer.accept();
                Thread thread = new GameThread(socket);
                thread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

/**
 * Thread class of a socket connection
 */
class GameThread extends Thread {
    /**
     * Get player by his name
     * @param name - name of player
     * @return ref to target player
     * @throws IllegalArgumentException
     */
    private static Player getPlayerByName(String name) throws IllegalArgumentException {
        if (Server.p1.isConnected() && Server.p1.getName().equals(name)) {
            return Server.p1;
        } else if (Server.p2.isConnected() && Server.p2.getName().equals(name)) {
            return Server.p2;
        }
        throw new IllegalArgumentException("There is no player with name '" + name + "' connected to the server!");
    }

    /**
     * Connecting user from request
     * @param r - request data
     * @return true if connected, otherwise false
     * @throws IllegalArgumentException
     */
    private static Boolean connectUser(RequestParser r) throws IllegalArgumentException {
        Player p1 = Server.p1;
        Player p2 = Server.p2;
        if (p1.isConnected() && p2.isConnected()) {
            throw new IllegalArgumentException("Maximum number of players (" + 2 + ") already connected!");
        } else if (!p1.isConnected()) {
            if (p2.isConnected() && p2.getName().equals(r.getName())) {
                throw new IllegalArgumentException("There is already user connected with name '" + r.getName() +
                        "'");
            }
            p1.setName(r.getName());
            p1.connect();
            return true;
        } else {
            if (p1.isConnected() && p2.getName().equals(r.getName())) {
                throw new IllegalArgumentException("There is already user connected with name '" + r.getName() +
                        "'");
            }
            p2.setName(r.getName());
            p2.connect();
            return true;
        }
    }

    /**
     * Provides the result of game when finished
     * @param current - current player
     * @param other - opponent
     * @return - response text
     */
    private static String finishGameResult(Player current, Player other) {
        if (other.getTurns() > current.getTurns()) {
            return "You lost the game! Your opponent made " + other.getTurns() + " turns, while you made " +
                    current.getTurns() + ".";
        } else if (other.getTurns().equals(current.getTurns())) {
            if (other.getTimeStarted().isBefore(current.getTimeStarted())) {
                return "You lost the game! You both made " + other.getTurns() + " turns but your opponent spent " +
                        other.getSecondsSpent() / 60 + " minutes and " + current.getSecondsSpent() % 60 +
                        " seconds, while you spent " + current.getSecondsSpent() / 60 + " minutes and " +
                        current.getSecondsSpent() % 60 + " seconds.";
            } else {
                return "You won the game! You both made " + other.getTurns() + " turns but your opponent spent " +
                        current.getSecondsSpent() / 60 + " minutes and " + current.getSecondsSpent() % 60 +
                        " seconds, while you spent " + current.getSecondsSpent() / 60 + " minutes and " +
                        current.getSecondsSpent() % 60 + " seconds.";
            }
        } else {
            return "You won the game! Your opponent made " + other.getTurns() + " turns, while you made " +
                    current.getTurns() + ".";
        }
    }

    /**
     * Get opponent for target player
     * @param p - current player
     * @return - ref to opponent
     */
    private static Player getOtherPlayer(Player p) {
        if (p.equals(Server.p1)) {
            return Server.p2;
        }
        return Server.p1;
    }

    /**
     * Provides data for user who has finished the game
     * @param r - request data
     * @return - response text
     * @throws IllegalArgumentException
     */
    private static String finishGameForUser(RequestParser r) throws IllegalArgumentException {
        Player p = getPlayerByName(r.getName());
        Player otherPlayer;
        if (p.equals(Server.p1)) {
            otherPlayer = Server.p2;
        } else {
            otherPlayer = Server.p1;
        }
        p.finishGame();
        if (!otherPlayer.isConnected()) {
            return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.GAME_FINISHED, "null",
                    "Your opponent has left the game, you are winner!", otherPlayer.getName());
        }
        if (otherPlayer.finished()) {
            return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.GAME_FINISHED, "null",
                                                           finishGameResult(p, otherPlayer), otherPlayer.getName());
        }
        return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.INFO, "null",
                                               "Waiting for other player...", otherPlayer.getName());
    }

    /**
     * Building response for target request
     * @param request - provided request
     * @return - response text
     * @throws IllegalArgumentException
     */
    public static String getResponse(String request) throws IllegalArgumentException {
        RequestParser r;
        try {
             r = RequestParser.parseRequest(request);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        if (r.getRequestType() == RequestParser.RequestTypes.CONNECT_USER) {
            try {
                Boolean result = connectUser(r);
                if (result && Server.p1.isConnected() && Server.p2.isConnected()) {
                    getPlayerByName(r.getName()).startTime();
                    return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.GAME_STARTED,
                            Server.getFigureByIndex(getPlayerByName(r.getName()).nextFigure()).toString(),
                                                            "Game started!",
                                                                getOtherPlayer(getPlayerByName(r.getName())).getName());
                }
                return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.INFO, "null",
                                                       "Waiting for other player",
                                                                getOtherPlayer(getPlayerByName(r.getName())).getName());
            } catch (IllegalArgumentException ex) {
                return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.REFUSED, "null",
                                                               ex.getMessage(),
                                                                getOtherPlayer(getPlayerByName(r.getName())).getName());
            }
        } else if (r.getRequestType() == RequestParser.RequestTypes.FINISH_GAME) {
            return finishGameForUser(r);
        } else if (r.getRequestType() == RequestParser.RequestTypes.STAY) {
            Player p = getPlayerByName(r.getName());
            if (p.finished()) {
                if (Server.p1.finished() && Server.p2.finished()) {
                    return finishGameForUser(r);
                } else {
                    return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.INFO, "null",
                                                                    "Waiting for other player...",
                                                                getOtherPlayer(getPlayerByName(r.getName())).getName());
                }
            }
            if (p.getTimeStarted() == null && Server.p1.isConnected() && Server.p2.isConnected()) {
                p.startTime();
                return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.GAME_STARTED,
                                                               Server.getFigureByIndex(p.nextFigure()).toString(),
                                                       "Game started!",
                                                                getOtherPlayer(getPlayerByName(r.getName())).getName());
            }
            return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.STAY, "null",
                                                    "Waiting for other player",
                                                            getOtherPlayer(getPlayerByName(r.getName())).getName());
        }  else if (r.getRequestType() == RequestParser.RequestTypes.MAKE_TURN) {
            Player p = getPlayerByName(r.getName());
            if (!Server.p2.isConnected() || !Server.p1.isConnected() || Server.p1.finished() || Server.p2.finished()) {
                return finishGameForUser(r);
            }
            return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.NEW_FIGURE,
                    Server.getFigureByIndex(p.nextFigure()).toString(), "figure sent",
                    getOtherPlayer(getPlayerByName(r.getName())).getName());
        } else if (r.getRequestType() == RequestParser.RequestTypes.DISCONNECT_USER) {
            Player p = getPlayerByName(r.getName());
            p.disconnect();
            return ResponseParser.convertToResponseMessage(ResponseParser.ResponseTypes.INFO, "null",
                    "You was disconnected and lost the game!",
                    getOtherPlayer(getPlayerByName(r.getName())).getName());
        }
        return null;
    }

    final Socket clientSocket;

    GameThread(Socket s) {
        clientSocket = s;
    }

    public void run () {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String request = br.readLine();

            // logics here
            System.out.println(request);

            PrintStream ps = new PrintStream(clientSocket.getOutputStream());
            ps.println(getResponse(request));
            ps.flush();
            clientSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
