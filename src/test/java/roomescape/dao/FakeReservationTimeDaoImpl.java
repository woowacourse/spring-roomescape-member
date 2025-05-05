package roomescape.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookedReservationTimeResponseDto;

public class FakeReservationTimeDaoImpl implements ReservationTimeDao {

    private final AtomicLong index = new AtomicLong(1);
    private final List<ReservationTime> reservationTimes = new ArrayList<>();

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public long saveReservationTime(ReservationTime reservationTime) {
        reservationTime.setId(index.getAndIncrement());
        reservationTimes.add(reservationTime);
        return index.longValue();
    }

    @Override
    public void deleteReservationTime(Long id) {
        ReservationTime reservationTime = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약시간입니다."));
        reservationTimes.remove(reservationTime);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
            .filter(reservationTime -> reservationTime.getId().equals(id))
            .findFirst();
    }

    @Override
    public List<BookedReservationTimeResponseDto> findBookedReservationTime(String date,
        Long themeId) {
        return List.of();
    }
}
