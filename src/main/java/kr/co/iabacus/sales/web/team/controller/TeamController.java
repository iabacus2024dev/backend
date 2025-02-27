package kr.co.iabacus.sales.web.team.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.team.dto.TeamResponse;
import kr.co.iabacus.sales.web.team.service.TeamService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<String>> getAllTeamNames() {
        return ResponseEntity.ok(teamService.getAllTeamNames());
    }

    @GetMapping("/tree-view")
    public ResponseEntity<Map<String, Map<String, List<TeamResponse>>>> getTeamTreeView() {
        return ResponseEntity.ok(teamService.getTeamTreeView());
    }

}
