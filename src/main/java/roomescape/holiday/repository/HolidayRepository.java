package roomescape.holiday.repository;

import roomescape.holiday.domain.Holiday;

import java.util.List;

public interface HolidayRepository {
    Holiday save(Holiday holiday);
    List<Holiday> findAll();
    boolean deleteById(Long id);
}
