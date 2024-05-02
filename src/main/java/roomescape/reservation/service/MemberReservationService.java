package roomescape.reservation.service;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.dto.MemberReservationRequest;

@Service
public class MemberReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    public MemberReservationService(final ReservationRepository reservationRepository,
                                    final MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
    }

    public void create(MemberReservationRequest reservationRequest) {
        Member member = memberRepository.save(new Member(reservationRequest.name()));
        Reservation reservation = reservationRepository.findBy(LocalDate.parse(reservationRequest.date()),
                        reservationRequest.timeId(),
                        reservationRequest.themeId())
                .orElseThrow(IllegalAccessError::new);
        reservationRepository.saveReservationList(member.getId(), reservation.getId());
    }
}
