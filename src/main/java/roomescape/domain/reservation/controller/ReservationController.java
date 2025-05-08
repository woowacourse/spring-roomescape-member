package roomescape.domain.reservation.controller;

import jakarta.validation.Valid;
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
import roomescape.domain.auth.config.AuthenticationPrincipal;
import roomescape.domain.auth.dto.LoginUserDto;
import roomescape.domain.reservation.dto.BookedReservationTimeResponse;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.service.ReservationService;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAllReservations(
            @RequestParam(value = "themeId", required = false) final Long themeId,
            @RequestParam(value = "memberId", required = false) final Long memberId,
            @RequestParam(value = "dataFrom", required = false) final LocalDate dataFrom,
            @RequestParam(value = "dataTo", required = false) final LocalDate dataTo
    ) {
        final List<ReservationResponse> response = reservationService.getAll(themeId, memberId, dataFrom, dataTo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookedReservationTimeResponse>> readAvailableReservationTimes(
            @RequestParam("date") final LocalDate date, @RequestParam("themeId") final Long themeId) {
        final List<BookedReservationTimeResponse> responses = reservationService.getAvailableTimes(date, themeId);

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody final ReservationRequest request,
                                                      @AuthenticationPrincipal final LoginUserDto userDto) {
        final ReservationResponse response = reservationService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent()
                .build();
    }
}
