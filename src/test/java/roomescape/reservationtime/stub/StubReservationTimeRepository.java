package roomescape.reservationtime.stub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.AvailableReservationTimeResponse;
import roomescape.reservationtime.repository.ReservationTimeRepository;

public class StubReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> data = new ArrayList<>();

    public StubReservationTimeRepository(final ReservationTime... times) {
        data.addAll(List.of(times));
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        ReservationTime newTime = new ReservationTime(3L, reservationTime.getStartAt());
        data.add(newTime);
        return newTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return List.copyOf(data);
    }

    @Override
    public List<AvailableReservationTimeResponse> findAllAvailable(final LocalDate date, final Long themeId) {
        return List.of();
    }

    @Override
    public int deleteById(final Long id) {
        final boolean isDeleted = data.removeIf(time -> time.getId().equals(id));
        if (isDeleted) {
            return 1;
        }
        return 0;
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        return data.stream()
                .filter(time -> time.getId().equals(id))
                .findFirst();
    }
}
