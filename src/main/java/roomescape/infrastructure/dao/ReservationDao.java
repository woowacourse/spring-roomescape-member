package roomescape.infrastructure.dao;

import roomescape.application.dto.ReservationInfoResponse;
import roomescape.domain.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {

    List<ReservationInfoResponse> findAll();

    List<ReservationInfoResponse> findByThemeIdAndMemberIdAndDate(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo);

    Reservation save(Reservation reservation);

    int deleteById(Long id);

    boolean existByTimeId(Long timeId);

    boolean existByThemeId(Long themeId);

    boolean existByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date);

    List<Long> findBookedTimes(final Long themeId, final LocalDate date);
}
