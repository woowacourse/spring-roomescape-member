package roomescape.time.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.utils.UriFactory;
import roomescape.time.service.ReservationTimeService;
import roomescape.time.controller.dto.CreateReservationTimeWebRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ReservationTimeController.BASE_PATH)
public class ReservationTimeController {

    public static final String BASE_PATH = "/times";

    private final ReservationTimeService reservationTimeService;

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getAll() {
        final List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.getAll();
        return ResponseEntity.ok(reservationTimeResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @RequestBody final CreateReservationTimeWebRequest createReservationTimeWebRequest) {
        final ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(createReservationTimeWebRequest);
        final URI location = UriFactory.buildPath(BASE_PATH, String.valueOf(reservationTimeResponse.id()));
        return ResponseEntity.created(location)
                .body(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
