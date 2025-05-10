package roomescape.presentation.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthRequired;
import roomescape.auth.Role;
import roomescape.business.model.entity.Reservation;
import roomescape.business.model.vo.LoginInfo;
import roomescape.business.model.vo.UserRole;
import roomescape.business.service.ReservationService;
import roomescape.presentation.dto.request.AdminReservationRequest;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.ReservationResponse;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    @AuthRequired
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid ReservationRequest request, LoginInfo loginInfo) {
        Reservation reservation = reservationService.addAndGet(request.date(), request.timeId(), request.themeId(), loginInfo.id());
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.created(URI.create("/reservations")).body(response);
    }

    @PostMapping("/admin/reservations")
    @AuthRequired
    @Role(UserRole.ADMIN)
    public ResponseEntity<ReservationResponse> adminCreateReservation(@RequestBody @Valid AdminReservationRequest request) {
        Reservation reservation = reservationService.addAndGet(request.date(), request.timeId(), request.themeId(), request.userId());
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.created(URI.create("/reservations")).body(response);
    }

    @GetMapping("/reservations")
    @AuthRequired
    public ResponseEntity<List<ReservationResponse>> getReservations(
            @RequestParam(required = false) String themeId,
            @RequestParam(required = false, name = "memberId") String userId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ) {
        List<Reservation> reservations = reservationService.getAll(themeId, userId, dateFrom, dateTo);
        List<ReservationResponse> responses = ReservationResponse.from(reservations);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/reservations/{id}")
    @AuthRequired
    public ResponseEntity<Void> deleteReservation(@PathVariable String id, LoginInfo loginInfo) {
        reservationService.delete(id, loginInfo.id());
        return ResponseEntity.noContent().build();
    }
}
