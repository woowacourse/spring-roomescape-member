package roomescape.repository;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    List<Reservation> findAll();

    // TODO: [3단계] 5. findAllByDateAndThemeId 만들어서 해당 날짜와 테마에 대해 예약된 모든 정보 가져오기

    Optional<Reservation> findById(Long id);

    boolean existsByTimeId(Long timeId);

    boolean existsByDateAndTimeId(Long timeId, LocalDate date);

    boolean existsByThemeId(Long themeId);

    Reservation save(Reservation reservation);

    int deleteById(Long id);
}
