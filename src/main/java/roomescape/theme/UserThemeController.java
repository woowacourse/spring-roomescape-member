package roomescape.theme;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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
    public ResponseEntity<List<ThemeResponse>> getRankedThemes(@RequestParam(defaultValue = "reservationCount") String sort,
                                                               @RequestParam(defaultValue = "DESC") String order,
                                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                               @RequestParam(defaultValue = "10") Long limit) {

        LocalDate actualEndDate = endDate;
        if (actualEndDate == null) {
            actualEndDate = LocalDate.now();
        }

        LocalDate actualStartDate = startDate;
        if (actualStartDate == null) {
            actualStartDate = actualEndDate.minusDays(7);
        }

        List<ThemeResponse> response = userThemeService.getThemes(sort, order, actualStartDate, actualEndDate, limit)
                .stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok().body(response);
    }
}
