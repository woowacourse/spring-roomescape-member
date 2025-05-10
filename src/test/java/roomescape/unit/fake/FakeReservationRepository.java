package roomescape.unit.fake;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberEncodedPassword;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationDateTime;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;
import roomescape.domain.time.ReservationTime;
import roomescape.repository.DynamicReservationSelectQuery;
import roomescape.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {
    private final List<Reservation> values = new ArrayList<>();
    private final AtomicLong increment = new AtomicLong(1);

    @Override
    public Reservation save(Member member, ReservationDateTime reservationDateTime, Theme theme) {
        Reservation reservation = new Reservation(
                increment.getAndIncrement(),
                member,
                reservationDateTime.getReservationDate(),
                reservationDateTime.getReservationTime(),
                theme
        );
        values.add(reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return values.stream().filter(reservation -> reservation.getId().equals(id)).findFirst();
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public void deleteById(Long id) {
        values.removeIf(reservation -> reservation.getId().equals(id));
    }

    @Override
    public boolean existSameDateTime(ReservationDate reservationDate, Long timeId) {
        return values.stream().anyMatch(reservation ->
                reservation.getReservationDate().equals(reservationDate) &&
                        reservation.getReservationTime().getId().equals(timeId)
        );
    }

    @Override
    public boolean existReservationByTimeId(Long timeId) {
        return values.stream().anyMatch(reservation -> reservation.getReservationTime().getId().equals(timeId));
    }

    @Override
    public boolean existReservationByThemeId(Long themeId) {
        return values.stream().anyMatch(reservation -> reservation.getTheme().getId().equals(themeId));
    }

    @Override
    public List<Reservation> findAllWithCondition(Long memberId, Long themeId, LocalDate fromDate, LocalDate toDate) {
        return findAll();
    }
}
