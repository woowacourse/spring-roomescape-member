package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

@Repository
public class JdbcMemberDao implements MemberRepository {

    private final RowMapper<Member> rowMapper = (rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
            );
    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM member where email = ?";

        List<Member> findMembers = jdbcTemplate.query(sql, rowMapper, email);

        if (findMembers.isEmpty()) {
            return Optional.empty();
        }
        if (findMembers.size() > 1) {
            throw new IllegalStateException("조회 결과가 2개 이상입니다.");
        }

        return Optional.of(findMembers.getFirst());
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, name, email, password FROM member where id = ?";
        List<Member> findMembers = jdbcTemplate.query(sql, rowMapper, id);

        if (findMembers.isEmpty()) {
            return Optional.empty();
        }
        if (findMembers.size() > 1) {
            throw new IllegalStateException("조회 결과가 2개 이상입니다.");
        }

        return Optional.of(findMembers.getFirst());
    }
}
