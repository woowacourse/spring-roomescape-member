package roomescape.member.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import java.util.Optional;

@Repository
public class MemberDao {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = ((rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
            )
    );

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE member.email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
