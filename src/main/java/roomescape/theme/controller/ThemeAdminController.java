package roomescape.theme.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.request.ThemeActiveUpdateDto;
import roomescape.theme.dto.request.ThemeSaveDto;
import roomescape.theme.dto.response.PopularThemeDetailDto;
import roomescape.theme.dto.response.ThemeDetailDto;
import roomescape.theme.service.ThemeService;

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

    @GetMapping("/themes/popular")
    public ResponseEntity<List<PopularThemeDetailDto>> getPopularThemes(@RequestParam int top){
        List<PopularThemeDetailDto> responseData = themeService.readPopularThemes(top).stream()
                .map(PopularThemeDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeDetailDto> createTheme(@Validated @RequestBody ThemeSaveDto dto) {
        Theme theme = themeService.register(dto.name(), dto.description(), dto.thumbnailUrl());
        ThemeDetailDto responseData = ThemeDetailDto.from(theme);
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/themes/{id}")
    public ResponseEntity<ThemeDetailDto> updateThemeStatus(@PathVariable Long id, @RequestBody ThemeActiveUpdateDto dto) {
        Theme theme = themeService.updateStatus(id, dto.isActive());
        ThemeDetailDto responseData = ThemeDetailDto.from(theme);
        return ResponseEntity.ok(responseData);
    }

}
