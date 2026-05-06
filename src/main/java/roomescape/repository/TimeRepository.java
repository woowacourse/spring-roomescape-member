package roomescape.repository;

import java.util.List;
import roomescape.domain.Time;

public interface TimeRepository {

    List<Time> findAll();

    Time findById(long id);

    Time save(Time time);

    void deleteById(long id);
}
