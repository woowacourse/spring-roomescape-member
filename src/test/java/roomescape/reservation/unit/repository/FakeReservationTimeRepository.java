package roomescape.reservation.unit.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import roomescape.reservation.dto.response.ReservationTimeResponse.AvailableReservationTimeResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> times = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public ReservationTime save(ReservationTime time) {
        times.add(time);
        return time;
    }

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(times);
    }

    @Override
    public boolean deleteById(Long id) {
        return times.removeIf(entity -> entity.getId().equals(id));
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return times.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<AvailableReservationTimeResponse> findAvailableTimes(LocalDate date, Long themeId) {
        Set<Long> reservedTimeIds = reservations.stream()
                .filter(r -> r.getDate().isEqual(date) && r.getThemeId().equals(themeId))
                .map(reservation -> reservation.getTime().getId())
                .collect(Collectors.toSet());

        return times.stream()
                .filter(ReservationTime::isAvailable)
                .map(time -> new AvailableReservationTimeResponse(
                        time.getId(),
                        time.getFormattedTime(),
                        reservedTimeIds.contains(time.getId())
                ))
                .toList();
    }
}
