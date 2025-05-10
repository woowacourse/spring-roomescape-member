package roomescape.presentation.admin.api;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.admin.AdminReservationService;
import roomescape.presentation.admin.dto.AdminReservationRequestDto;
import roomescape.presentation.admin.dto.ReservationQueryCondition;
import roomescape.presentation.member.dto.ReservationResponseDto;

@RestController
@RequestMapping("/admin/reservations")
public final class AdminReservationController {

    private final AdminReservationService reservationService;

    public AdminReservationController(AdminReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @Valid @RequestBody AdminReservationRequestDto reservationRequestDto) {
        ReservationResponseDto reservation = reservationService.createReservation(reservationRequestDto);
        String location = "/reservations/" + reservation.id();
        return ResponseEntity.created(URI.create(location)).body(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByCondition(
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo) {
        ReservationQueryCondition condition = new ReservationQueryCondition(themeId, memberId, dateFrom, dateTo);
        List<ReservationResponseDto> reservations = reservationService.getAllReservationsByCondition(condition);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("reservationId") Long id) {
        reservationService.deleteReservationById(id);
        return ResponseEntity.noContent().build();
    }
}
