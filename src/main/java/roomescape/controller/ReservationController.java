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
import roomescape.dto.LoginMember;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // 관리자 예약 등록
//    @PostMapping("/admin/reservation")
//    public ResponseEntity<ReservationResponse> addReservationByAdmin(@RequestBody ReservationRequest reservationRequest
//    ) {
//        Long savedId = reservationService.addReservation(reservationRequest);
//        ReservationResponse reservationResponse = reservationService.getReservation(savedId);
//        return ResponseEntity.created(URI.create("/reservations/" + savedId)).body(reservationResponse);
//    }

//     사용자 예약 등록
    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> addReservationByUser(
            @RequestBody ReservationRequest reservationRequest,
            LoginMember loginMember
    ) {
        Long savedId = reservationService.addReservation(reservationRequest, loginMember);
        ReservationResponse reservationResponse = reservationService.getReservation(savedId);
        return ResponseEntity.created(URI.create("/reservations/" + savedId)).body(reservationResponse);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> reservationResponses = reservationService.getAllReservations();
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable Long id) {
        ReservationResponse reservationResponse = reservationService.getReservation(id);
        return ResponseEntity.ok(reservationResponse);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
