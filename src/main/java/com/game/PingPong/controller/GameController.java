package com.game.PingPong.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.game.PingPong.model.GameState;
import com.game.PingPong.model.PlayerAction;

import jakarta.annotation.PostConstruct;

@Controller
public class GameController {

    private final SimpMessagingTemplate messagingTemplate;
    private GameState gameState;
    private ScheduledExecutorService gameLoop;

    public GameController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.gameState = new GameState();
    }

    @PostConstruct
    public void startGameLoop() {
        gameLoop = Executors.newSingleThreadScheduledExecutor();
        gameLoop.scheduleAtFixedRate(() -> {
            gameState.update();
            messagingTemplate.convertAndSend("/topic/gameState", gameState);
        }, 0, 16, TimeUnit.MILLISECONDS); // ~60 FPS
    }

    @MessageMapping("/playerAction")
    public void handlePlayerAction(PlayerAction action) {
        switch (action.getPlayer()) {
            case "LEFT":
                if ("UP".equals(action.getAction())) {
                    gameState.moveLeftPaddleUp();
                } else if ("DOWN".equals(action.getAction())) {
                    gameState.moveLeftPaddleDown();
                }
                break;
            case "RIGHT":
                if ("UP".equals(action.getAction())) {
                    gameState.moveRightPaddleUp();
                } else if ("DOWN".equals(action.getAction())) {
                    gameState.moveRightPaddleDown();
                }
                break;
        }
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
