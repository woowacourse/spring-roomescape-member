package roomescape.service.reservationmember;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationmember.ReservationMember;
import roomescape.domain.reservationmember.ReservationMemberIds;
import roomescape.dto.reservation.AddReservationDto;
import roomescape.repository.reservationmember.ReservationMemberRepository;
import roomescape.service.member.MemberService;
import roomescape.service.reservation.ReservationService;

@Service
public class ReservationMemberService {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final ReservationMemberRepository reservationMemberRepository;

    public ReservationMemberService(MemberService memberService, ReservationService reservationService,
                                    ReservationMemberRepository reservationMemberRepository) {
        this.memberService = memberService;
        this.reservationService = reservationService;
        this.reservationMemberRepository = reservationMemberRepository;
    }

    public long addReservation(AddReservationDto newReservationDto, long memberId) {
        Member member = memberService.getMemberById(memberId);
        long reservationId = reservationService.addReservation(newReservationDto);
        Reservation reservation = reservationService.getReservationById(reservationId);
        return reservationMemberRepository.add(reservation, member);
    }

    public List<ReservationMember> allReservations() {
        List<ReservationMemberIds> reservationMembers = reservationMemberRepository.findAll();
        return reservationMembers.stream()
                .map(reservationMemberIds -> {
                    Reservation reservation = reservationService.getReservationById(
                            reservationMemberIds.getReservationId());
                    Member member = memberService.getMemberById(reservationMemberIds.getMemberId());
                    return new ReservationMember(reservation, member);
                })
                .toList();
    }

    public void deleteReservation(long id) {
        reservationMemberRepository.deleteById(id);
    }
}
