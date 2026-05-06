package roomescape.theme.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.entity.Theme;
import roomescape.theme.payload.ThemeRequest;
import roomescape.theme.payload.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponse postTheme(@Valid @RequestBody ThemeRequest request) {
        Theme theme = themeService.save(request);
        return ThemeResponse.from(theme);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponse> getAllTimes() {
        return themeService.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTimes(@PathVariable Long id) {
        themeService.deleteById(id);
    }
}
