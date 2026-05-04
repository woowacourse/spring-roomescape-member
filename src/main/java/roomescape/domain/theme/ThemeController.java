package roomescape.domain.theme;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.AdminRequestValidator;
import roomescape.domain.theme.dto.CreateThemeRequest;
import roomescape.domain.theme.dto.CreateThemeResponse;
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

    @PostMapping("/admin/themes")
    public ResponseEntity<CreateThemeResponse> createTheme(@RequestBody CreateThemeRequest createThemeRequest,
        HttpServletRequest httpServletRequest) {
        if (validator.isUnauthorized(httpServletRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CreateThemeResponse response = themeService.createTheme(createThemeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
