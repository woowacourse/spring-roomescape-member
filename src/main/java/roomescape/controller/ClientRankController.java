package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.dto.ThemeResponse;
import roomescape.service.RankService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rank")
public class ClientRankController {
    private final RankService rankService;

    public ClientRankController(final RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> read(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam Long count) {
        return ResponseEntity.ok(rankService.getPopularThemeList(startDate, endDate, count));
    }
}
