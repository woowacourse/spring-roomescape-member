package roomescape.admin.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.admin.dto.AdminReservationRequestDto;
import roomescape.member.service.MemberService;
import roomescape.reservation.dto.ReservationResponseDto;
import roomescape.reservation.service.ReservationService;

@Validated
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;
    private final MemberService memberService;

    public AdminController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> registerReservation(
            @RequestBody @Valid final AdminReservationRequestDto request) {
        ReservationResponseDto responseDto = new ReservationResponseDto(
                reservationService.save(
                        memberService.findById(request.memberId()),
                        request.toReservationRequestDto()));
        return ResponseEntity.created(URI.create("reservations/" + responseDto.id())).body(responseDto);
    }

}
