package roomescape.domain.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody final ReservationAdminCreateRequest request) {
        final ReservationCreateDto createDto = new ReservationCreateDto(request.memberId(), request.timeId(),
                request.themeId(), request.date());

        final ReservationResponse response = reservationService.create(createDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

}
