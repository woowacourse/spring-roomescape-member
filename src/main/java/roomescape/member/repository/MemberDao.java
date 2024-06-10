package roomescape.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import roomescape.member.domain.Member;
import roomescape.member.response.MemberReservationResponse;

@Repository
public class MemberDao {
    private static final String SECRET = "password";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberRowMapper = (resultSet, __) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getBoolean("is_admin")
    );
    private final RowMapper<MemberReservationResponse> memberReservationResponseRowMapper = (resultSet, __) -> new MemberReservationResponse(
            resultSet.getLong("id"),
            resultSet.getString("name")
    );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByEmailAndPassword(String email, String password) {
        String query = "SELECT COUNT(*) FROM MEMBER WHERE EMAIL = ? AND PASSWORD = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, email, password);
        return count != null && count == 1;
    }

    public Optional<Member> findMemberByEmail(String email) {
        String query = "SELECT " +
                "ID, NAME, EMAIL, PASSWORD, IS_ADMIN " +
                "FROM MEMBER " +
                "WHERE email = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(query, memberRowMapper, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findMemberById(long id) {
        String query = "SELECT ID, NAME, EMAIL, PASSWORD, IS_ADMIN FROM MEMBER WHERE ID = ?";
       try{
           return Optional.of(jdbcTemplate.queryForObject(query, memberRowMapper, id));
       }catch(EmptyResultDataAccessException e){
           return Optional.empty();
       }
    }

    public List<MemberReservationResponse> findAllMemberReservationResponse() {
        String query = "SELECT "
                + "ID, NAME "
                + "FROM MEMBER ";
        return jdbcTemplate.query(query, memberReservationResponseRowMapper);
    }
}
