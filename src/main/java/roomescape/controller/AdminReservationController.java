package roomescape.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.config.LoginUser;
import roomescape.domain.member.Member;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
public class AdminReservationController {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> create(@LoginUser Member member,
                                                      @RequestBody ReservationCreateRequest request) {
        LocalDateTime now = LocalDateTime.now(KST_ZONE);
        return ResponseEntity.created(URI.create("/admin/reservations"))
                .body(reservationService.add(member, request, now));
    }
}
