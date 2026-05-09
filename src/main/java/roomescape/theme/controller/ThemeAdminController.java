package roomescape.theme.controller;

import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.request.ThemeActiveUpdateDto;
import roomescape.theme.dto.request.ThemeSaveDto;
import roomescape.theme.dto.response.ThemeDetailDto;
import roomescape.theme.service.ThemeService;

@Slf4j
@RestController
@RequestMapping("/admin")
public class ThemeAdminController {
    private final ThemeService themeService;

    public ThemeAdminController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeDetailDto>> getThemes() {
        List<ThemeDetailDto> responseData = themeService.readThemes().stream()
                .map(ThemeDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/themes/{id}")
    public ResponseEntity<ThemeDetailDto> getTheme(@PathVariable Long id) {
        ThemeDetailDto responseData = ThemeDetailDto.from(themeService.readTheme(id));
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeDetailDto> createTheme(@Valid @RequestBody ThemeSaveDto dto) {
        Theme theme = themeService.register(dto.name(), dto.description(), dto.thumbnailUrl());
        ThemeDetailDto responseData = ThemeDetailDto.from(theme);
        return ResponseEntity.status(CREATED).body(responseData);
    }

    @PatchMapping("/themes/{id}")
    public ResponseEntity<ThemeDetailDto> updateThemeStatus(@PathVariable Long id, @Valid @RequestBody ThemeActiveUpdateDto dto) {
        Theme theme = themeService.updateStatus(id, dto.isActive());
        ThemeDetailDto responseData = ThemeDetailDto.from(theme);
        return ResponseEntity.ok(responseData);
    }
}
