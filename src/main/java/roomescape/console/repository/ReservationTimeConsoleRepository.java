package roomescape.console.repository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.core.domain.ReservationTime;
import roomescape.core.repository.ReservationTimeRepository;

public class ReservationTimeConsoleRepository implements ReservationTimeRepository {
    public static final String TIME_FORMAT = "HH:mm";
    private final List<ReservationTime> reservationTimes;
    private final AtomicLong id;

    public ReservationTimeConsoleRepository() {
        reservationTimes = new ArrayList<>();
        id = new AtomicLong(1);
    }

    @Override
    public Long save(final ReservationTime reservationTime) {
        final ReservationTime persistReservationTime = new ReservationTime(
                id.getAndIncrement(),
                reservationTime.getStartAt().format(DateTimeFormatter.ofPattern(TIME_FORMAT))
        );
        reservationTimes.add(persistReservationTime);
        return persistReservationTime.getId();
    }

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public ReservationTime findById(final long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId() == id)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
    }

    @Override
    public boolean existByStartAt(final String startAt) {
        return false;
    }

    @Override
    public void deleteById(final long id) {
        reservationTimes.removeIf(reservationTime -> reservationTime.getId() == id);
    }
}
