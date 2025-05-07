package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.dto.response.AvailableTimeResponse;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> fakeReservationTimes = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public FakeReservationTimeRepository(ReservationTime... reservationTimes) {
        Arrays.stream(reservationTimes)
                .forEach(reservationTime -> fakeReservationTimes.add(reservationTime));
    }

    @Override
    public List<ReservationTime> findAll() {
        return new ArrayList<>(fakeReservationTimes);
    }

    @Override
    public ReservationTime create(ReservationTime reservationTime) {
        ReservationTime reservationTimeWithId = new ReservationTime(index.getAndIncrement(),
                reservationTime.getStartAt());
        fakeReservationTimes.add(reservationTime);
        return reservationTimeWithId;
    }

    @Override
    public void delete(Long id) {
        fakeReservationTimes.removeIf(reservationTime -> reservationTime.getId().equals(id));
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return Optional.ofNullable(fakeReservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId().equals(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new));
    }

    @Override
    public List<AvailableTimeResponse> findByDateAndThemeIdWithBooked(LocalDate date, Long themeId) {
        return List.of();
    }
}
