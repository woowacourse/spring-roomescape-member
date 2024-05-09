package roomescape.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isExist(String email) {
        String sql = "SELECT EXISTS ( SELECT 1 FROM member WHERE email = ?)";
        try {
            return jdbcTemplate.queryForObject(sql, Boolean.class, email);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new queryResultSizeException("db 쿼리 조회 에러");
        }
    }

    public Member findMember(String memberEmail) {
        String sql = "SELECT member.name, member.email, member.password FROM member WHERE member.email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                return new Member(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }, memberEmail);
            return member;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new queryResultSizeException("db 쿼리 조회 에러");
        }
    }
}
