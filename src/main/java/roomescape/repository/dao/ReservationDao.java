package roomescape.repository.dao;

import roomescape.repository.dto.ReservationSavedDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    long save(ReservationSavedDto reservationSavedDto);

    List<ReservationSavedDto> findAll();

    Optional<ReservationSavedDto> findById(long id);

    List<ReservationSavedDto> findByDateAndThemeId(LocalDate date, long themeId);

    List<Long> findThemeIdByDateAndOrderByThemeIdCountAndLimit(LocalDate startDate, LocalDate endDate, int limit);

    void deleteById(long id);

    Boolean isExistById(long id);

    Boolean isExistByTimeId(long timeId);

    Boolean isExistByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId);

    List<ReservationSavedDto> findByMemberIdAndThemeIdAndDate(long memberId, long themeId, LocalDate from, LocalDate to);
}
