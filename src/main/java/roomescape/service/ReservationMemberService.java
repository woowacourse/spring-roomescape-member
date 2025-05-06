package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.reservation.AddReservationDto;
import roomescape.exception.member.InvalidMemberException;
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

    public long addReservation(AddReservationDto newReservationDto, long memberId) {
        Optional<Member> member = memberService.findMemberById(memberId);
        if (member.isEmpty()) {
            throw new InvalidMemberException("유효하지 않은 유저의 예약 추가입니다");
        }
        return reservationService.addReservation(newReservationDto);
    }
}
