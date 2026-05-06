package roomescape.fake;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final Map<Long, ReservationTime> times = new HashMap<>();
    private Long idHoler = 1L;

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return Optional.of(times.get(id));
    }

    @Override
    public List<ReservationTime> findAll() {
        return List.of();
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime savedTime = reservationTime.withId(idHoler);
        times.put(idHoler++, savedTime);
        return savedTime;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<ReservationTime> findByThemeAndDate(Long themeId, LocalDate date) {
        return List.of();
    }
}
