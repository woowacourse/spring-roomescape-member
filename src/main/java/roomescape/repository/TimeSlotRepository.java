package roomescape.repository;

import roomescape.domain.TimeSlot;

import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository {

    List<TimeSlot> findAll();

    Optional<TimeSlot> findById(long id);

    TimeSlot save(TimeSlot timeSlot);

    void deleteById(long id);

    int update(TimeSlot timeSlot);
}
