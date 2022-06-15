package com.example.pshenitsyn_maxim_203_hw5_tetris;

import engine.Engine;
import engine.Figure;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class HelloController {
    private Engine engine;
    protected Figure figure;
    private int turns = 0;
    private LocalDateTime start;

    @FXML
    public Label opponentName;

    @FXML
    public Button closeButton;

    @FXML
    public GridPane field;

    @FXML
    public Button connectButton;

    @FXML
    public Button disconnectButton;

    @FXML
    public TextField portInput;

    @FXML
    private Label results;

    @FXML
    public GridPane figureSource;

    @FXML
    public TextField nameInput;

    /**
     * Show current mood
     */
    private void updateResults() {
        results.setText("Turn: " + turns + ";  Time: " +
                java.time.Duration.between(start, LocalDateTime.now()).toSeconds());
    }

    /**
     * Checks whether name is incorrect
     * @param name - provided name
     * @return - true if name is incorrect and false otherwise
     */
    public Boolean incorrectName(String name) {
        return name.contains(";") || name.length() == 0 || name.length() >= 64;
    }

    /**
     * Draws current figure
     */
    private void generateFigure() {
        figureSource.getChildren().clear();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                Canvas canvas = new Canvas(figureSource.getCellBounds(i, j).getWidth() - 1,
                        figureSource.getCellBounds(i, j).getHeight() - 1);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setFill(Color.GREEN);
                if (figure.getXY(i, j)) {
                    // System.out.println(i + " " + j);
                    gc.fillRect(0, 0, canvas.getHeight(), canvas.getWidth());
                    canvas.setOnDragDetected(this::onFigureDragDetected);
                }
                figureSource.add(canvas, j, i);
            }
        }
    }

    /**
     * Calling alert with target message
     * @param message - message to be shown
     */
    private void printMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Information");
        alert.showAndWait();
    }

    /**
     * Server connection logics
     * @param request - request text
     * @return - ResponseParser instance of a response
     * @throws Exception
     */
    private ResponseParser sendRequest(String request) throws Exception {
        int port;
        try {
            port = Integer.parseInt(portInput.getText());
        } catch (Exception ex) {
            throw new Exception("Port should be integer!");
        }
        Socket clientSocket = new Socket("localhost", port);
        PrintStream requestStream = new PrintStream(clientSocket.getOutputStream());
        requestStream.println(request);
        BufferedReader responseStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        return ResponseParser.parseResponse(responseStream.readLine());
    }

    /**
     * Connecting to server
     */
    @FXML
    protected void onConnectButtonClick() {
        if (incorrectName(nameInput.getText())) {
            printMessage("Name should be 1 - 63 characters long and can contain symbol ';'");
            return;
        }
        if (connectButton.getText().equals("Start")) {
            try {
                ResponseParser r = sendRequest(RequestParser.convertToRequestMessage(RequestParser.RequestTypes.STAY,
                        nameInput.getText()));
                if (r.getResponseType() == ResponseParser.ResponseTypes.GAME_STARTED) {
                    printMessage(r.getMessage());
                    figure = Figure.convertFromString(r.getFigure());
                    connectButton.setDisable(true);
                    opponentName.setText("Opponent: " + r.getOpponentName());
                    startGame();
                } else {
                    printMessage(r.getMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                printMessage(ex.getMessage());
            }
            return;
        }
        nameInput.setDisable(true);
        try {
            ResponseParser r = sendRequest(RequestParser.convertToRequestMessage(RequestParser.RequestTypes.CONNECT_USER,
                                                                                 nameInput.getText()));
            if (r.getResponseType() == ResponseParser.ResponseTypes.REFUSED) {
                printMessage(r.getMessage());
                nameInput.setDisable(false);
                return;
            } else if (r.getResponseType() == ResponseParser.ResponseTypes.INFO) {
                printMessage(r.getMessage());
                connectButton.setText("Start");
                disconnectButton.setDisable(false);
                return;
            } else if (r.getResponseType() == ResponseParser.ResponseTypes.GAME_STARTED) {
                printMessage(r.getMessage());
                figure = Figure.convertFromString(r.getFigure());
                opponentName.setText(r.getOpponentName());
                startGame();
            }
        } catch (Exception ex) {
            nameInput.setDisable(false);
            printMessage(ex.getMessage() + " server is not available!");
            return;
        }
        connectButton.setDisable(true);
        disconnectButton.setDisable(false);
        portInput.setDisable(true);
    }

    /**
     * Settlements to start the game
     */
    private void startGame() {
        engine = new Engine();
        generateFigure();
        field.getChildren().clear();
        turns = 0;
        start = LocalDateTime.now();
        closeButton.setDisable(false);
        updateResults();

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                Canvas canvas = new Canvas(field.getCellBounds(i, j).getWidth() - 1,
                        field.getCellBounds(i, j).getHeight() - 1);
                canvas.setOnDragOver(this::onFigureDragOver);
                canvas.setOnDragDropped(this::onDragDropped);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.WHITE);
                } else {
                    gc.setFill(Color.color(1.0, 0.98, 1.0));
                }
                gc.fillRect(0, 0, canvas.getHeight(), canvas.getWidth());
                field.add(canvas, i, j);
            }
        }
    }

    /**
     * Drag and drop started
     * @param event - mouse event
     */
    public void onFigureDragDetected(MouseEvent event) {
        Dragboard db = figureSource.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        Node call = (Node)event.getTarget();
        content.putString(GridPane.getRowIndex(call) + " " + GridPane.getColumnIndex(call));
        db.setContent(content);
        event.consume();
    }

    /**
     * Drag and drop process
     * @param event - drag event
     */
    public void onFigureDragOver(DragEvent event) {
        if (event.getGestureSource() != field &&
                event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }

    /**
     * Drag and drop dropping
     * @param event - drag event
     */
    public void onDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            Node call = (Node)event.getTarget();
            int x1 = GridPane.getRowIndex(call);
            int y1 = GridPane.getColumnIndex(call);
            int x = Integer.parseInt(db.getString().split(" ")[0]);
            int y = Integer.parseInt(db.getString().split(" ")[1]);
            if (engine.trySettleFigure(figure, x1 - x, y1 - y)) {
                fillField();
                turns++;
                updateResults();
                try {
                    ResponseParser r = sendRequest(RequestParser.convertToRequestMessage(
                            RequestParser.RequestTypes.MAKE_TURN, nameInput.getText()));
                    if (r.getResponseType() == ResponseParser.ResponseTypes.GAME_FINISHED) {
                        printMessage(r.getMessage());
                        gameFinish();
                    } else if (r.getResponseType() == ResponseParser.ResponseTypes.NEW_FIGURE) {
                        figure = Figure.convertFromString(r.getFigure());
                        generateFigure();
                    }
                } catch (Exception ex) {
                    printMessage(ex.getMessage() + " server is not available! Game finishes.");
                    Platform.exit();
                    printMessage(ex.getMessage());
                }
            }
        }
        event.consume();
    }

    /**
     * Arranging the output of field
     */
    private void fillField() {
        for (Node n : field.getChildren()) {
            if (engine.getField()[GridPane.getRowIndex(n)][GridPane.getColumnIndex(n)]) {
                GraphicsContext gc = ((Canvas)n).getGraphicsContext2D();
                gc.setFill(Color.GREEN);
                gc.fillRect(0, 0, ((Canvas)n).getHeight(), ((Canvas)n).getWidth());
            }
        }
    }

    /**
     * Disconnecting user
     */
    @FXML
    public void gameDisconnect() {
        try {
            ResponseParser r = sendRequest(RequestParser.convertToRequestMessage(
                    RequestParser.RequestTypes.DISCONNECT_USER, nameInput.getText()));
            printMessage(r.getMessage());
        } catch (Exception ex) {
            printMessage(ex.getMessage() + " server is not available! Game finishes.");
            Platform.exit();
        }
        Platform.exit();
    }

    /**
     * Finishing the game
     */
    @FXML
    public void gameFinish() {
        if (engine == null) {
            Platform.exit();
            return;
        }
        RequestParser.RequestTypes type = RequestParser.RequestTypes.FINISH_GAME;
        if (closeButton.getText().equals("Refresh results")) {
            type = RequestParser.RequestTypes.STAY;
        }
        try {
            ResponseParser r = sendRequest(RequestParser.convertToRequestMessage(type, nameInput.getText()));
            printMessage(r.getMessage());
            if (r.getResponseType() == ResponseParser.ResponseTypes.INFO) {
                closeButton.setText("Refresh results");
                return;
            }
        } catch (Exception ex) {
            printMessage(ex.getMessage() + " server is not available! Game finishes.");
            Platform.exit();
            printMessage(ex.getMessage());
        }
        updateResults();
        Platform.exit();
    }
}
