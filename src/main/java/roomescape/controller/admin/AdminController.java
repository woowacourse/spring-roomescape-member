package roomescape.controller.admin;

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
import roomescape.dto.admin.request.AdminReservationRequest;
import roomescape.dto.admin.request.SearchConditionRequest;
import roomescape.dto.reservation.response.ReservationResponseDto;
import roomescape.service.reservation.ReservationService;

@RequestMapping("/admin/reservations")
@RestController
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createAdminReservation(
            @RequestBody @Valid final AdminReservationRequest request) {
        final ReservationResponseDto response = reservationService.saveReservation(request);
        return ResponseEntity.created(URI.create("/admin/reservations" + response.id())).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByCondition(
            @ModelAttribute @Valid final SearchConditionRequest request) {
        final List<ReservationResponseDto> response = reservationService.findByCondition(request);

        return ResponseEntity.ok(response);
    }
}
