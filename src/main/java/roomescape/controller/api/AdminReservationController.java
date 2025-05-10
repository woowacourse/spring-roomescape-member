package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.dto.reservation.AdminReservationCreateRequestDto;
import roomescape.dto.reservation.ReservationResponseDto;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationCreateDto;

import java.net.URI;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final MemberService memberService;
    private final ReservationService reservationService;

    public AdminReservationController(MemberService memberService, ReservationService reservationService) {
        this.memberService = memberService;
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> addReservation(@RequestBody AdminReservationCreateRequestDto requestDto) {
        Member memberById = memberService.findMemberById(requestDto.memberId());
        ReservationCreateDto createDto = new ReservationCreateDto(memberById.getName(), requestDto.date(), requestDto.timeId(), requestDto.themeId());
        ReservationResponseDto responseDto = reservationService.createReservation(createDto);
        return ResponseEntity.created(URI.create("reservations/" + responseDto.id())).body(responseDto);
    }
}
