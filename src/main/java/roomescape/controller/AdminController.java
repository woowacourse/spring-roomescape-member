package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.UserReservationRequest;
import roomescape.model.Reservation;
import roomescape.model.user.Name;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class AdminController {
    private final ReservationService reservationService;
    private final MemberService memberService;

    public AdminController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<Name> addReservation(@RequestBody AdminReservationRequest request) {
        Name name = memberService.getUserNameByUserId(request.userId());
        reservationService.addReservation(name,
                UserReservationRequest.of(request.date(), request.timeId(), request.themeId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(name);
    }

    @GetMapping("/admin/reservations/filter")
    public List<Reservation> filterReservation(
            @RequestParam Long themeId,
            @RequestParam Long memberId,
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo)
    {
        return reservationService.getFilteredReservation(themeId, memberId, dateFrom, dateTo);
    }
}
