package roomescape.controller;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
import roomescape.controller.request.ReservationRequest;
import roomescape.controller.request.ReservationRequest3;
import roomescape.controller.response.ReservationResponse;
import roomescape.controller.response.UserResponse;
import roomescape.service.ReservationService;

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
    public ResponseEntity<ReservationResponse> save(HttpServletRequest request, @RequestBody ReservationRequest3 reservationRequest3) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        String secret = Jwts.parser()
                .setSigningKey("secret")
                .parseClaimsJws(token)
                .getBody().getSubject();
        UserResponse userResponse = reservationService.findByEmail(secret);
        ReservationRequest reservationRequest = new ReservationRequest(userResponse.name(), reservationRequest3.date().toString(), reservationRequest3.timeId(),
                reservationRequest3.themeId());
        ReservationResponse reservationResponse = reservationService.save(reservationRequest);

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
