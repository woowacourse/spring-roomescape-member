package roomescape.dao;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.mapper.MemberMapper;

@Component
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        try {
            String sql = "select * from member where email = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    sql,
                    new MemberMapper(),
                    email
            ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        try {
            String sql = "select * from member where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    sql,
                    new MemberMapper(),
                    memberId
            ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
