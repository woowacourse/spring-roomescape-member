package roomescape.persistence.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.business.domain.PlayTime;

public interface PlayTimeDao {

    Long insert(PlayTime playTime);

    Optional<PlayTime> findById(Long id);

    List<PlayTime> findAll();

    boolean deleteById(Long id);

    boolean existsByStartAt(LocalTime startAt);

    boolean existsById(Long timeId);
}
