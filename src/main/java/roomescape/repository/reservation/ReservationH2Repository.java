package roomescape.repository.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.*;

@Repository
public class ReservationH2Repository implements ReservationRepository {

    private static final String TABLE_NAME = "RESERVATION";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationH2Repository(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", reservation.getMember().getId())
                .addValue("date", reservation.getDate(DateTimeFormatter.ISO_DATE))
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getMember(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    @Override
    public boolean hasSameReservation(String date, Long timeId, Long themeId) {
        String sql = "SELECT * FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?";
        return !jdbcTemplate.query(sql, (rs, rowNum) -> 0, date, timeId, themeId).isEmpty();
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM RESERVATION WHERE id = ?", id);
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                "SELECT r.id as reservation_id, r.member_id, member.name as member_name, member.email as member_email , r.date, time.id as time_id, time.start_at as time_value,theme.id as theme_id, theme.name as theme_name, theme.description, theme.thumbnail \n" +
                        "FROM reservation as r \n" +
                        "inner join reservation_time as time on r.time_id = time.id\n" +
                        "inner join theme on r.theme_id = theme.id\n" +
                        "inner join member on r.member_id = member.id",
                getReservationRowMapper()
        );
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> {
            ReservationTime reservationTime = new ReservationTime(
                    resultSet.getLong("time_id"),
                    LocalTime.parse(resultSet.getString("time_value"))
            );
            Theme theme = new Theme(
                    resultSet.getLong("theme_id"),
                    new Name(resultSet.getString("theme_name")),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );
            Member member = new Member(
                    resultSet.getLong("member_id"),
                    new Name(resultSet.getString("member_name")),
                    new Email(resultSet.getString("member_email")),
                    null
            );
            return new Reservation(
                    resultSet.getLong("id"),
                    member,
                    LocalDate.parse(resultSet.getString("date")),
                    reservationTime,
                    theme
            );
        };
    }
}
