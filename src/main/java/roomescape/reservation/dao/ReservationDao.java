package roomescape.reservation.dao;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.dto.ReservationMember;
import roomescape.reservation.domain.repository.ReservationRepository;

@Repository
public class ReservationDao implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<ReservationMember> rowMapper = (ResultSet resultSet, int rowNum) -> {
        return new ReservationMember(
                new Reservation(
                        resultSet.getLong("reservation_id"),
                        resultSet.getDate("date").toLocalDate(),
                        new ReservationTime(resultSet.getLong("time_id"),
                                resultSet.getTime("time_value").toLocalTime()
                        ),
                        new Theme(resultSet.getLong("theme_id"),
                                resultSet.getString("theme_name"),
                                resultSet.getString("description"),
                                resultSet.getString("thumbnail"))
                ),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_name")
                )
        );
    };

    private final ResultSetExtractor<Optional<ReservationMember>> optionalResultSetExtractor = (ResultSet resultSet) -> {
        if (resultSet.next()) {
            ReservationMember reservationMember = new ReservationMember(
                    new Reservation(
                            resultSet.getLong("reservation_id"),
                            resultSet.getDate("date").toLocalDate(),
                            new ReservationTime(resultSet.getLong("time_id"),
                                    resultSet.getTime("time_value").toLocalTime()
                            ),
                            new Theme(resultSet.getLong("theme_id"),
                                    resultSet.getString("theme_name"),
                                    resultSet.getString("description"),
                                    resultSet.getString("thumbnail"))
                    ),
                    new Member(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member_name")
                    )
            );
            return Optional.of(reservationMember);
        } else {
            return Optional.empty();
        }
    };

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(
                id,
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public List<ReservationMember> findAll() {
        String sql = """
                SELECT r.id AS reservation_id, r.date, t.id AS time_id, t.start_at AS time_value, 
                th.id AS theme_id, th.name AS theme_name, th.description, th.thumbnail, m.id AS member_id, m.name AS member_name 
                FROM reservation AS r 
                INNER JOIN reservation_time AS t ON r.time_id = t.id 
                INNER JOIN theme AS th ON r.theme_id = th.id 
                INNER JOIN reservation_list AS rl ON rl.reservation_id = r.id 
                INNER JOIN member AS m ON m.id = rl.member_id;
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean deleteById(long reservationId) {
        String sql = "DELETE FROM reservation WHERE id = ?;";
        int updateId = jdbcTemplate.update(sql, reservationId);
        return updateId != 0;
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        String sql = """
                SELECT 1
                FROM reservation as r 
                INNER JOIN reservation_time as t ON r.time_id = t.id 
                INNER JOIN theme as th ON r.theme_id = th.id 
                WHERE t.id = ? 
                LIMIT 1;
                """;

        return jdbcTemplate.query(sql, ResultSet::next, timeId);
    }

    @Override
    public boolean existReservationListBy(LocalDate date, long timeId, long themeId) {
        String sql = """
                SELECT 1
                FROM reservation_list as rl
                WHERE rl.reservation_id = (
                    SELECT r.id
                    FROM reservation AS r 
                    INNER JOIN reservation_time AS t ON r.time_id = t.id 
                    INNER JOIN theme AS th ON r.theme_id = th.id 
                    INNER JOIN reservation_list AS rl ON rl.reservation_id = r.id 
                    INNER JOIN member AS m ON m.id = rl.member_id 
                    WHERE date = ? AND time_id = ? AND theme_id = ?
                    LIMIT 1
                );
                """;

        return jdbcTemplate.query(sql, ResultSet::next, date, timeId, themeId);
    }

    @Override
    public void saveReservationList(long memberId, long reservationId) {
        String sql = "INSERT INTO reservation_list(member_id, reservation_id) VALUES (?, ?);";
        jdbcTemplate.update(sql, memberId, reservationId);
    }
}
