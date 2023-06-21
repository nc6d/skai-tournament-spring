package com.example.skaitournamentspring.service.impl;

import com.example.skaitournamentspring.model.Player;
import com.example.skaitournamentspring.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketballGameService implements GameService {

    @Override
    public List<Player> processGame(String sport, List<String> lines) {
        if (!sport.equalsIgnoreCase("basketball"))
            throw new IllegalArgumentException("Invalid sport type");

        List<Player> players = processPlayerStats(lines);
        String winningTeam = getWinnerTeam(players);
        log.info("Winner team - {}", winningTeam);
        if (!winningTeam.equals("DRAW"))
            addPointsByWin(players, winningTeam);

        return players;
    }

    private int calculateScore(String[] splitData) {
        int scoredPoints = Integer.parseInt(splitData[4]);
        int rebounds = Integer.parseInt(splitData[5]);
        int assists = Integer.parseInt(splitData[6]);
        return scoredPoints * 2 + rebounds + assists;
    }

    private List<Player> processPlayerStats(List<String> lines) {
        return lines.stream().map(line -> {
            String[] data = line.split(";");
            if (data.length != 7) throw new IllegalArgumentException();
            Player player = buildPlayer(data);
            player.addRatingPoints(calculateScore(data));
            return player;
        }).collect(Collectors.toList());
    }

    private void addPointsByWin(List<Player> players, String winnerTeamName) {
        players.stream()
                .filter(player -> player.getTeamName().equals(winnerTeamName))
                .forEach(player -> player.addRatingPoints(10));
    }
}
