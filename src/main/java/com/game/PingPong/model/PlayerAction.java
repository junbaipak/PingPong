package com.game.PingPong.model;

public class PlayerAction {

    private String action;
    private String player;

    public PlayerAction() {
    }

    public PlayerAction(String action, String player) {
        this.action = action;
        this.player = player;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}
