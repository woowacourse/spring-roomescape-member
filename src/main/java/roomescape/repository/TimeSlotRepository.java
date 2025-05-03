package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.model.TimeSlot;

public interface TimeSlotRepository {

    Optional<TimeSlot> findById(final long id);

    long save(final TimeSlot timeSlot);

    boolean removeById(final long id);

    List<TimeSlot> findAll();
}
