package roomescape.theme.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.service.UserThemeService;

@RestController
@RequestMapping("/themes")
public class UserThemeController {

    private final UserThemeService userThemeService;

    public UserThemeController(UserThemeService userThemeService) {
        this.userThemeService = userThemeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {

        List<ThemeResponse> response = userThemeService.getAllThemes().stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/rank")
    public ResponseEntity<List<ThemeResponse>> getRankedThemes(
            @RequestParam(defaultValue = "reservationCount") SortColumn sort,
            @RequestParam(defaultValue = "DESC") SortOrder order,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> endDate,
            @RequestParam(defaultValue = "10") Long limit) {

        LocalDate actualEndDate = endDate.orElseGet(LocalDate::now);
        LocalDate actualStartDate = startDate.orElseGet(() -> actualEndDate.minusDays(6));

        List<ThemeResponse> response = userThemeService.getThemes(sort, order, actualStartDate, actualEndDate, limit)
                .stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok().body(response);
    }
}
