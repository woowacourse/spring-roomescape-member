package roomescape.dao;

import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.mapper.MemberRowMapper;
import roomescape.domain.Member;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final MemberRowMapper rowMapper;

    public MemberDao(final JdbcTemplate jdbcTemplate,
                     final DataSource dataSource,
                     final MemberRowMapper memberRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = memberRowMapper;
    }

    public Member create(final Member member) {
        final var params = new MapSqlParameterSource()
                .addValue("name", member.name())
                .addValue("email", member.email())
                .addValue("password", member.password());
        final var id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Member.of(id, member);
    }

    public Optional<Member> findByEmail(final Member member) {
        final var sql = "SELECT * FROM member WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, member.email().value()));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(final String id) {
        final var sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(final Long id) {
        final var sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

//    public boolean isExistByStartAt(final String startAt) {
//        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE start_at = ?)";
//        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, startAt));
//    }
//
//    public Optional<ReservationTime> find(final Long id) {
//        final String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
//        try {
//            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id));
//        } catch (final EmptyResultDataAccessException exception) {
//            return Optional.empty();
//        }
//    }
//
//    public List<ReservationTime> getAll() {
//        final String sql = "SELECT id, start_at FROM reservation_time";
//        return jdbcTemplate.query(sql, reservationTimeRowMapper);
//    }
//
//    public List<AvailableReservationTimeOutput> findAvailable(final ReservationDate date, final long themeId) {
//        final String sql = """
//                SELECT
//                t.start_at AS start_at,
//                t.id AS time_id,
//                r.id IS NOT NULL AS already_booked
//                FROM reservation_time AS t
//                LEFT OUTER JOIN reservation AS r
//                ON t.id = r.time_id AND r.date = ? AND r.theme_id = ?;
//                """;
//        return jdbcTemplate.query(sql, availableReservationTimeRowMapper, date.asString(), themeId);
//    }
//
//    public void delete(final long id) {
//        final String sql = "DELETE FROM reservation_time WHERE id = ?";
//        jdbcTemplate.update(sql, id);
//    }

}
