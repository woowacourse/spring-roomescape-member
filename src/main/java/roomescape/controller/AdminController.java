package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.UserReservationRequest;
import roomescape.model.Reservation;
import roomescape.model.user.Member;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminController {
    private final ReservationService reservationService;
    private final MemberService memberService;

    public AdminController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Reservation> addReservation(@RequestBody AdminReservationRequest request) {

        Member member = memberService.getMemberById(request.userId());
        Reservation reservation = reservationService.addReservation(member,
                UserReservationRequest.of(request.date(), request.timeId(), request.themeId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/filter")
    public List<Reservation> filterReservation(
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo) {
        return reservationService.getFilteredReservation(themeId, memberId, dateFrom, dateTo);
    }
}
