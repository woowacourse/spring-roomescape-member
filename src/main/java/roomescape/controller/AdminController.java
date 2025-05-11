package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.UserReservationRequest;
import roomescape.model.user.UserName;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;

@RestController
public class AdminController {
    private final ReservationService reservationService;
    private final MemberService memberService;

    public AdminController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<UserName> reservation(@RequestBody AdminReservationRequest request) {
        UserName userName = memberService.getUserNameByUserId(request.userId());
        reservationService.addReservation(userName,
                UserReservationRequest.of(request.date(), request.timeId(), request.themeId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userName);
    }
}
