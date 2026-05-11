package roomescape.closeddate.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.closeddate.domain.ClosedDate;

@Repository
public class JdbcClosedDateRepository implements ClosedDateRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<ClosedDate> closedDateRowMapper = (resultSet, rowNum) ->
            ClosedDate.load(
                    resultSet.getLong("id"),
                    resultSet.getDate("date").toLocalDate()
            );

    public JdbcClosedDateRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("closed_date")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ClosedDate> findAll() {
        String sql = "SELECT * FROM closed_date";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), closedDateRowMapper);
    }

    @Override
    public Optional<ClosedDate> findById(Long id) {
        String sql = "SELECT * FROM closed_date WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, closedDateRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public ClosedDate save(ClosedDate closedDate) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", closedDate.date());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return ClosedDate.load(id, closedDate.date());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM closed_date WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public boolean existsByDate(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM closed_date WHERE date = :date";
        SqlParameterSource params = new MapSqlParameterSource("date", date);
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}
