package roomescape.reservation.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.Time;
import roomescape.theme.domain.Theme;

@Repository
public class ReservationRepository {

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReservationRepository(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Reservation add(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("member_id", reservation.member().id());
        parameters.put("date", reservation.date());
        parameters.put("time_id", reservation.time().id());
        parameters.put("theme_id", reservation.theme().id());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Reservation(
                id,
                reservation.member(),
                reservation.date(),
                reservation.time(),
                reservation.theme()
        );
    }

    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT r.id AS reservation_id, r.date, "
                + "t.id AS time_id, "
                + "t.start_at AS time_value, "
                + "e.id AS theme_id, "
                + "e.name AS theme_name, "
                + "e.description AS theme_description, "
                + "e.thumbnail AS theme_thumbnail, "
                + "m.id AS member_id, "
                + "m.name AS member_name, "
                + "m.email AS member_email, "
                + "m.password AS member_password, "
                + "m.role AS member_role "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS t ON r.time_id = t.id "
                + "INNER JOIN theme AS e ON r.theme_id = e.id "
                + "INNER JOIN member AS m ON r.member_id = m.id "
                + "WHERE r.id = :id";

        Map<String, Object> parameter = Map.of("id", id);

        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(
                    sql, parameter,
                    (resultSet, rowNum) -> createReservation(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Reservation> findAll() {
        String sql = "SELECT r.id AS reservation_id, r.date, "
                + "t.id AS time_id, "
                + "t.start_at AS time_value, "
                + "e.id AS theme_id, "
                + "e.name AS theme_name, "
                + "e.description AS theme_description, "
                + "e.thumbnail AS theme_thumbnail, "
                + "m.id AS member_id, "
                + "m.name AS member_name, "
                + "m.email AS member_email, "
                + "m.password AS member_password, "
                + "m.role AS member_role "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS t ON r.time_id = t.id "
                + "INNER JOIN theme AS e ON r.theme_id = e.id "
                + "INNER JOIN member AS m ON r.member_id = m.id";

        return namedParameterJdbcTemplate.query(sql, (resultSet, rowNum) -> createReservation(resultSet));
    }

    public List<Reservation> findBySearchFilter(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        String sql = "SELECT r.id AS reservation_id, r.date, "
                + "t.id AS time_id, "
                + "t.start_at AS time_value, "
                + "e.id AS theme_id, "
                + "e.name AS theme_name, "
                + "e.description AS theme_description, "
                + "e.thumbnail AS theme_thumbnail, "
                + "m.id AS member_id, "
                + "m.name AS member_name, "
                + "m.email AS member_email, "
                + "m.password AS member_password, "
                + "m.role AS member_role "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS t ON r.time_id = t.id "
                + "INNER JOIN theme AS e ON r.theme_id = e.id "
                + "INNER JOIN member AS m ON r.member_id = m.id "
                + "WHERE e.id = :themeId AND m.id = :memberId "
                + "AND r.date >= :dateFrom AND r.date <= :dateTo";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("themeId", themeId);
        parameters.put("memberId", memberId);
        parameters.put("dateFrom", dateFrom);
        parameters.put("dateTo", dateTo);

        return namedParameterJdbcTemplate.query(sql, parameters,
                (resultSet, rowNum) -> createReservation(resultSet));
    }

    public boolean existsByThemeId(Long themeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = :themeId)";

        Map<String, Object> parameter = Map.of("themeId", themeId);

        return Boolean.TRUE.equals(
                namedParameterJdbcTemplate.queryForObject(sql, parameter, Boolean.class));
    }

    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = :timeId)";

        Map<String, Object> parameter = Map.of("timeId", timeId);

        return Boolean.TRUE.equals(
                namedParameterJdbcTemplate.queryForObject(sql, parameter, Boolean.class));
    }

    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation "
                + "WHERE date = :date AND time_id = :timeId AND theme_id = :themeId)";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("date", date);
        parameters.put("timeId", timeId);
        parameters.put("themeId", themeId);

        return Boolean.TRUE.equals(
                namedParameterJdbcTemplate.queryForObject(sql, parameters, Boolean.class));
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";

        Map<String, Object> parameter = Map.of("id", id);

        namedParameterJdbcTemplate.update(sql, parameter);
    }

    private Reservation createReservation(ResultSet resultSet) throws SQLException {
        return new Reservation(
                resultSet.getLong("reservation_id"),
                Member.of(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("member_email"),
                        resultSet.getString("member_password"),
                        resultSet.getString("member_role")
                ),
                resultSet.getDate("date").toLocalDate(),
                new Time(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("time_value").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                ));
    }
}
