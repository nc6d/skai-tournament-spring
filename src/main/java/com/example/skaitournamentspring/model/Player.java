package com.example.skaitournamentspring.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Player {
    private String name;
    private String nickname;
    private int number;
    private String teamName;
    private int ratingPoints;

    public void addRatingPoints(int points) {
        ratingPoints += points;
    }

}