package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import roomescape.reservation.dto.response.AvailableTimeResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.Time;

public class FakeTimeRepository implements TimeRepository {

    private final List<Time> times = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public Time save(Time time) {
        times.add(time);
        return time;
    }

    @Override
    public List<Time> findAll() {
        return Collections.unmodifiableList(times);
    }

    @Override
    public boolean deleteById(Long id) {
        return times.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public Optional<Time> findById(Long id) {
        return times.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, Long themeId) {
        Set<Long> reservedTimeIds = reservations.stream()
                .filter(r -> r.getDate().isEqual(date) && r.getThemeId().equals(themeId))
                .map(Reservation::getTimeId)
                .collect(Collectors.toSet());

        return times.stream()
                .filter(Time::isAvailable)
                .map(time -> new AvailableTimeResponse(
                        time.getId(),
                        time.getFormattedTime(),
                        reservedTimeIds.contains(time.getId())
                ))
                .toList();
    }
}
