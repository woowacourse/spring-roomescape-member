//package roomescape.repository;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.stereotype.Repository;
//import roomescape.model.Reservation;
//import roomescape.model.ReservationTime;
//
//import java.sql.PreparedStatement;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//@Repository
//public class ReservationRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public ReservationRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public Long create(Reservation reservation) {
//        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(
//                connection -> {
//                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
//                    ps.setString(1, reservation.getName());
//                    ps.setObject(2, reservation.getDate());
//                    ps.setLong(3, reservation.getTime().getId());
//                    return ps;
//                }, keyHolder);
//        return keyHolder.getKey().longValue();
//    }
//
//    public List<Reservation> findAll() {
//        String sql = "SELECT r.id, r.name, r.date, t.id AS time_id, t.start_at AS time_value FROM reservation r INNER JOIN reservation_time t ON r.time_id = t.id";
//
//        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
//            ReservationTime time = new ReservationTime(
//                    resultSet.getLong("time_id"),
//                    resultSet.getObject("time_value", LocalTime.class)
//            );
//            return new Reservation(
//                    resultSet.getLong("id"),
//                    resultSet.getString("name"),
//                    resultSet.getObject("date", LocalDate.class),
//                    time
//            );
//        });
//    }
//
//    public int delete(Long id) {
//        String sql = "delete from reservation where id = ?";
//        return jdbcTemplate.update(sql, id);
//    }
//}
