package roomescape.rank.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import roomescape.rank.response.RankTheme;

@Repository
public class RankDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<RankTheme> rankThemeRowMapper = (resultSet, __) -> new RankTheme(
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    public RankDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RankTheme> getRank() {
        String query = "SELECT t.id, t.name, t.description, t.thumbnail, COUNT(r.id) AS reservation_count " +
                "FROM theme t " +
                "INNER JOIN reservation r ON t.id = r.theme_id " +
                "WHERE r.date >=( TIMESTAMPADD(DAY, -7, CURRENT_DATE)) " +
                "AND r.date <= ( TIMESTAMPADD(DAY, -1, CURRENT_DATE)) " +
                "GROUP BY t.id, t.name, t.description, t.thumbnail " +
                "ORDER BY reservation_count DESC " +
                "LIMIT 10";

        return jdbcTemplate.query(query, rankThemeRowMapper);
    }
}
