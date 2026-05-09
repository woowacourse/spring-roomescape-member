package roomescape.fixture.fake;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.repository.AvailableReservationTime;
import roomescape.reservationtime.domain.repository.AvailableTimeRepository;
import roomescape.reservationtime.domain.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository, AvailableTimeRepository {

    private final Map<Long, ReservationTime> times = new LinkedHashMap<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private Long idHolder = 1L;

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return Optional.ofNullable(times.get(id));
    }

    @Override
    public List<ReservationTime> findAll() {
        return times.values().stream()
                .toList();
    }

    @Override
    public ReservationTime save(ReservationTime time) {
        ReservationTime savedTime = time.withId(idHolder);
        times.put(idHolder++, savedTime);
        return savedTime;
    }

    @Override
    public Integer delete(Long id) {
        int beforeSize = times.size();
        times.remove(id);
        int afterSize = times.size();

        if (beforeSize != afterSize) {
            return 1;
        }

        return 0;
    }

    @Override
    public List<AvailableReservationTime> findByThemeAndDate(Long themeId, LocalDate date) {
        Set<Long> reservedTimeIds = reservations.stream()
                .filter(reservation -> reservation.getThemeId().equals(themeId))
                .filter(reservation -> reservation.getDate().equals(date))
                .map(Reservation::getTimeId)
                .collect(Collectors.toSet());

        return times.values().stream()
                .map(time -> new AvailableReservationTime(
                        time.getId(),
                        time.getStartAt(),
                        !reservedTimeIds.contains(time.getId())
                ))
                .toList();
    }

    @Override
    public Boolean existsByStartAt(LocalTime startAt) {
        return times.values().stream()
                .anyMatch(savedTimes -> savedTimes.getStartAt().equals(startAt));
    }

    public void saveReservation(Reservation reservation) {
        reservations.add(reservation);
    }
}
