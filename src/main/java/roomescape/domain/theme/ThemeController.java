package roomescape.domain.theme;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.AdminRequestValidator;
import roomescape.domain.theme.dto.ThemeResponse;

@RestController
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;
    private final AdminRequestValidator validator;

    @GetMapping("/admin/themes")
    public ResponseEntity<List<ThemeResponse>> getAllTheme(HttpServletRequest request) {
        if (validator.isUnauthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ThemeResponse> response = themeService.getAllTheme();
        return ResponseEntity.ok(response);
    }
}
