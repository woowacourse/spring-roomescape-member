package roomescape.infrastructure;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.SiteUser;

import java.util.Optional;

@Repository
public class JdbcSiteUserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<SiteUser> siteUserRowMapper = (resultSet, rowNum) -> new SiteUser(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
            );

    public JdbcSiteUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("site_user")
                .usingGeneratedKeyColumns("id");
    }


    public Optional<SiteUser> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM site_user WHERE email = ? AND password = ?";
        SiteUser siteUser = jdbcTemplate.queryForObject(sql, siteUserRowMapper, email, password);

        return Optional.ofNullable(siteUser);
    }

    public Optional<SiteUser> findById(Long memberId) {
        String sql = "SELECT * FROM site_user WHERE id = ?";
        SiteUser siteUser = jdbcTemplate.queryForObject(sql, siteUserRowMapper, memberId);

        return Optional.ofNullable(siteUser);
    }
}
