package roomescape.theme.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.theme.dto.ThemeRequestDto;
import roomescape.theme.dto.ThemeResponseDto;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponseDto>> findAll() {
        List<ThemeResponseDto> themeResponseDtos = themeService.findAll().stream()
                                                               .map(ThemeResponseDto::new)
                                                               .toList();
        return ResponseEntity.ok(themeResponseDtos);
    }

    @PostMapping
    public ResponseEntity<ThemeResponseDto> save(@RequestBody final ThemeRequestDto request) {
        ThemeResponseDto responseDto = new ThemeResponseDto(themeService.save(request));
        return ResponseEntity.created(URI.create("/themes/" + responseDto.id())).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        themeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponseDto>> findPopularThemes() {
        List<ThemeResponseDto> themes = themeService.findPopularThemes().stream()
                                                    .map(ThemeResponseDto::new)
                                                    .toList();
        return ResponseEntity.ok(themes);
    }
}
