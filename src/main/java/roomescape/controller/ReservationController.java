package roomescape.controller;

import java.util.List;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.AuthService;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {
    private final ReservationService reservationService;
    private final AuthService authService;

    @Autowired
    public ReservationController(ReservationService reservationService, AuthService authService) {
        this.reservationService = reservationService;
        this.authService = authService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createWithToken(@RequestBody CreateReservationRequest createReservationRequest, HttpServletRequest request) {
        // JWT 토큰에서 사용자 정보 추출
        Cookie[] cookies = request.getCookies();
        String memberId = authService.getVerifiedPayloadFrom(cookies)
                .getSubject();

        CreateReservationRequest requestWithAuthor = CreateReservationRequest.setMember(createReservationRequest,
                Long.valueOf(memberId));

        ReservationResponse response = reservationService.saveReservation(requestWithAuthor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // TODO: 관리자 전용 API - 분리 필요
    @PostMapping("admin/reservations")
    public ResponseEntity<ReservationResponse> createWithMemberId(@RequestBody CreateReservationRequest request) {
        ReservationResponse response = reservationService.saveReservation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> readAll() {
        List<ReservationResponse> responses = reservationService.readReservations();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
