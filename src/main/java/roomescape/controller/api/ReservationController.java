package roomescape.controller.api;

import jakarta.validation.Valid;
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
import roomescape.application.ReservationService;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.auth.LoginInfo;
import roomescape.dto.reservation.ReservationInfo;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request,
                                                                 LoginInfo loginMember) {
        ReservationInfo reservationInfo = new ReservationInfo(loginMember.name(), request);
        Reservation reservation = reservationService.reserve(reservationInfo);
        // request -> 날짜, 시간id, 테마 id
        // LoginInfo가 줘야할 정보 : Member 전체.

        // 반환 - 아이디, 이름,         ThemeResponse theme,
        //        LocalDate date,
        //        ReservationTimeResponse time 반환
        ReservationResponse response = ReservationResponse.from(reservation);
        URI location = URI.create("/reservations/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    // todo: 예약 목록 조회 반환값
//        row.insertCell(0).textContent = item.id;              // 예약 id
//    row.insertCell(1).textContent = item.member.name;     // 사용자 name
//    row.insertCell(2).textContent = item.theme.name;      // 테마 name
//    row.insertCell(3).textContent = item.date;            // date
//    row.insertCell(4).textContent = item.time.startAt;    // 예약 시간 startAt
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = reservationService.getReservations();
        List<ReservationResponse> responses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        reservationService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
