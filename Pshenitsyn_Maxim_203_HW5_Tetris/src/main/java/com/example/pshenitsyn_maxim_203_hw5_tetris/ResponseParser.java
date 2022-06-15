package com.example.pshenitsyn_maxim_203_hw5_tetris;

public class ResponseParser {
    private static final String separator = ";";

    private final ResponseTypes responseType;
    private final String figure;
    private final String message;
    private final String opponentName;

    public ResponseParser(ResponseTypes responseType, String figure, String message, String opponentName) {
        this.responseType = responseType;
        this.figure = figure;
        this.message = message;
        this.opponentName = opponentName;
    }

    public ResponseTypes getResponseType() {
        return responseType;
    }

    public String getFigure() {
        return figure;
    }

    public String getMessage() {
        return message;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public enum ResponseTypes {
        REFUSED,
        GAME_STARTED,
        GAME_FINISHED,
        NEW_FIGURE,
        STAY,
        INFO
    }

    public static ResponseParser parseResponse(String response) throws Exception {
        String[] data = response.split(separator);
        return switch (data[0]) {
            case "REFUSED" -> new ResponseParser(ResponseTypes.REFUSED, null, data[2], data[3]);
            case "GAME_STARTED" -> new ResponseParser(ResponseTypes.GAME_STARTED, data[1], data[2], data[3]);
            case "GAME_FINISHED" -> new ResponseParser(ResponseTypes.GAME_FINISHED, null, data[2], data[3]);
            case "NEW_FIGURE" -> new ResponseParser(ResponseTypes.NEW_FIGURE, data[1], data[2], data[3]);
            case "STAY" -> new ResponseParser(ResponseTypes.STAY, null, data[2], data[3]);
            case "INFO" -> new ResponseParser(ResponseTypes.INFO, null, data[2], data[3]);
            default -> throw new Exception("Unable to parse response");
        };
    }

    public static String convertToResponseMessage(ResponseTypes responseType, String figure, String message,
                                                  String opponentName) {
        return responseType.name() + separator + figure + separator + message + separator + opponentName;
    }
}
