package roomescape.controller;

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
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.controller.response.AvailableReservationTimeResponse;
import roomescape.controller.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.findAll();

        return ResponseEntity.ok(reservationTimeResponses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableReservationTimeResponse>> findAllAvailable(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Integer themeId
    ) {
        List<AvailableReservationTimeResponse> reservationTimeResponses = reservationTimeService.findAllWithAvailability(
                date, themeId);

        return ResponseEntity.ok(reservationTimeResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> save(@RequestBody ReservationTimeRequest reservationTimeRequest) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.save(reservationTimeRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        reservationTimeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
