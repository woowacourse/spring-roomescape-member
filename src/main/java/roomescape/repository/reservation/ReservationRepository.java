package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.entity.member.Member;
import roomescape.entity.reservation.Reservation;
import roomescape.entity.reservation.ReservationTime;
import roomescape.entity.reservation.Theme;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(long id);

    List<Long> findBookedTimeIdsByDateAndThemeId(LocalDate date, Long themeId);

    boolean existsByDateAndTimeAndTheme(LocalDate date, ReservationTime time, Theme theme, Member member);

}
