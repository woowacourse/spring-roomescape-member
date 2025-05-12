package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.auth.domain.Role;
import roomescape.member.entity.MemberEntity;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.reservationtime.entity.ReservationTimeEntity;
import roomescape.theme.entity.ThemeEntity;

@Repository
public class JDBCReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JDBCReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> getAll() {
        return jdbcTemplate.query(
                "SELECT " +
                        "r.id as reservation_id, " +
                        "r.date, " +
                        "t.id as time_id, " +
                        "t.start_at as time_value, " +
                        "th.id as theme_id, " +
                        "th.name as theme_name, " +
                        "th.description, " +
                        "th.thumbnail, " +
                        "m.id as member_id, " +
                        "m.name as member_name, " +
                        "m.email as member_email, " +
                        "m.password as member_password, " +
                        "m.role as member_role " +
                        "FROM reservation as r " +
                        "INNER JOIN reservation_time as t ON r.time_id = t.id " +
                        "INNER JOIN theme as th ON r.theme_id = th.id " +
                        "INNER JOIN member as m ON r.member_id = m.id",
                (resultSet, rowNum) -> {
                    ReservationTimeEntity timeEntity = new ReservationTimeEntity(
                            resultSet.getLong("time_id"),
                            resultSet.getString("time_value")
                    );

                    ThemeEntity themeEntity = new ThemeEntity(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    );

                    MemberEntity memberEntity = new MemberEntity(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member_name"),
                            resultSet.getString("member_email"),
                            resultSet.getString("member_password"),
                            Role.valueOf(resultSet.getString("member_role"))
                    );

                    ReservationEntity entity = new ReservationEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("date"),
                            memberEntity,
                            timeEntity,
                            themeEntity
                    );

                    return entity.toReservation();
                }
        );
    }

    @Override
    public Reservation put(final Reservation reservation) {
        long generatedId = simpleJdbcInsert.executeAndReturnKey(
                Map.of("date", reservation.getDate(), "member_id", reservation.getMember().getId(), "time_id",
                        reservation.getTime().getId(), "theme_id", reservation.getTheme().getId())
        ).longValue();

        return Reservation.of(generatedId, reservation.getDate(), reservation.getMember(),
                reservation.getTime(), reservation.getTheme());
    }

    @Override
    public boolean deleteById(final Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id) != 0;
    }

    @Override
    public boolean existsByTimeId(final Long id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)",
                Boolean.class,
                id
        ));
    }

    @Override
    public boolean existsByThemeId(final Long id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)",
                Boolean.class,
                id
        ));
    }

    @Override
    public boolean existsByDateAndTimeId(final LocalDate date, final long timeId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM reservation WHERE (date, time_id) = (?,?))",
                Boolean.class,
                date,
                timeId
        ));
    }

    @Override
    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themeId) {
        return jdbcTemplate.query(
                "SELECT " +
                        "r.id as reservation_id, " +
                        "r.date, " +
                        "t.id as time_id, " +
                        "t.start_at as time_value, " +
                        "th.id as theme_id, " +
                        "th.name as theme_name, " +
                        "th.description, " +
                        "th.thumbnail, " +
                        "m.id as member_id, " +
                        "m.name as member_name, " +
                        "m.email as member_email, " +
                        "m.password as member_password, " +
                        "m.role as member_role " +
                        "FROM reservation as r " +
                        "INNER JOIN reservation_time as t ON r.time_id = t.id " +
                        "INNER JOIN theme as th ON r.theme_id = th.id " +
                        "INNER JOIN member as m ON r.member_id = m.id " +
                        "WHERE (r.date, r.theme_id) = (?, ?)",
                (resultSet, rowNum) -> {
                    ReservationTimeEntity timeEntity = new ReservationTimeEntity(
                            resultSet.getLong("time_id"),
                            resultSet.getString("time_value")
                    );

                    ThemeEntity themeEntity = new ThemeEntity(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    );

                    MemberEntity memberEntity = new MemberEntity(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member_email"),
                            resultSet.getString("member_password"),
                            resultSet.getString("member_name"),
                            Role.valueOf(resultSet.getString("role"))
                    );

                    ReservationEntity entity = new ReservationEntity(
                            resultSet.getLong("reservation_id"),
                            resultSet.getString("date"),
                            memberEntity,
                            timeEntity,
                            themeEntity
                    );

                    return entity.toReservation();
                },
                date,
                themeId
        );
    }

}
