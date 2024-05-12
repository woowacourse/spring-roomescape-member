package roomescape.controller;

import jakarta.validation.Valid;
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
    public ResponseEntity<ReservationResponse> addReservation(@Valid @RequestBody ReservationRequest request, LoginMember member) {
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

    @GetMapping(value = "/times", params = {"date", "themeId"})
    public ResponseEntity<List<ReservationTimeInfoResponse>> showReservationTimesInformation(LocalDate date, Long themeId) {
        validateNull(themeId);
        ReservationTimeInfoDto timesInfo = reservationService.findReservationTimesInformation(date, themeId);
        List<ReservationTimeInfoResponse> response = ReservationTimeInfoResponse.from(timesInfo);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/filter", params = {"memberId", "themeId", "from", "to"})
    public ResponseEntity<List<ReservationResponse>> searchReservations(Long memberId, Long themeId,
                                                                        LocalDate from, LocalDate to) {
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
