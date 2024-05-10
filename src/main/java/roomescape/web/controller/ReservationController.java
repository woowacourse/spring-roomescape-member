package roomescape.web.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.service.ReservationService;
import roomescape.web.dto.request.MemberInfo;
import roomescape.web.dto.request.ReservationRequest;
import roomescape.web.dto.request.UserReservationRequest;
import roomescape.web.dto.response.ReservationResponse;

@RequestMapping("/reservations")
@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAllReservation() {
        List<ReservationResponse> response = reservationService.findAllReservation();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(@Valid @RequestBody UserReservationRequest request,
                                                               MemberInfo memberInfo) {
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .date(request.date())
                .memberId(memberInfo.id())
                .timeId(request.timeId())
                .themeId(request.themeId())
                .build();

        ReservationResponse response = reservationService.saveReservation(reservationRequest, memberInfo);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable(value = "reservation_id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
