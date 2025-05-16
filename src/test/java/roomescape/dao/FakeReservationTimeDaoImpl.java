package roomescape.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.reservationtime.dao.ReservationTimeDao;
import roomescape.domain.reservationtime.model.ReservationTime;

public class FakeReservationTimeDaoImpl implements ReservationTimeDao {

    private final AtomicLong index = new AtomicLong(1);
    private final List<ReservationTime> reservationTimes = new ArrayList<>();

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public long save(ReservationTime reservationTime) {
        reservationTimes.add(reservationTime);
        return index.getAndIncrement();
    }

    @Override
    public boolean delete(Long id) {
        ReservationTime reservationTime = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약시간입니다."));
        return reservationTimes.remove(reservationTime);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
            .filter(reservationTime -> reservationTime.getId().equals(id))
            .findFirst();
    }

    /***
     * 현 시점에서는 포워딩 역할만 하기에, 따로 테스트 코드를 작성하지 않았음
     */
    @Override
    public List<ReservationTime> findBookedTimes(String date, Long themeId) {
        return List.of();
    }
}
