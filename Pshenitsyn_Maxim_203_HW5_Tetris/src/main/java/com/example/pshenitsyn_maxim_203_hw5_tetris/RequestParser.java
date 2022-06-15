package com.example.pshenitsyn_maxim_203_hw5_tetris;

import java.time.LocalDateTime;

public class RequestParser {
    private static final String separator = ";";

    public enum RequestTypes {
        CONNECT_USER,
        STAY,
        MAKE_TURN,
        DISCONNECT_USER,
        FINISH_GAME
    }

    private final RequestTypes requestType;
    private final String name;

    public RequestParser(RequestTypes requestType, String name) {
        this.requestType = requestType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public RequestTypes getRequestType() {
        return requestType;
    }

    public static RequestParser parseRequest(String request) throws Exception {
        String[] data = request.split(separator);
        return switch (data[0]) {
            case "CONNECT_USER" -> new RequestParser(RequestTypes.CONNECT_USER, data[1]);
            case "STAY" -> new RequestParser(RequestTypes.STAY, data[1]);
            case "MAKE_TURN" -> new RequestParser(RequestTypes.MAKE_TURN, data[1]);
            case "DISCONNECT_USER" -> new RequestParser(RequestTypes.DISCONNECT_USER, data[1]);
            case "FINISH_GAME" -> new RequestParser(RequestTypes.FINISH_GAME, data[1]);
            default -> throw new Exception("Unable to parse request");
        };
    }

    public static String convertToRequestMessage(RequestTypes requestType, String name) {
        return requestType.name() + separator + name;
    }
}
