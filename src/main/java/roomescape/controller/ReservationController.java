package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.request.ReservationRequest;
import roomescape.controller.response.ReservationTimeInfoResponse;
import roomescape.controller.response.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.LoginMember;
import roomescape.model.Reservation;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationDto;
import roomescape.service.dto.ReservationTimeInfoDto;

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
    public ResponseEntity<List<ReservationTimeInfoResponse>> showReservationTimesInformation(
            @RequestParam(name = "date") LocalDate date,
            @RequestParam(name = "themeId") Long themeId) {
        validateNull(themeId);
        ReservationTimeInfoDto timesInfo = reservationService.findReservationTimesInformation(date, themeId);
        List<ReservationTimeInfoResponse> response = ReservationTimeInfoResponse.from(timesInfo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter") // TODO: 각 조건이 없는 경우?
    public ResponseEntity<List<ReservationResponse>> searchReservations(@RequestParam(name = "member_id") Long memberId,
                                                                        @RequestParam(name = "theme_id") Long themeId,
                                                                        @RequestParam(name = "from") LocalDate from,
                                                                        @RequestParam(name = "to") LocalDate to) {
        List<Reservation> responses = reservationService.findReservationsByConditions(memberId, themeId, from, to);
        List<ReservationResponse> response = responses.stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    private void validateNull(Long value) {
        if (value == null) {
            throw new BadRequestException("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
        }
    }
}
