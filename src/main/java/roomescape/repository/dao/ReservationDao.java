package roomescape.repository.dao;

import roomescape.model.Reservation;
import roomescape.repository.dto.ReservationSavedDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    long save(Reservation reservation);

    List<ReservationSavedDto> findAll();

    Optional<ReservationSavedDto> findById(long id);

    List<ReservationSavedDto> findByDateAndThemeId(LocalDate date, long themeId);

    List<Long> findByDateAndGroupByThemeIdAndOrderByCountAndLimit(LocalDate startDate, LocalDate endDate, int limit);
    // TODO: decide naming
    void deleteById(long id);

    Boolean isExistById(long id);

    Boolean isExistByTimeId(long timeId);

    Boolean isExistByDateAndTimeId(LocalDate date, long timeId);
}
