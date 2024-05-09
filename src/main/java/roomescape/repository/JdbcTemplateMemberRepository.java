package roomescape.repository;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.repository.rowmapper.MemberRowMapper;

@Repository
public class JdbcTemplateMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmailAndEncryptedPassword(String email, String encryptedPassword) {
        String query = "SELECT id, name, email, password FROM member WHERE email = ? AND password = ?";
        return jdbcTemplate.query(query, new MemberRowMapper(), email, encryptedPassword)
                .stream()
                .findAny();
    }
}
