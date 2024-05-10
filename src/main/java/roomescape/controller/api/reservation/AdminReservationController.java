package roomescape.controller.api.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.AdminReservationAddRequest;
import roomescape.dto.request.ReservationAddMemberRequest;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;

    public AdminReservationController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody AdminReservationAddRequest request) { // TODO 테스트추가
        MemberResponse memberResponse = memberService.findById(request.memberId());
        ReservationAddMemberRequest memberRequest = new ReservationAddMemberRequest(memberResponse);
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(request.date(), request.timeId(), request.themeId());
        ReservationResponse addReservationResponse = reservationService.addReservation(reservationAddRequest, memberRequest);
        return ResponseEntity.created(URI.create("reservations/" + addReservationResponse.id()))
                .body(addReservationResponse);
    }

    @GetMapping
    public List<ReservationResponse> getReservations(
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ) {
        return reservationService.findReservationsByCondition(memberId, themeId, dateFrom, dateTo);
    }
}
