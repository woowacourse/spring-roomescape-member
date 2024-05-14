package roomescape.controller.api;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.LoginMemberDto;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.ReservationResponseDto;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody @Valid final ReservationRequestDto request,
            final LoginMemberDto loginMember
    ) {
        final Reservation reservation = reservationService.create(request, loginMember);
        final ReservationResponseDto response = new ReservationResponseDto(reservation);
        return ResponseEntity.created(URI.create("/reservations/" + response.getId())).body(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponseDto>> findReservationsWithConditions(
            @RequestParam(value = "member-id", required = false) final Long memberId,
            @RequestParam(value = "theme-id", required = false) final Long themeId,
            @RequestParam(value = "date-from", required = false) final LocalDate dateFrom,
            @RequestParam(value = "date-to", required = false) final LocalDate dateTo
    ) {
        return reservationService.findAllWithConditions(memberId, themeId, dateFrom, dateTo)
                .stream()
                .map(ReservationResponseDto::new)
                .collect(collectingAndThen(toList(), ResponseEntity::ok));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") final long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
