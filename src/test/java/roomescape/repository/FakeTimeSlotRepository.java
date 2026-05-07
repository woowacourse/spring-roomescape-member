package roomescape.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.domain.TimeSlot;

public class FakeTimeSlotRepository implements TimeSlotRepository {

    private final Map<Long, TimeSlot> storage = new HashMap<>();
    private long sequence = 1L;

    @Override
    public List<TimeSlot> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public TimeSlot findById(long id) {
        return storage.get(id);
    }

    @Override
    public TimeSlot save(TimeSlot timeSlot) {
        long id = sequence++;
        TimeSlot savedTimeSlot = new TimeSlot(id, timeSlot.startAt());
        storage.put(id, savedTimeSlot);
        return savedTimeSlot;
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }
}
