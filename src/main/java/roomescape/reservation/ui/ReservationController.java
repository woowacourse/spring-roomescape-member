package roomescape.reservation.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.utils.UriFactory;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.ui.dto.AvailableReservationTimeWebResponse;
import roomescape.reservation.ui.dto.CreateReservationWebRequest;
import roomescape.reservation.ui.dto.ReservationResponse;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ReservationController.BASE_PATH)
public class ReservationController {

    public static final String BASE_PATH = "/reservations";

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAll() {
        final List<ReservationResponse> reservations = reservationService.getAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/times")
    public ResponseEntity<List<AvailableReservationTimeWebResponse>> getAvailable(
            @RequestParam final LocalDate date,
            @RequestParam final Long themeId) {
        final List<AvailableReservationTimeWebResponse> reservations = reservationService.getAvailable(date, themeId);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @RequestBody final CreateReservationWebRequest createReservationWebRequest) {
        final ReservationResponse reservationResponse = reservationService.create(createReservationWebRequest);
        final URI location = UriFactory.buildPath(BASE_PATH, String.valueOf(reservationResponse.id()));
        return ResponseEntity.created(location)
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
