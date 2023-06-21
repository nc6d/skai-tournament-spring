package com.example.skaitournamentspring.service;

import com.example.skaitournamentspring.model.Player;

import java.util.List;
import java.util.Map;

public interface MvpService {
    void processGames(Map<List<String>, List<String>> gameData);
    Player getMvp();

}
