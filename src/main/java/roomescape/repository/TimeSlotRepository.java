package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.model.TimeSlot;

public interface TimeSlotRepository {

    List<TimeSlot> findAll();

    Long save(final TimeSlot timeSlot);

    Optional<TimeSlot> findById(final Long id);

    Boolean removeById(final Long id);
}
