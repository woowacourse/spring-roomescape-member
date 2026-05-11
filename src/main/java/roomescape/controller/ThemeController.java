package roomescape.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.theme.ThemeRankResponses;
import roomescape.controller.dto.theme.ThemeRequest;
import roomescape.controller.dto.theme.ThemeRankResponse;
import roomescape.controller.dto.theme.ThemeResponse;
import roomescape.controller.dto.theme.ThemeRankingQuery;
import roomescape.controller.dto.theme.ThemeResponses;
import roomescape.service.ThemeService;
import roomescape.service.dto.theme.ThemeResult;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping
    public ResponseEntity<ThemeResponses> getThemes() {
        List<ThemeResponse> themes = themeService.getThemes().stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(new ThemeResponses(themes));
    }

    @GetMapping("/rank")
    public ResponseEntity<ThemeRankResponses> getThemeRankings(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int limit
    ) {
        ThemeRankingQuery query = new ThemeRankingQuery(days, limit);
        List<ThemeRankResponse> themeRankings = themeService.getThemeRankings(query.toCondition(), LocalDate.now())
                .stream()
                .map(ThemeRankResponse::from)
                .toList();
        return ResponseEntity.ok(new ThemeRankResponses(themeRankings));
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody ThemeRequest request) {
        ThemeResult themeResult = themeService.createTheme(request.toCommand());
        ThemeResponse theme = ThemeResponse.from(themeResult);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(theme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
