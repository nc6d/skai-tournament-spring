package com.example.skaitournamentspring;

import com.example.skaitournamentspring.model.Player;
import com.example.skaitournamentspring.service.GameService;
import com.example.skaitournamentspring.service.impl.BasketballGameService;
import com.example.skaitournamentspring.service.impl.HandballGameService;
import com.example.skaitournamentspring.service.impl.MvpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MvpServiceImplTest {
    private MvpServiceImpl mvpService;

    @Mock
    private BasketballGameService basketballGameService;

    @Mock
    private HandballGameService handballGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        List<GameService> gameServices = Arrays.asList(basketballGameService, handballGameService);
        mvpService = new MvpServiceImpl(gameServices);
    }

    @Test
    void testProcessGames_NoGameData() {
        Map<List<String>, List<String>> gameData = Collections.emptyMap();

        assertDoesNotThrow(() -> mvpService.processGames(gameData));
        assertNull(mvpService.getMvp());

        verifyNoInteractions(basketballGameService, handballGameService);
    }

    @Test
    void testProcessGames_BasketballGameData() {
        String basketballFile = "basketball_match.csv";
        List<String> basketballLines = Arrays.asList(
                "player 1;nick1;4;Team A;10;2;7",
                "player 2;nick2;8;Team A;0;10;0"
        );
        Map<List<String>, List<String>> gameData = Collections.singletonMap(List.of(basketballFile, "basketball"), basketballLines);

        Player player1 = new Player("player 1", "nick1", 4, "Team A", 0);
        player1.addRatingPoints(37);
        Player player2 = new Player("player 2", "nick2", 8, "Team A", 0);
        player2.addRatingPoints(10);
        List<Player> players = Arrays.asList(player1, player2);

        when(basketballGameService.processGame(eq("basketball"), eq(basketballLines))).thenReturn(players);

        assertDoesNotThrow(() -> mvpService.processGames(gameData));

        verify(basketballGameService).processGame(eq("basketball"), eq(basketballLines));
        verify(handballGameService, never()).processGame(anyString(), anyList());

        Player mvp = mvpService.getMvp();
        assertNotNull(mvp);
        assertEquals(player1, mvp);
    }

    @Test
    void testProcessGames_HandballGameData() {
        String handballFile = "handball_match.csv";
        List<String> handballLines = Arrays.asList(
                "player 1;nick1;4;Team A;0;20",
                "player 2;nick2;8;Team A;15;20"
        );
        Map<List<String>, List<String>> gameData = Collections.singletonMap(List.of(handballFile, "handball"), handballLines);

        Player player1 = new Player("player 1", "nick1", 4, "Team A", 0);
        player1.addRatingPoints(-20);
        Player player2 = new Player("player 2", "nick2", 8, "Team A", 0);
        player2.addRatingPoints(25);
        List<Player> players = Arrays.asList(player1, player2);

        when(handballGameService.processGame(eq("handball"), eq(handballLines))).thenReturn(players);

        assertDoesNotThrow(() -> mvpService.processGames(gameData));

        verify(handballGameService).processGame(eq("handball"), eq(handballLines));
        verify(basketballGameService, never()).processGame(anyString(), anyList());

        Player mvp = mvpService.getMvp();
        assertNotNull(mvp);
        assertEquals(player2, mvp);
    }

    @Test
    void testProcessGames_InvalidSportType() {
        List<String> meta = List.of("football_match.csv", "football");
        List<String> lines = Collections.singletonList("player;nick;1;Team A;0;0");
        Map<List<String>, List<String>> gameData = Collections.singletonMap(meta, lines);

        assertThrows(IllegalArgumentException.class, () -> mvpService.processGames(gameData));

        verifyNoInteractions(basketballGameService, handballGameService);
    }

    @Test
    void testGetMvp_NoPlayers() {
        assertNull(mvpService.getMvp());

        verifyNoInteractions(basketballGameService, handballGameService);
    }

}