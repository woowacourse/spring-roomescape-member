package roomescape.theme.controller;

import static org.springframework.http.HttpStatus.CREATED;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
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

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {
    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    @Operation(summary = "Read all themes", description = "모든 테마를 조회하는 api")
    public ResponseEntity<List<ThemeDetailDto>> getThemes() {
        List<ThemeDetailDto> responseData = themeService.findThemes().stream()
                .map(ThemeDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Read a theme by id", description = "테마 id로 테마를 조회하는 api")
    public ResponseEntity<ThemeDetailDto> getTheme(@PathVariable Long id) {
        ThemeDetailDto responseData = ThemeDetailDto.from(themeService.findTheme(id));
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    @Operation(summary = "Create a theme", description = "테마를 생성하는 api")
    public ResponseEntity<ThemeDetailDto> createTheme(@Valid @RequestBody ThemeSaveDto dto) {
        Theme theme = themeService.register(dto.name(), dto.description(), dto.thumbnailUrl());
        ThemeDetailDto responseData = ThemeDetailDto.from(theme);
        return ResponseEntity.status(CREATED).body(responseData);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update theme status", description = "테마 활성화 상태를 업데이트하는 api")
    public ResponseEntity<ThemeDetailDto> updateThemeStatus(@PathVariable Long id, @Valid @RequestBody ThemeActiveUpdateDto dto) {
        Theme theme = themeService.updateStatus(id, dto.isActive());
        ThemeDetailDto responseData = ThemeDetailDto.from(theme);
        return ResponseEntity.ok(responseData);
    }
}
