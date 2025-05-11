package roomescape.controller.reservation;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.reservation.ThemeRequest;
import roomescape.dto.reservation.ThemeResponse;
import roomescape.service.reservation.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService service;

    public ThemeController(ThemeService service) {
        this.service = service;
    }

    @GetMapping
    public List<ThemeResponse> readAllTheme() {
        return service.readAllTheme();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponse postTheme(@RequestBody ThemeRequest request,
                                   HttpServletResponse response) {
        ThemeResponse themeResponse = service.postTheme(request);
        response.setHeader("Location", "/themes/" + themeResponse.id());
        return themeResponse;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable Long id) {
        service.deleteTheme(id);
    }

    @GetMapping("/popular")
    public List<ThemeResponse> readPopularThemesByPeriod(
            @RequestParam(value = "period", defaultValue = "7") int period,
            @RequestParam(value = "maxResults", defaultValue = "10") int maxResults) {
        return service.readPopularThemesByPeriod(period, maxResults);
    }
}
