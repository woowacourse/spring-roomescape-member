package roomescape.domain.reservation.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.dto.reservation.ReservationAdminCreateRequest;
import roomescape.domain.reservation.dto.reservation.ReservationCreateDto;
import roomescape.domain.reservation.dto.reservation.ReservationResponse;
import roomescape.domain.reservation.service.ReservationService;

@RequestMapping("/admin/reservations")
@RestController
public class ReservationAdminController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationAdminController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAllReservations(
            @RequestParam(value = "themeId", required = false) final Long themeId,
            @RequestParam(value = "memberId", required = false) final Long memberId,
            @RequestParam(value = "dateFrom", required = false) final LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) final LocalDate dateTo) {
        final List<ReservationResponse> response = reservationService.getAll(themeId, memberId, dateFrom, dateTo);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody final ReservationAdminCreateRequest request) {
        final ReservationCreateDto createDto = new ReservationCreateDto(request.memberId(), request.timeId(),
                request.themeId(), request.date());

        final ReservationResponse response = reservationService.create(createDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

}
