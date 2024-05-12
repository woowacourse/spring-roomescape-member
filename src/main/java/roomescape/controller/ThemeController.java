package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.request.ThemeRequest;
import roomescape.controller.response.ThemeResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.theme.Theme;
import roomescape.service.ThemeService;
import roomescape.service.dto.ThemeDto;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.findAllThemes();
        List<ThemeResponse> response = themes.stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(@Valid @RequestBody ThemeRequest request) {
        ThemeDto themeDto = ThemeDto.from(request);
        Theme theme = themeService.saveTheme(themeDto);
        ThemeResponse response = ThemeResponse.from(theme);
        return ResponseEntity
                .created(URI.create("/themes/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") Long id) {
        validateNull(id);
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rank")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes() {
        List<Theme> themes = themeService.findPopularThemes();
        List<ThemeResponse> response = themes.stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    private void validateNull(Long value) {
        if (value == null) {
            throw new BadRequestException("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
        }
    }
}
