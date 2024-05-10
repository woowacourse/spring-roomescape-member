package roomescape.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import roomescape.config.LoginUser;
import roomescape.domain.member.Member;
import roomescape.dto.reservation.AvailableReservationResponse;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@LoginUser Member member,
                                                      @RequestBody ReservationCreateRequest request) {
        LocalDateTime now = LocalDateTime.now(KST_ZONE);
        return ResponseEntity.created(URI.create("/reservations"))
                .body(reservationService.add(member, request, now));
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableReservationResponse>> readReservationTimes(@RequestParam String date,
                                                                                   @RequestParam Long themeId) {
        return ResponseEntity.ok(reservationService.findTimeByDateAndThemeID(date, themeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
