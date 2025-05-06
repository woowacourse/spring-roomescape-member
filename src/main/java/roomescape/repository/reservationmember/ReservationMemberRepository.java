package roomescape.repository.reservationmember;

import java.util.List;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationmember.ReservationMemberIds;

public interface ReservationMemberRepository {

    List<ReservationMemberIds> findAll();

    long add(Reservation reservation, Member member);

    void deleteById(long id);
}
