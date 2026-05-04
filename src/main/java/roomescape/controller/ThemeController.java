package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeRequestDTO;
import roomescape.dto.ThemeResponseDTO;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }


    @PostMapping
    public ResponseEntity<ThemeResponseDTO> add(
            @RequestBody ThemeRequestDTO request) {
        ThemeResponseDTO saved = themeService.addTheme(request);
        return ResponseEntity.created(URI.create("/theme/" + saved.id())).build();
    }

    @GetMapping
    public List<ThemeResponseDTO> readThemes() {
        return themeService.findAllThemes();
    }
}
