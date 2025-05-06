package roomescape.repository.fake;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.model.TimeSlot;
import roomescape.repository.TimeSlotRepository;

public class TimeSlotFakeRepository implements TimeSlotRepository {

    private final Map<Long, TimeSlot> timeSlots = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1L);

    @Override
    public List<TimeSlot> findAll() {
        return List.copyOf(timeSlots.values());
    }

    @Override
    public Long save(final TimeSlot timeSlot) {
        var id = index.getAndIncrement();
        var created = new TimeSlot(id, timeSlot.startAt());
        timeSlots.put(id, created);
        return id;
    }

    @Override
    public Optional<TimeSlot> findById(final Long id) {
        return Optional.ofNullable(timeSlots.get(id));
    }

    @Override
    public Boolean removeById(final Long id) {
        TimeSlot removed = timeSlots.remove(id);
        return removed != null;
    }
}
