package com.example.skaitournamentspring;

import com.example.skaitournamentspring.model.Player;
import com.example.skaitournamentspring.service.impl.BasketballGameService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BasketballGameServiceTest {

    @InjectMocks
    private BasketballGameService basketballGameService;

    public BasketballGameServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void processGame_ValidBasketballGame_ReturnsListOfPlayers() {
        List<String> lines = Arrays.asList("John;Doe;10;Team A;15;10;5", "Jane;Smith;7;Team B;12;8;3");

        List<Player> result = basketballGameService.processGame("basketball", lines);

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Doe", result.get(0).getNickname());
        assertEquals(55, result.get(0).getRatingPoints());
        assertEquals("Jane", result.get(1).getName());
        assertEquals("Smith", result.get(1).getNickname());
        assertEquals(35, result.get(1).getRatingPoints());
    }

    @Test
    public void processGame_InvalidSportType_ThrowsIllegalArgumentException() {

        List<String> lines = Arrays.asList("John;Doe;10;Team A;15", "Jane;Smith;7;Team B;12");
        assertThrows(IllegalArgumentException.class, () -> basketballGameService.processGame("football", lines));
    }

    @Test
    public void processGame_EmptyList_ReturnsEmptyList() {

        List<String> lines = List.of();
        List<Player> result = basketballGameService.processGame("basketball", lines);

        assertTrue(result.isEmpty());
    }

    @Test
    public void processGame_InvalidDataFormat_ThrowsIllegalArgumentException() {

        List<String> lines = List.of("InvalidData");
        assertThrows(IllegalArgumentException.class, () -> basketballGameService.processGame("basketball", lines));
    }

    @Test
    public void processGame_GameWithTies_ReturnsListOfPlayersWithTies() {
        List<String> lines = Arrays.asList("John;Doe;10;Team A;5;5;5", "Jane;Smith;10;Team A;5;5;5", "Alex;Johnson;5;Team B;10;10;10");

        List<Player> result = basketballGameService.processGame("basketball", lines);

        assertEquals(3, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Doe", result.get(0).getNickname());
        assertEquals(20, result.get(0).getRatingPoints());
        assertEquals("Jane", result.get(1).getName());
        assertEquals("Smith", result.get(1).getNickname());
        assertEquals(20, result.get(1).getRatingPoints());
        assertEquals("Alex", result.get(2).getName());
        assertEquals("Johnson", result.get(2).getNickname());
        assertEquals(40, result.get(2).getRatingPoints());
    }

    @Test
    public void processGame_GameWithNoWinners_ReturnsListOfPlayersWithoutAdditionalPoints() {
        List<String> lines = Arrays.asList("John;Doe;10;Team A;10;10;10", "Jane;Smith;7;Team B;10;10;10");

        List<Player> result = basketballGameService.processGame("basketball", lines);

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Doe", result.get(0).getNickname());
        assertEquals(40, result.get(0).getRatingPoints());
        assertEquals("Jane", result.get(1).getName());
        assertEquals("Smith", result.get(1).getNickname());
        assertEquals(40, result.get(1).getRatingPoints());
    }

}