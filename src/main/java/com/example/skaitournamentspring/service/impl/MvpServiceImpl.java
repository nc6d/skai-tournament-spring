package com.example.skaitournamentspring.service.impl;

import com.example.skaitournamentspring.model.Player;
import com.example.skaitournamentspring.service.GameService;
import com.example.skaitournamentspring.service.MvpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class MvpServiceImpl implements MvpService {
    private final List<GameService> gameServices;
    public Map<String, Player> playerMap = new HashMap<>();

    @Override
    public void processGames(Map<List<String>, List<String>> gameData) {
        playerMap.clear();
        gameData.forEach(this::processGame);
    }

    private void processGame(List<String> meta, List<String> lines) {
        log.info(meta.get(0));
        GameService gameService = gameServices.stream()
                .filter(service -> service.getClass().getSimpleName().equalsIgnoreCase(meta.get(1) + "GameService"))
                .findAny().orElseThrow(() -> new IllegalArgumentException("Invalid sport type"));

        List<Player> players = gameService.processGame(meta.get(1), lines);
        for (Player player : players) {
            if (playerMap.containsKey(player.getName())) {
                playerMap.get(player.getName()).addRatingPoints(player.getRatingPoints());
            } else {
                playerMap.put(player.getName(), player);
            }
        }
        playerMap.forEach((k, v) -> log.info("{} : {}", k, v.getRatingPoints()));
    }

    @Override
    public Player getMvp() {
        return playerMap.values().stream()
                .max(Comparator.comparingInt(Player::getRatingPoints))
                .orElse(null);
    }
}
