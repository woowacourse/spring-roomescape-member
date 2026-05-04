package roomescape.domain.time.repository;

import java.util.List;
import roomescape.domain.time.entity.Time;

public interface TimeRepository {

    Time save(Time time);

    List<Time> findAllTimes();

    Time findTimeById(Long id);

    void deleteTimeById(Long id);
}
