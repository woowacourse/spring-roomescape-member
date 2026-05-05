package roomescape.dao;


import roomescape.domain.Time;

import java.util.List;
import java.util.Optional;

public interface TimeDao {
    Long insert(Time time);

    Optional<Time> findById(Long id);

    List<Time> findAll();

    int delete(Long id);
}
