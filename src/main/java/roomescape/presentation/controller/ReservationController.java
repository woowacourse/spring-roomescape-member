package roomescape.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.application.service.ReservationService;
import roomescape.domain.model.Member;
import roomescape.presentation.annotation.MemberAuthorization;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.ReservationResponse;

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
    public ResponseEntity<List<ReservationResponse>> getAll() {
        List<ReservationResponse> response = reservationService.findAll();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponse>> getByConditions(
            @RequestParam("themeId") Long themeId,
            @RequestParam("memberId") Long memberId,
            @RequestParam("dateFrom") LocalDate dateFrom,
            @RequestParam("dateTo") LocalDate dateTo
    ) {
        List<ReservationResponse> response = reservationService.findByThemeIdAndMemberIdAndDate(themeId, memberId, dateFrom, dateTo);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@MemberAuthorization Member member, @Validated @RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.save(member, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
