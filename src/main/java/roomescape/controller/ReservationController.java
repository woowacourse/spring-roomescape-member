package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.request.ReservationRequest;
import roomescape.controller.response.MemberReservationTimeResponse;
import roomescape.controller.response.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.LoginMember;
import roomescape.model.Reservation;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationDto;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> showReservations() {
        List<Reservation> reservations = reservationService.findAllReservations();
        List<ReservationResponse> response = reservations.stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody ReservationRequest request, LoginMember member) {
        ReservationDto reservationDto = ReservationDto.from(request, member);
        Reservation reservation = reservationService.saveReservation(reservationDto);
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity
                .created(URI.create("/reservations/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        validateNull(id);
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/times")
    public ResponseEntity<List<MemberReservationTimeResponse>> showReservationTimesInformation(
            @RequestParam(name = "date") LocalDate date,
            @RequestParam(name = "themeId") Long themeId) {
        validateNull(themeId);
        List<MemberReservationTimeResponse> response = reservationService.findReservationTimesInformation(date, themeId);
        // TODO: 여기서 response 객체로 반환하도록 수정
        return ResponseEntity.ok(response);
    }

    private void validateNull(Long value) {
        if (value == null) {
            throw new BadRequestException("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
        }
    }
}
