package roomescape.reservation.presentation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.application.MemberService;
import roomescape.member.domain.Member;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.application.ReservationTimeService;
import roomescape.reservation.application.ThemeService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.AdminReservationSaveRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.response.ReservationTimeResponse;
import roomescape.reservation.dto.response.ThemeResponse;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;
    private final MemberService memberService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public AdminReservationController(ReservationService reservationService, MemberService memberService,
                                      ReservationTimeService reservationTimeService, ThemeService themeService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid AdminReservationSaveRequest request) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.findById(request.timeId());
        ThemeResponse themeResponse = themeService.findById(request.themeId());
        Member member = memberService.findById(request.memberId());
        Reservation reservation = request.toModel(themeResponse, reservationTimeResponse, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(reservation));
    }
}
