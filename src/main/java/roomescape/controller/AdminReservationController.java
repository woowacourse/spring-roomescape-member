package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.request.AdminReservationWebRequest;
import roomescape.controller.response.AdminReservationWebResponse;
import roomescape.service.ReservationService;
import roomescape.service.request.ReservationAppRequest;
import roomescape.service.response.ReservationAppResponse;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<AdminReservationWebResponse> reserve(
        @Valid @RequestBody AdminReservationWebRequest request) {
        ReservationAppRequest appRequest = new ReservationAppRequest(request.date(), request.timeId(),
            request.themeId(), request.memberId());

        ReservationAppResponse appResponse = reservationService.save(appRequest);
        AdminReservationWebResponse adminReservationWebResponse = new AdminReservationWebResponse(
            appResponse.date().getDate(),
            appRequest.timeId(), appRequest.themeId(), appRequest.memberId());

        return ResponseEntity.created(URI.create("/reservations/" + appResponse.id()))
            .body(adminReservationWebResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBy(@PathVariable Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
