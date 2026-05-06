package roomescape.holiday.repository;

import org.springframework.stereotype.Repository;
import roomescape.holiday.domain.Holiday;

import java.util.List;

@Repository
public class JdbcHolidayRepository implements HolidayRepository{

    @Override
    public Holiday save(Holiday holiday) {
        return null;
    }

    @Override
    public List<Holiday> findAll() {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
