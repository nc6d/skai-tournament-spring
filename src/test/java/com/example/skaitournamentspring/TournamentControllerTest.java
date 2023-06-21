package com.example.skaitournamentspring;

import com.example.skaitournamentspring.controller.TournamentController;
import com.example.skaitournamentspring.model.Player;
import com.example.skaitournamentspring.service.MvpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

public class TournamentControllerTest {

    @Mock
    private MvpService mvpService;

    @InjectMocks
    private TournamentController tournamentController;

    @Captor
    private ArgumentCaptor<Map<List<String>, List<String>>> gameDataCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void calculateMvp_ValidFiles_ReturnsOkResponse() throws IOException {
        MultipartFile[] files = new MultipartFile[2];
        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);

        InputStream file1InputStream = new ByteArrayInputStream("John;Doe;10;Team A;15".getBytes());
        InputStream file2InputStream = new ByteArrayInputStream("Jane;Smith;7;Team B;12".getBytes());

        when(file1.getOriginalFilename()).thenReturn("file1.csv");
        when(file1.getInputStream()).thenReturn(file1InputStream);

        when(file2.getOriginalFilename()).thenReturn("file2.csv");
        when(file2.getInputStream()).thenReturn(file2InputStream);

        files[0] = file1;
        files[1] = file2;

        when(mvpService.getMvp()).thenReturn(new Player("John", "Doe", 10, "Team A", 15));

        ResponseEntity<Player> expectedResponse = ResponseEntity.ok(new Player("John", "Doe", 10, "Team A", 15));

        ResponseEntity<Player> response = tournamentController.calculateMvp(files);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(mvpService).processGames(gameDataCaptor.capture());
        verify(mvpService).getMvp();

        Map<List<String>, List<String>> capturedGameData = gameDataCaptor.getValue();
        assertEquals(2, capturedGameData.size());
        assertTrue(capturedGameData.containsKey(List.of("file1.csv", "John;Doe;10;Team A;15")));
        assertTrue(capturedGameData.containsKey(List.of("file2.csv", "Jane;Smith;7;Team B;12")));
    }

    @Test
    public void calculateMvp_EmptyFiles_ReturnsBadRequest() throws IOException {
        // Arrange
        MultipartFile[] files = new MultipartFile[0];
        ResponseEntity<Player> expectedResponse = ResponseEntity.badRequest().build();

        // Act
        ResponseEntity<Player> response = tournamentController.calculateMvp(files);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(mvpService, never()).processGames(anyMap());
        verify(mvpService, never()).getMvp();
    }

    @Test
    public void calculateMvp_ExceptionDuringProcessing_ReturnsInternalServerError() throws IOException {
        MultipartFile[] files = new MultipartFile[2];

        doAnswer(invocation -> {
            throw new IOException("Error processing games");
        }).when(mvpService).processGames(anyMap());

        ResponseEntity<Player> expectedResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        ResponseEntity<Player> response = tournamentController.calculateMvp(files);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
    }
}