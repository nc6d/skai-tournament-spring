package com.example.skaitournamentspring.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Parser {
    public static Map<List<String>, List<String>> fromCsv(MultipartFile[] files) throws IOException {
        Map<List<String>, List<String>> gameData = new HashMap<>();
        for (var file : files) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String fileName = Objects.requireNonNull(file.getOriginalFilename());
                String sport = br.readLine();
                List<String> lines = br.lines().collect(Collectors.toList());

                gameData.put(List.of(fileName, sport), lines);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
        log.info("Files parsed: {}", gameData.size());

        return gameData;
    }
}