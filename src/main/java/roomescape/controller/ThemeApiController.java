package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ThemeService;
import roomescape.service.dto.ThemeRequest;
import roomescape.service.dto.ThemeResponse;

@RestController
@RequestMapping("/themes")
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> findAllThemes() {
        return themeService.findAllThemes();
    }

    @GetMapping("/rank")
    public List<ThemeResponse> findTopBookedThemes() {
        return themeService.findTopBookedThemes();
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest requestDto) {
        ThemeResponse theme = themeService.createTheme(requestDto);
        return ResponseEntity.created(URI.create("/themes/" + theme.getId())).body(theme);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTheme(@PathVariable long id) {
        themeService.deleteTheme(id);
    }
}
