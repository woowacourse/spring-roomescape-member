package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ReservationCommandService;
import roomescape.service.ReservationTimeQueryService;
import roomescape.service.ThemeQueryService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationTimeQueryService reservationTimeQueryService;
    private final ThemeQueryService themeQueryService;

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") long themeId) {
        return ResponseEntity.ok(reservationTimeQueryService.findAvailableReservationTimes(date, themeId));
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        ReservationResponse reservationResponse = reservationCommandService.create(request.name(), request.date(), request.timeId(), request.themeId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(reservationResponse);
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        return ResponseEntity.ok(themeQueryService.findAllThemes());
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<ThemeResponse>> getSortedPopularThemesAtPeriod(
            @RequestParam("startAt") LocalDate startAt,
            @RequestParam("endAt") LocalDate endAt,
            @RequestParam("limit") int limit) {
        List<ThemeResponse> popularThemesBy = themeQueryService.findPopularThemesByPeriod(startAt, endAt, limit);

        return ResponseEntity.ok(popularThemesBy);
    }
}
