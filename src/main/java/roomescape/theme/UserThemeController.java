package roomescape.theme;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
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
    public ResponseEntity<?> getRankedThemes(@RequestParam(defaultValue = "reservationCount") String sort,
                                       @RequestParam(defaultValue = "DESC") String order,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                       @RequestParam(required = false) Long limit) {

        LocalDate actualEndDate = (endDate != null) ? endDate : LocalDate.now();
        LocalDate actualStartDate = (startDate != null) ? startDate : actualEndDate.minusDays(7);

        List<ThemeResponse> response = userThemeService.getThemes(sort, order, actualStartDate, actualEndDate, limit).stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok().body(response);
    }
}
