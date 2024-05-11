package roomescape.controller;

import java.net.URI;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.request.ReservationMemberCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;
import roomescape.service.TokenService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final TokenService tokenService;

    public ReservationController(ReservationService reservationService, TokenService tokenService) {
        this.reservationService = reservationService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readReservations() {
        List<ReservationResponse> response = reservationService.readReservations();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation
            (@RequestBody ReservationMemberCreateRequest dto, HttpServletRequest servletRequest) {
        Long memberId = tokenService.findTokenId(servletRequest.getCookies());
        ReservationAdminCreateRequest request = ReservationAdminCreateRequest.of(dto, memberId);

        ReservationResponse response = reservationService.createReservation(request);

        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity
                .created(location)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
