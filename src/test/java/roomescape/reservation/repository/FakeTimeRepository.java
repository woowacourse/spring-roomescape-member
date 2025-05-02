package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.dto.response.AvailableTimeResponse;
import roomescape.reservation.entity.Time;

public class FakeTimeRepository implements TimeRepository {

    private final List<Time> times = new ArrayList<>();

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
        // TODO: 테스트용 메서드 로직 작성
        return null;
    }
}
