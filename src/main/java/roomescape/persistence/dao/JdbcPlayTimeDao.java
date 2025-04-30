package roomescape.persistence.dao;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.PlayTime;
import roomescape.persistence.entity.PlayTimeEntity;

@Repository
public class JdbcPlayTimeDao implements PlayTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPlayTimeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final PlayTime playTime) {
        final PlayTimeEntity playTimeEntity = PlayTimeEntity.from(playTime);
        final String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, playTimeEntity.startAt());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<PlayTime> find(final Long id) {
        final String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        try {
            final PlayTimeEntity playTimeEntity = jdbcTemplate.queryForObject(sql, PlayTimeEntity.getDefaultRowMapper(), id);
            return Optional.of(playTimeEntity.toDomain());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PlayTime> findAll() {
        final String sql = "SELECT id, start_at FROM reservation_time";

        return jdbcTemplate.query(sql, PlayTimeEntity.getDefaultRowMapper()).stream()
                .map(PlayTimeEntity::toDomain)
                .toList();
    }

    @Override
    public boolean remove(final Long id) {
        final String sql = "DELETE FROM reservation_time WHERE id = ?";
        final int rowNum = jdbcTemplate.update(sql, id);

        return rowNum == 1;
    }

    @Override
    public boolean existsByStartAt(final LocalTime startAt) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE start_at = ?) AS is_exists";
        final int flag = jdbcTemplate.queryForObject(sql, Integer.class ,PlayTimeEntity.formatStartAt(startAt));

        return flag == 1;
    }
}
