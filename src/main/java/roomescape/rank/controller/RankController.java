package roomescape.rank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.rank.service.RankService;
import roomescape.rank.response.RankTheme;

@RestController
public class RankController {
    private final RankService rankService;

    public RankController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping("/rank")
    public ResponseEntity<List<RankTheme>> getRank() {
        return ResponseEntity.ok(rankService.getRank());
    }
}
