package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeQueryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeQueryService themeQueryService;

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getSortedPopularThemesAtPeriod(
            @RequestParam("date") LocalDate today,
            @RequestParam("limit") int limit) {


        LocalDate startAt = today.minusWeeks(1L);
        LocalDate endAt = today.minusDays(1);

        List<ThemeResponse> popularThemesBy = themeQueryService.findPopularThemesBy(startAt, endAt, limit);

        return ResponseEntity.ok(popularThemesBy);
    }
}
