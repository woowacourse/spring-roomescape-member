package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTheme;

@Repository
public class RoomescapeThemeRepositoryImpl implements RoomescapeThemeRepository {

    private static final int SUCCESS_COUNT = 1;

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public RoomescapeThemeRepositoryImpl(final DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<ReservationTheme> findById(final Long id) {
        String sql = "select * from reservation_theme where id=:id";
        try {
            SqlParameterSource param = new MapSqlParameterSource("id", id);
            ReservationTheme reservationTheme = template.queryForObject(sql, param, reservationThemeRowMapper());
            return Optional.of(reservationTheme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTheme> findAll() {
        String sql = "select * from reservation_theme";
        return template.query(sql, reservationThemeRowMapper());
    }

    @Override
    public List<ReservationTheme> findTopThemeOrderByCountWithinDaysDesc(int days) {
        String sql = """
                SELECT th.id, th.name, th.description, th.thumbnail, COUNT(*) AS reservation_count
                FROM reservation r
                JOIN reservation_theme th ON r.theme_id = th.id
                WHERE r.date BETWEEN DATEADD('DAY', :days, CURRENT_DATE) AND DATEADD('DAY', -1, CURRENT_DATE)
                GROUP BY th.id, th.name, th.description, th.thumbnail
                ORDER BY reservation_count DESC
                LIMIT 10;
                """;
        SqlParameterSource param = new MapSqlParameterSource("days", -days);
        return template.query(sql, param, reservationThemeRowMapper());
    }

    @Override
    public ReservationTheme save(final ReservationTheme reservationTheme) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", reservationTheme.getName())
                .addValue("description", reservationTheme.getDescription())
                .addValue("thumbnail", reservationTheme.getThumbnail());
        Number key = insert.executeAndReturnKey(param);
        return reservationTheme.assignId(key.longValue());
    }

    @Override
    public boolean deleteById(final Long id) {
        String sql = "delete from reservation_theme where id=:id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        return template.update(sql, param) == SUCCESS_COUNT;

    }

    private RowMapper<ReservationTheme> reservationThemeRowMapper() {
        return (rs, rowNum) -> {
            return new ReservationTheme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            );
        };
    }
}
