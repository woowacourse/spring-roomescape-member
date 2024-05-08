package roomescape.controller;

import jakarta.servlet.http.Cookie;
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
import roomescape.controller.request.ReservationRequest2;
import roomescape.controller.request.ReservationRequest3;
import roomescape.controller.response.ReservationResponse;
import roomescape.domain.Member;
import roomescape.service.ReservationService;
import roomescape.ui.AuthenticationPrincipal;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        List<ReservationResponse> reservationResponses = reservationService.findAll();

        return ResponseEntity.ok(reservationResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> save(@AuthenticationPrincipal Member member, @RequestBody ReservationRequest3 reservationRequest3) {
        ReservationResponse reservationResponse = reservationService.save(new ReservationRequest2(member.getId(), reservationRequest3.date(), reservationRequest3.timeId(), reservationRequest3.themeId()));

        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        reservationService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
