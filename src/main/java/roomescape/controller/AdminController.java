package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ReservationCommandService;
import roomescape.service.ReservationQueryService;
import roomescape.service.ReservationTimeCommandService;
import roomescape.service.ReservationTimeQueryService;
import roomescape.service.ThemeCommandService;
import roomescape.service.ThemeQueryService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(("/admin"))
@RequiredArgsConstructor
public class AdminController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;
    private final ReservationTimeCommandService reservationTimeCommandService;
    private final ReservationTimeQueryService reservationTimeQueryService;
    private final ThemeCommandService themeCommandService;
    private final ThemeQueryService themeQueryService;


    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(reservationQueryService.getAllReservations());
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        ReservationResponse reservationResponse = reservationCommandService.create(request.name(), request.date(), request.timeId(), request.themeId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(reservationResponse);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getAllTimes() {
        return ResponseEntity.ok(reservationTimeQueryService.findAllReservationTimes());
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@RequestBody ReservationTimeRequest request) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeCommandService.create(request.startAt());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(reservationTimeResponse);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("id") Long id) {
        reservationTimeCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        return ResponseEntity.ok(themeQueryService.findAllThemes());
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest request) {
        ThemeResponse themeResponse = themeCommandService.create(request.name(), request.thumbnailUrl(), request.description());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(themeResponse);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") Long id) {
        themeCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
