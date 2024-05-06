package roomescape.controller.user;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.service.user.UserThemeService;

@RestController
public class UserThemeController {

    private final UserThemeService userThemeService;

    public UserThemeController(UserThemeService userThemeService) {
        this.userThemeService = userThemeService;
    }

    @GetMapping("/theme-rank")
    public ResponseEntity<List<Theme>> getThemeRank() {
        return ResponseEntity.ok(userThemeService.getThemeRanking());
    }
}
