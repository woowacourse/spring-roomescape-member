package roomescape.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.dto.request.LoginMember;
import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.request.ReservationDetailRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readReservations() {
        List<ReservationResponse> response = reservationService.readReservations();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail")
    public ResponseEntity<List<ReservationResponse>> readDetailReservations(ReservationDetailRequest request) {
        List<ReservationResponse> response = reservationService.readReservations(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationAdminCreateRequest dto, LoginMember member) {
        ReservationAdminCreateRequest request = ReservationAdminCreateRequest.of(dto, member.id());
        ReservationResponse response = reservationService.createReservation(request);

        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity.created(location)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent()
                .build();
    }
}
