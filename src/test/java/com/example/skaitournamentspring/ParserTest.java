package com.example.skaitournamentspring;

import com.example.skaitournamentspring.util.Parser;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    @Test
    void testFromCsv() throws IOException {
        String csvData1 = "basketball\nplayer1;nick1;4;Team A;10;2;7\nplayer2;nick2;8;Team A;0;10;0";
        String csvData2 = "handball\nplayer1;nick1;4;Team A;0;20\nplayer2;nick2;8;Team A;15;20";
        MockMultipartFile file1 = new MockMultipartFile("file1.csv", "file1.csv", "text/csv", csvData1.getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2.csv", "file2.csv", "text/csv", csvData2.getBytes());

        Map<List<String>, List<String>> gameData = Parser.fromCsv(new MockMultipartFile[]{file1, file2});

        assertEquals(2, gameData.size());

        List<String> basketballLines = gameData.get(List.of("file1.csv", "basketball"));
        List<String> handballLines = gameData.get(List.of("file2.csv", "handball"));

        assertEquals(Arrays.asList("player1;nick1;4;Team A;10;2;7", "player2;nick2;8;Team A;0;10;0"), basketballLines);
        assertEquals(Arrays.asList("player1;nick1;4;Team A;0;20", "player2;nick2;8;Team A;15;20"), handballLines);
    }
}
