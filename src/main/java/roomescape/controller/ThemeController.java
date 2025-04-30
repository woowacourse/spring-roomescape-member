package roomescape.controller;

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
import roomescape.dto.ThemeCreateRequestDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponseDto>> getAllThemes() {
        List<ThemeResponseDto> allThemes = themeService.findAllThemes();
        return ResponseEntity.ok(allThemes);
    }

    @PostMapping
    public ResponseEntity<ThemeResponseDto> postTheme(@RequestBody final ThemeCreateRequestDto requestDto) {
        ThemeResponseDto responseDto = themeService.createTheme(requestDto);
        return ResponseEntity.created(URI.create("themes/" + responseDto.id())).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") final Long id) {
        themeService.deleteThemeById(id);
        return ResponseEntity.noContent().build();
    }
}
