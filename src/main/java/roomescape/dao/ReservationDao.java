package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

@Repository
public interface ReservationDao {

    List<Reservation> readAll();

    List<Reservation> readAllByMemberAndThemeAndDateBetweenFromAndTo(Member member, Theme theme,
                                                                     ReservationDate from, ReservationDate to);

    Optional<Reservation> readById(Long id);

    List<ReservationTime> readTimesByDateAndThemeId(ReservationDate reservationDate, Long themeId);

    List<Theme> readPopularThemes(LocalDate startDate, LocalDate endDate);

    Reservation create(Reservation reservation);

    boolean hasSame(Reservation reservation);

    void delete(Reservation reservation);

    boolean existByTimeId(Long timeId);

    boolean existByThemeId(Long id);
}
