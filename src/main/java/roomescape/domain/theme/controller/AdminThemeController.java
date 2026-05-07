package roomescape.domain.theme.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.dto.request.ThemeCreatedRequestDto;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.service.ThemeService;

@RestController
@RequestMapping("/api/admin/themes")
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping()
    public ResponseEntity<ThemeResponseDto> saveTheme(@RequestBody ThemeCreatedRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(themeService.saveTheme(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteThemeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
