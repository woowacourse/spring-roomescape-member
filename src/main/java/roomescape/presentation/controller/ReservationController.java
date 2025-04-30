package roomescape.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.dto.ReservationDto;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.AdminReservationResponse;
import roomescape.application.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<AdminReservationResponse> getAllReservations() {
        List<ReservationDto> allReservations = service.getAllReservations();
        return AdminReservationResponse.from(allReservations);
    }

    @PostMapping
    public ResponseEntity<AdminReservationResponse> addReservation(@Valid @RequestBody ReservationRequest request) {
        ReservationDto reservationDto = service.registerReservation(request);
        AdminReservationResponse response = AdminReservationResponse.from(reservationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
