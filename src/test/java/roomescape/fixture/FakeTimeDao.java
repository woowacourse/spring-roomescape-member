package roomescape.fixture;

import org.springframework.dao.DuplicateKeyException;
import roomescape.dao.TimeDao;
import roomescape.dao.row.TimeRow;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static roomescape.fixture.FakeDatabase.generateTimeId;
import static roomescape.fixture.FakeDatabase.times;

public class FakeTimeDao implements TimeDao {
    @Override
    public List<TimeRow> findAll() {
        return List.copyOf(times.values());
    }

    @Override
    public Optional<TimeRow> findById(Long id) {
        return Optional.ofNullable(times.get(id));
    }

    @Override
    public TimeRow create(TimeRow time) {
        boolean duplicate = existsByStartAt(time.startAt());

        if (duplicate) {
            throw new DuplicateKeyException("uk_start_at");
        }

        Long id = generateTimeId();
        TimeRow newTime = new TimeRow(id, time.startAt());
        times.put(id, newTime);
        return newTime;
    }

    @Override
    public int delete(Long id) {
        TimeRow remove = times.remove(id);

        if (remove == null) {
            return 0;
        }

        return 1;
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        return times.values()
                .stream()
                .anyMatch(theme -> theme.startAt().equals(startAt));
    }

    @Override
    public boolean existsById(Long id) {
        return times.get(id) != null;
    }
}
