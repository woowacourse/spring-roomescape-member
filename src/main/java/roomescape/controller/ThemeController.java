package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService service;

    public ThemeController(ThemeService service) {
        this.service = service;
    }

    @GetMapping("/themes")
    public List<ThemeResponse> readAllTheme() {
        return service.readAllTheme();
    }

    @PostMapping("/themes")
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponse postTheme(@RequestBody ThemeRequest request) {
        return service.postTheme(request);
    }

    @DeleteMapping("/themes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable long id) {
        service.deleteTheme(id);
    }
}
