package com.example.skaitournamentspring.service;

import com.example.skaitournamentspring.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GameService {

    List<Player> processGame(String sport, List<String> lines);

    default Player buildPlayer(String[] splitData) {
        return Player.builder()
                .name(splitData[0])
                .nickname(splitData[1])
                .number(Integer.parseInt(splitData[2]))
                .teamName(splitData[3])
                .build();
    }

default String getWinnerTeam(List<Player> players) {
    Map<String, Integer> teamPoints = new HashMap<>();

    for (Player player : players) {
        String teamName = player.getTeamName();
        int ratingPoints = player.getRatingPoints();

        teamPoints.put(teamName, teamPoints.getOrDefault(teamName, 0) + ratingPoints);
    }

    List<String> winningTeams = new ArrayList<>();
    int maxPoints = 0;

    for (Map.Entry<String, Integer> entry : teamPoints.entrySet()) {
        String teamName = entry.getKey();
        int points = entry.getValue();

        if (points > maxPoints) {
            winningTeams.clear();
            winningTeams.add(teamName);
            maxPoints = points;
        } else if (points == maxPoints) {
            winningTeams.add(teamName);
        }
    }

    if (winningTeams.size() == 1) {
        return winningTeams.get(0);
    } else {
        return "DRAW";
    }
}
}
