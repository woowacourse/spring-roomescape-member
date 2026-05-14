package roomescape.fixture;

import roomescape.dao.ReservationQueryDao;
import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ThemeRow;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static roomescape.fixture.FakeDatabase.*;
import static roomescape.fixture.FakeDatabase.reservations;

public class FakeReservationQueryDao implements ReservationQueryDao {
    public void clear() {
        clearAll();
    }

    @Override
    public List<AvailableTimeRow> findAvailableTimesById(Long themeId, LocalDate localDate) {
        return times.values().stream()
                .map(time -> {
                    boolean alreadyBooked = reservations.values().stream()
                            .anyMatch(r -> r.timeRow().id().equals(time.id())
                                    && r.themeRow().id().equals(themeId)
                                    && r.date().equals(localDate));

                    return new AvailableTimeRow(time.id(), time.startAt(), alreadyBooked);
                })
                .sorted(Comparator.comparing(AvailableTimeRow::startAt))
                .toList();
    }

    @Override
    public List<ThemeRow> findPopulars(int limit, int days, LocalDate date) {
        LocalDate startDate = date.minusDays(days);

        Map<Long, Long> countByTheme =  reservations.values().stream()
                .filter(r -> !r.date().isBefore(startDate) && !r.date().isAfter(date))
                .collect(Collectors.groupingBy(
                        r -> r.themeRow().id(),
                        Collectors.counting()
                ));

        return FakeDatabase.themes.values().stream()
                .sorted(Comparator.comparingLong(
                        (ThemeRow t) -> countByTheme.getOrDefault(t.id(), 0L)
                ).reversed())
                .limit(limit)
                .toList();
    }
}
