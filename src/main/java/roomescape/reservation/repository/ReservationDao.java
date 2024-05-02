package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.ranking.RankTheme;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, __) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            )
    );
    private final RowMapper<Theme> themeRowMapper = (resultSet, __) -> new Theme(
            resultSet.getLong("name"),
    private final RowMapper<RankTheme> themeRowMapper = (resultSet, __) -> new RankTheme(
            resultSet.getString("name"),
            resultSet.getString("thumbnail"),
            resultSet.getDate("descr").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            )
            resultSet.getString("description")
    );
    );

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String query = "SELECT "
                + "r.id, r.name, r.date, "
                + "t.id AS time_id, t.start_at, "
                + "theme.id AS theme_id, theme.name as theme_name, "
                + "theme.description AS theme_description, theme.thumbnail AS theme_thumbnail "
                + "FROM RESERVATION AS r "
                + "INNER JOIN RESERVATION_TIME AS t "
                + "ON r.time_id = t.id "
                + "INNER JOIN THEME AS theme "
                + "ON r.theme_id = theme.id";
        return jdbcTemplate.query(query, reservationRowMapper);
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.name())
                .addValue("date", reservation.date())
                .addValue("time_id", reservation.time().id())
                .addValue("theme_id", reservation.theme().id());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return findById(id);
    }

    private Reservation findById(long id) {
        String query = "SELECT "
                + "r.id, r.name, r.date, "
                + "t.id AS time_id, t.start_at, "
                + "theme.id AS theme_id, theme.name as theme_name, "
                + "theme.description AS theme_description, theme.thumbnail AS theme_thumbnail "
                + "FROM RESERVATION AS r "
                + "INNER JOIN RESERVATION_TIME AS t "
                + "ON r.time_id = t.id "
                + "INNER JOIN THEME AS theme "
                + "ON r.theme_id = theme.id "
                + "WHERE r.id = ?";
        return jdbcTemplate.queryForObject(query, reservationRowMapper, id);
    }

    public boolean existsTime(long id) {
        String query = "SELECT COUNT(*) FROM RESERVATION WHERE TIME_ID = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, id);
        return count != null && count > 0;
    }

    public void deleteById(long id) {
        jdbcTemplate.update("DELETE FROM RESERVATION WHERE ID = ?", id);
    }

    public boolean existsByDateTime(LocalDate date, long timeId) {
        String query = "SELECT COUNT(*) FROM RESERVATION WHERE time_id = ? AND date = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, timeId, date);
        return count != null && count > 0;
    }

    public List<Theme> getRanking(){
        String query="SELECT t.name, t.thumbnail, t.description, COUNT(*) AS count\n"
                + "FROM RESERVATION r "
                + "INNER JOIN THEME t ON r.theme_id = t.id "
                + "GROUP BY r.theme_id "
                + "ORDER BY count DESC "
                + "LIMIT 10";
    //    public List<RankTheme> getRanking(){
//        String query="SELECT t.name, t.thumbnail, t.description, COUNT(*) AS count "
//                + "FROM RESERVATION r "
//                + "INNER JOIN THEME t ON r.theme_id = t.id "
//                + "GROUP BY r.theme_id "
//                + "ORDER BY count DESC "
//                + "LIMIT 10";
//        return jdbcTemplate.query(query, themeRowMapper);
//
//
//    }
    public List<RankTheme> getRanking() {
        String query = "SELECT t.id, t.name, t.description, t.thumbnail, COUNT(r.id) AS reservation_count " +
                "FROM theme t " +
                "INNER JOIN reservation r ON t.id = r.theme_id " +
                "WHERE r.date >=( TIMESTAMPADD(DAY, -7, CURRENT_DATE)) " +
                "AND r.date <= ( TIMESTAMPADD(DAY, -1, CURRENT_DATE)) " +
                "GROUP BY t.id, t.name, t.description, t.thumbnail " +
                "ORDER BY reservation_count DESC " +
                "LIMIT 10";

        return jdbcTemplate.query(query, themeRowMapper);
    }

}
