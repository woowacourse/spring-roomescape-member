package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.config.AuthenticationPrincipal;
import roomescape.domain.Reservation;
import roomescape.dto.MemberLoginResponse;
import roomescape.dto.MemberReservationSaveRequest;
import roomescape.dto.MemberResponse;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationController(
            final MemberService memberService,
            final ReservationService reservationService,
            final ReservationTimeService reservationTimeService,
            final ThemeService themeService)
    {
        this.memberService = memberService;
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @AuthenticationPrincipal MemberLoginResponse loginResponse,
            @RequestBody final MemberReservationSaveRequest request) {
        final MemberResponse memberResponse = memberService.findById(loginResponse.id());
        final ReservationTimeResponse reservationTimeResponse = reservationTimeService.findById(request.timeId());
        final ThemeResponse themeResponse = themeService.findById(request.themeId());

        final Reservation reservation = request.toModel(memberResponse, themeResponse, reservationTimeResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(reservation));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findReservations () {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping(params = {"themeId", "memberId", "dateFrom", "dateTo"})
    public ResponseEntity<List<ReservationResponse>> findReservationsByThemeAndMemberAndPeriod (
            @RequestParam(required = false) final Long themeId,
            @RequestParam(required = false) final Long memberId,
            @RequestParam(required = false) final LocalDate dateFrom,
            @RequestParam(required = false) final LocalDate dateTo
    ) {
        return ResponseEntity.ok(reservationService.findAllByThemeAndMemberAndPeriod(themeId, memberId, dateFrom, dateTo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable final Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
