package roomescape.domain;

import roomescape.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<ReservationResponse> findAll();

    int deleteById(Long id);

    boolean existByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date);

    List<Long> findBookedTimes(Long themeId, LocalDate date);

    boolean existByThemeId(Long themeId);

    boolean existByTimeId(Long timeId);
}
