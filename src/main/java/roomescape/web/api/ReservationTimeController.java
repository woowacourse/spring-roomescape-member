package roomescape.web.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.request.ReservationTimeRequest;
import roomescape.service.dto.response.AvailableReservationTimeResponse;
import roomescape.service.dto.response.ReservationTimeResponse;
import roomescape.service.dto.validation.DateFormat;
import roomescape.web.api.dto.ReservationTimeListResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> save(@RequestBody @Valid ReservationTimeRequest request) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTimeResponse);
    }

    @GetMapping("/times")
    public ResponseEntity<ReservationTimeListResponse> findAll() {
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.findAll();

        return ResponseEntity.ok(new ReservationTimeListResponse(reservationTimeResponses));
    }

    @GetMapping("/times/available")
    public ResponseEntity<ReservationTimeListResponse> findAvailableByThemeAndDate(
            @RequestParam("date") @DateFormat String date,
            @RequestParam("themeId") @Positive Long themeId
    ) {
        List<AvailableReservationTimeResponse> reservationTimeResponses =
                reservationTimeService.findAllWithAvailability(LocalDate.parse(date), themeId);

        return ResponseEntity.ok(new ReservationTimeListResponse(reservationTimeResponses));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        reservationTimeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
