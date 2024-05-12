package roomescape.member.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import roomescape.member.domain.Member;
import roomescape.member.response.MemberReservationResponse;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberRowMapper = (resultSet, __) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getBoolean("is_admin")
    );
    private final RowMapper<MemberReservationResponse> memberReservationResponseRowMapper = (resultSet, __) -> new MemberReservationResponse(
            resultSet.getLong("id"),
            resultSet.getString("name")
    );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isMember(String email, String password) {
        String query = "SELECT COUNT(*) FROM MEMBER WHERE EMAIL = ? AND PASSWORD = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, email, password);
        return count != null && count == 1;
    }

    public Member findMemberByEmail(String email) {
        String query = "SELECT " +
                "ID, NAME, EMAIL, PASSWORD, IS_ADMIN " +
                "FROM MEMBER " +
                "WHERE email = ?";
        return jdbcTemplate.queryForObject(query, memberRowMapper, email);
    }

    public String findNameById(Long memberId) {
        String query = "SELECT NAME FROM MEMBER WHERE ID = ?";
        return jdbcTemplate.queryForObject(query, String.class, memberId);
    }

    public Member findMemberById(long id) {
        String query = "SELECT ID, NAME, EMAIL, PASSWORD, IS_ADMIN FROM MEMBER WHERE ID = ?";
        return jdbcTemplate.queryForObject(query, memberRowMapper, id);
    }

    public List<MemberReservationResponse> findAllReservationResponse() {
        String query = "SELECT "
                + "ID, NAME "
                + "FROM MEMBER ";
        return jdbcTemplate.query(query, memberReservationResponseRowMapper);
    }
}
