package roomescape.presentation.controller.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.ReservationService;
import roomescape.application.dto.AdminReservationCreateDto;
import roomescape.application.dto.ReservationDto;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService service;

    public AdminReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationDto> addReservation(@Valid @RequestBody AdminReservationCreateDto request) {
        ReservationDto reservationDto = service.registerReservationByAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationDto);
    }
}
