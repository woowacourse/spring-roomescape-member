package roomescape.unit.repository.reservationmember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationmember.ReservationMemberIds;
import roomescape.repository.reservationmember.ReservationMemberRepository;

public class FakeReservationMemberRepository implements ReservationMemberRepository {

    private final AtomicLong index = new AtomicLong(1L);
    private final List<ReservationMemberIds> reservationMemberIds = new ArrayList<>();

    @Override
    public List<ReservationMemberIds> findAll() {
        return Collections.unmodifiableList(reservationMemberIds);
    }

    @Override
    public long add(Reservation reservation, Member member) {
        long id = index.getAndIncrement();
        reservationMemberIds.add(new ReservationMemberIds(id, reservation.getId(), member.getId()));
        return id;
    }
}
