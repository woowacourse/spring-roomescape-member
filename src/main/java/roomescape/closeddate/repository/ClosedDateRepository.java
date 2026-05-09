package roomescape.closeddate.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.closeddate.domain.ClosedDate;

@Repository
public interface ClosedDateRepository {

    Optional<ClosedDate> findById(Long id);

    List<ClosedDate> findAll();

    ClosedDate save(ClosedDate reservationDate);

    boolean delete(Long id);

    boolean existsByDate(LocalDate date);

}
