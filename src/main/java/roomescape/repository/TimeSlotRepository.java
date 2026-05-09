package roomescape.repository;

import java.util.List;
import java.util.Optional;

import roomescape.domain.TimeSlot;

public interface TimeSlotRepository {

    List<TimeSlot> findAll();

    Optional<TimeSlot> findById(long id);

    TimeSlot save(TimeSlot timeSlot);

    void deleteById(long id);
}
