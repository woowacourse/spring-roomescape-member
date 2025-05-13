package roomescape.domain.repository;

import roomescape.application.dto.ReservationInfoResponse;
import roomescape.domain.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<ReservationInfoResponse> findAll();

    int deleteById(Long id);

    boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    List<Long> findBookedTimes(Long themeId, LocalDate date);

    boolean existByThemeId(Long themeId);

    boolean existByTimeId(Long timeId);

    List<ReservationInfoResponse> findByThemeIdAndMemberIdAndDate(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo);
}
