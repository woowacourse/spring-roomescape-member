package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.auth.UserInfo;
import roomescape.domain.member.Member;
import roomescape.dto.reservation.AddReservationDto;
import roomescape.service.member.MemberService;
import roomescape.service.reservation.ReservationService;

@Service
public class ReservationMemberService {

    private final MemberService memberService;
    private final ReservationService reservationService;

    public ReservationMemberService(MemberService memberService, ReservationService reservationService) {
        this.memberService = memberService;
        this.reservationService = reservationService;
    }

    public long addReservation(AddReservationDto newReservationDto, UserInfo userInfo) {
        Member member = memberService.getMemberById(userInfo.id());
        return reservationService.addReservation(newReservationDto, member.getId());
    }
}
