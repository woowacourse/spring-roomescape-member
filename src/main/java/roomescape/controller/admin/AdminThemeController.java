package roomescape.controller.admin;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.service.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {
    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponseDto> create(@Valid @RequestBody ThemeRequestDto themeRequest) {
        Theme theme = themeService.create(themeRequest);
        return ResponseEntity.ok(ThemeResponseDto.from(theme));
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponseDto>> findAll() {
        List<Theme> themes = themeService.findAll();
        return ResponseEntity.ok(themes.stream()
                .map(ThemeResponseDto::from)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponseDto> findById(@PathVariable Long id) {
        Theme themeById = themeService.findById(id);
        return ResponseEntity.ok(ThemeResponseDto.from(themeById));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
