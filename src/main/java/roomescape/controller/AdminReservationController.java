package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Reservation;
import roomescape.dto.AdminReservationSaveRequest;
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

@RequestMapping("/admin/reservations")
@RestController
public class AdminReservationController {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public AdminReservationController(
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
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody final AdminReservationSaveRequest request) {
        final MemberResponse memberResponse = memberService.findById(request.memberId());
        final ReservationTimeResponse reservationTimeResponse = reservationTimeService.findById(request.timeId());
        final ThemeResponse themeResponse = themeService.findById(request.themeId());

        final Reservation reservation = request.toModel(memberResponse, themeResponse, reservationTimeResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(reservation));
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
}
