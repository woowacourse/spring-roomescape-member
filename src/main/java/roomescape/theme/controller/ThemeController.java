package roomescape.theme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequestDto;
import roomescape.theme.dto.ThemeResponseDto;
import roomescape.theme.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponseDto>> findAll() {
        final List<Theme> themes = themeService.findAll();

        final List<ThemeResponseDto> themeResponseDtos = changeToThemeResponseDtos(themes);

        return ResponseEntity.ok(themeResponseDtos);
    }

    @PostMapping
    public ResponseEntity<ThemeResponseDto> save(@RequestBody final ThemeRequestDto request) {
        final Theme theme = themeService.save(request);

        final ThemeResponseDto themeResponseDto = changeToThemeResponseDto(theme);
        final String url = "/themes/" + themeResponseDto.id();

        return ResponseEntity.created(URI.create(url)).body(themeResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        themeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponseDto>> findPopular() {
        List<Theme> themes = themeService.findPopular();

        List<ThemeResponseDto> themeResponseDtos = changeToThemeResponseDtos(themes);
        
        return ResponseEntity.ok(themeResponseDtos);
    }

    private ThemeResponseDto changeToThemeResponseDto(final Theme theme) {
        return new ThemeResponseDto(theme);
    }

    private List<ThemeResponseDto> changeToThemeResponseDtos(final List<Theme> themes) {
        return themes.stream()
                .map(this::changeToThemeResponseDto)
                .toList();
    }
}
