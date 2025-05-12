package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(long id);

    List<Long> findBookedTimeIdsByDateAndThemeId(LocalDate date, Long themeId);

    boolean existsByDateAndTimeAndTheme(LocalDate date, ReservationTime time, Theme theme, Member member);

}
