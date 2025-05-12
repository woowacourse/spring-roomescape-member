package roomescape.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.business.ReservationTime;
import roomescape.presentation.dto.response.AvailableTimesResponseDto;

public final class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> reservationTimes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes;
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
                .filter(time -> Objects.equals(time.getId(), id))
                .findFirst();
    }

    @Override
    public Long add(ReservationTime reservationTime) {
        ReservationTime savedReservationTime = new ReservationTime(
                idGenerator.getAndIncrement(),
                reservationTime.getStartAt()
        );
        reservationTimes.add(savedReservationTime);
        return savedReservationTime.getId();
    }

    @Override
    public void deleteById(Long id) {
        reservationTimes.removeIf(reservationTime -> Objects.equals(reservationTime.getId(), id));
    }

    @Override
    public List<AvailableTimesResponseDto> findAvailableTimes(LocalDate date, Long themeId) {
        return List.of();
    }
}
