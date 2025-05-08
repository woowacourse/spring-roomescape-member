package roomescape.repository.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member findMember(Member memberRequest) {
        String sql = "SELECT * FROM member where email=? and password=?";
        Member member = jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            Member memberEntity = new Member(resultSet.getLong("id"), resultSet.getString("name"),
                    resultSet.getString("email"), resultSet.getString("password"));
            return memberEntity;
        }, memberRequest.getEmail(), memberRequest.getPassword());
        return member;
    }

    @Override
    public Member findMemberById(Long id) {
        String sql = "SELECT * FROM member where id=?";
        Member member = jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            Member newMember = new Member(resultSet.getLong("id"), resultSet.getString("name"),
                    resultSet.getString("email"), resultSet.getString("password"));
            return newMember;
        }, id);
        return member;
    }
}
