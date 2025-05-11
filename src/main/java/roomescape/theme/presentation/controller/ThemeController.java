package roomescape.theme.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.application.ThemeService;
import roomescape.theme.application.dto.ThemeDto;
import roomescape.theme.presentation.dto.request.ThemeRankingRequest;
import roomescape.theme.presentation.dto.request.ThemeRequest;
import roomescape.theme.presentation.dto.response.ThemeResponse;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> getAllThemes() {
        List<ThemeDto> themes = themeService.getAllThemes();
        return ThemeResponse.from(themes);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody ThemeRequest themeRequest) {
        ThemeDto themeDto = themeService.registerTheme(themeRequest);
        ThemeResponse themeResponse = ThemeResponse.from(themeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranking")
    public List<ThemeResponse> getThemeRanking(ThemeRankingRequest request) {
        ThemeRankingCondition condition = ThemeRankingCondition.ofRequestParams(
                request.startDate(), request.endDate(), request.limit());

        List<ThemeDto> themes = themeService.getThemeRanking(condition);
        return ThemeResponse.from(themes);
    }
}
