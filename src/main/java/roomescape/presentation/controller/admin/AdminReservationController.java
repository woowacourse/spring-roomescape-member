package roomescape.presentation.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.ReservationService;
import roomescape.presentation.dto.request.AdminReservationRequestDto;
import roomescape.presentation.dto.response.ReservationResponseDto;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    @Autowired
    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(@Valid @RequestBody AdminReservationRequestDto reservationDto) {
        Long id = reservationService.createReservationWithMember(reservationDto);
        ReservationResponseDto reservation = reservationService.readReservationOne(id);
        return ResponseEntity.ok().body(reservation);
    }
}
