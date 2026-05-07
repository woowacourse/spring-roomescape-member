package roomescape.repository;

import java.util.List;
import roomescape.domain.TimeSlot;

public interface TimeRepository {

    List<TimeSlot> findAll();

    TimeSlot findById(long id);

    TimeSlot save(TimeSlot timeSlot);

    void deleteById(long id);
}
