package roomescape.controller.theme;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.controller.theme.dto.CreateThemeRequest;
import roomescape.controller.theme.dto.ThemeResponse;
import roomescape.service.ThemeService;
import roomescape.service.dto.AvailableTimeSlotDto;

@Controller
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService service;

    public ThemeController(final ThemeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> allThemes() {
        var themes = service.allThemes();
        var response = ThemeResponse.from(themes);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> add(@RequestBody CreateThemeRequest request) {
        var theme = service.add(request.name(), request.description(), request.thumbnail());
        var response = ThemeResponse.from(theme);
        return ResponseEntity.created(URI.create("/themes/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/popular", params = {"startDate", "endDate", "limit"})
    public ResponseEntity<List<ThemeResponse>> availableTimes(
        @RequestParam("startDate") LocalDate startDate,
        @RequestParam("endDate") LocalDate endDate,
        @RequestParam("limit") Integer limit
    ) {
        var themes = service.findPopularThemes(startDate, endDate, limit);
        var response = ThemeResponse.from(themes);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
