package roomescape.theme.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.service.ReservationService;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService themeService;
    private final ReservationService reservationService;

    public ThemeController(ThemeService themeService, ReservationService reservationService) {
        this.themeService = themeService;
        this.reservationService = reservationService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> readAll() {
        List<ThemeResponse> responses = themeService.findAll()
                .stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping(value = "/themes", params = "popular=true")
    public ResponseEntity<List<ThemeResponse>> readPopular(
            @RequestParam("period") int period,
            @RequestParam("limit") int limit
    ) {
        List<ThemeResponse> responses = reservationService.findPopularThemes(period, limit).popularThemes()
                .stream()
                .map(ThemeResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/admin/themes")
    public ResponseEntity<ThemeResponse> create(@RequestBody ThemeRequest requestDto) {
        Theme theme = themeService.save(requestDto.toCommand());
        ThemeResponse response = ThemeResponse.from(theme);
        return ResponseEntity
                .created(URI.create("/themes/" + theme.getId()))
                .body(response);
    }

    @DeleteMapping("/admin/themes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
