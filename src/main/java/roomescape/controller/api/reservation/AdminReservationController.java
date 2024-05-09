package roomescape.controller.api.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.AdminReservationAddRequest;
import roomescape.dto.request.ReservationAddMemberRequest;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;

import java.net.URI;
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
        Optional<MemberResponse> optionalMemberResponse = memberService.findById(request.memberId()); // TODO Optional 로 받는게 맞을까?? 그냥 예외를 터트리면 안될까??
        if (optionalMemberResponse.isEmpty()) {
            throw new IllegalArgumentException("요청이 잘못되었습니다. 유저 id를 확인해주세요.");
        }
        MemberResponse memberResponse = optionalMemberResponse.get();
        ReservationAddMemberRequest memberRequest = new ReservationAddMemberRequest(memberResponse);
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(request.date(), request.timeId(), request.themeId());
        ReservationResponse addReservationResponse = reservationService.addReservation(reservationAddRequest, memberRequest);
        return ResponseEntity.created(URI.create("reservations/" + addReservationResponse.id()))
                .body(addReservationResponse);
    }
}
