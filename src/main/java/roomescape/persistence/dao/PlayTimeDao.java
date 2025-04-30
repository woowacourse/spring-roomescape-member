package roomescape.persistence.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.business.domain.PlayTime;

public interface PlayTimeDao {

    Long save(PlayTime playTime);

    Optional<PlayTime> find(Long id);

    List<PlayTime> findAll();

    boolean remove(Long id);

    boolean existsByStartAt(LocalTime startAt);
}
