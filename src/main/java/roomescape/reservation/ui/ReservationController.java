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
import roomescape.common.uri.UriFactory;
import roomescape.reservation.application.ReservationWebFacade;
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

    private final ReservationWebFacade reservationWebFacade;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAll() {
        final List<ReservationResponse> reservations = reservationWebFacade.getAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/times")
    public ResponseEntity<List<AvailableReservationTimeWebResponse>> getAvailable(
            @RequestParam final LocalDate date,
            @RequestParam final Long themeId) {
        final List<AvailableReservationTimeWebResponse> reservations = reservationWebFacade.getAvailable(date, themeId);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @RequestBody final CreateReservationWebRequest createReservationWebRequest) {
        final ReservationResponse reservationResponse = reservationWebFacade.create(createReservationWebRequest);
        final URI location = UriFactory.buildPath(BASE_PATH, String.valueOf(reservationResponse.id()));
        return ResponseEntity.created(location)
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        reservationWebFacade.delete(id);
        return ResponseEntity.noContent().build();
    }
}
