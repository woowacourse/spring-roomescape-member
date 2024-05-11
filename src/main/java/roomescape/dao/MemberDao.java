package roomescape.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Role;

import java.util.List;

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

    public Member findMemberByEmail(String memberEmail) {
        String sql = "SELECT member.id, member.name, member.email, member.password, member.role FROM member WHERE member.email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, getMemberRowMapper(), memberEmail);
            return member;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new queryResultSizeException("db 쿼리 조회 에러");
        }
    }

    public Member findMemberById(Long memberId) {
        String sql = "SELECT member.id, member.name, member.email, member.password, member.role FROM member WHERE id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, getMemberRowMapper(), memberId);
            return member;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new queryResultSizeException("db 쿼리 조회 에러");
        }
    }

    public List<Member> allMembers() {
        String sql = "SELECT id, name, email, password, role FROM member";
        return jdbcTemplate.query(sql, getMemberRowMapper());
    }

    private static RowMapper<Member> getMemberRowMapper() {
        return (rs, rowNum) -> {
            return new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    Role.findRole(rs.getString("role"))
            );
        };
    }
}
