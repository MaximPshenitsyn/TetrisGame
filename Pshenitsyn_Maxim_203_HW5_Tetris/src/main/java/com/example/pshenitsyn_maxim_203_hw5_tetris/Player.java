package com.example.pshenitsyn_maxim_203_hw5_tetris;

import java.time.LocalDateTime;

/*
Class representing basic player
 */
public class Player {
    private Boolean connected;
    private String name;
    private LocalDateTime timeStarted;
    private LocalDateTime timeFinished;
    private Boolean finished;
    private Integer figureCount;

    public Player() {
        name = "none";
        connected = false;
        finished = false;
        figureCount = 0;
    }
    
    public Integer nextFigure() {
        return figureCount++;
    }

    public Boolean isConnected() {
        return connected;
    }

    public void connect() {
        connected = true;
    }

    public void disconnect() {
        connected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTurns() {
        return figureCount - 1;
    }

    public LocalDateTime getTimeStarted() {
        return timeStarted;
    }

    public void startTime() {
        timeStarted = LocalDateTime.now();
    }

    public void finishGame() {
        finished = true;
        timeFinished = LocalDateTime.now();
    }

    public Boolean finished() {
        return finished;
    }

    public long getSecondsSpent() {
        return java.time.Duration.between(timeStarted, timeFinished).getSeconds();
    }
}
