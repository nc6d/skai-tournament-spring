package com.example.skaitournamentspring.controller;

import com.example.skaitournamentspring.model.Player;
import com.example.skaitournamentspring.service.MvpService;
import com.example.skaitournamentspring.util.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class TournamentController {
    private final MvpService mvpService;

    @PostMapping("/calculate")
    public ResponseEntity<Player> calculateMvp(@RequestParam("file") MultipartFile[] files) throws IOException {
        if (files.length == 0) return ResponseEntity.badRequest().build();
        if (Arrays.stream(files).anyMatch(Objects::isNull)) return ResponseEntity.internalServerError().build();
        mvpService.processGames(Parser.fromCsv(files));
        return ResponseEntity.ok(mvpService.getMvp());
    }
}
