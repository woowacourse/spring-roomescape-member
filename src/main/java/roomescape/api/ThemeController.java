package roomescape.api;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.facade.ReservationFacade;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;
    private final ReservationFacade reservationFacade;

    public ThemeController(ThemeService themeService, ReservationFacade reservationFacade) {
        this.themeService = themeService;
        this.reservationFacade = reservationFacade;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> search() {
        List<ThemeResponse> responses = themeService.getThemes().stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok().body(responses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> add(@RequestBody ThemeRequest request) {
        Theme theme = new Theme(null, request.name(), request.description(), request.thumbnailImageUrl());
        ThemeResponse response = ThemeResponse.from(themeService.addTheme(theme));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationFacade.deleteTheme(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> searchPopularTop10(@RequestParam(defaultValue = "7") Integer days) {
        List<ThemeResponse> responses = themeService.getPopularTop10Themes(LocalDate.now(), days).stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok().body(responses);
    }
}
