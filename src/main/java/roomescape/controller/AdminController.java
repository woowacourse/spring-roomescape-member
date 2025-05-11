package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.SearchConditionRequest;
import roomescape.service.ReservationService;

@RequestMapping("/admin/reservations")
@RestController
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createAdminReservation(
            @RequestBody @Valid AdminReservationRequest request) {
        ReservationResponseDto response = reservationService.saveReservation(request);
        return ResponseEntity.created(URI.create("/admin/reservations" + response.id())).body(response);
    }

    @GetMapping("/search/")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByCondition(
            @ModelAttribute @Valid SearchConditionRequest request) {
        List<ReservationResponseDto> response = reservationService.findByCondition(request);
        return ResponseEntity.ok(response);
    }
}
