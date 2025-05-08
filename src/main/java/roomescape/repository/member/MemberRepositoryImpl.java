package roomescape.repository.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.entity.MemberEntity;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MemberEntity findMember(MemberEntity memberRequest) {
        String sql = "SELECT * FROM member where email=? and password=?";
        MemberEntity member = jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            MemberEntity memberEntity = new MemberEntity(resultSet.getLong("id"), resultSet.getString("name"),
                    resultSet.getString("email"), resultSet.getString("password"));
            return memberEntity;
        }, memberRequest.email(), memberRequest.password());
        return member;
    }

    @Override
    public MemberEntity findMemberById(Long id) {
        String sql = "SELECT * FROM member where id=?";
        MemberEntity member = jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            MemberEntity memberEntity = new MemberEntity(resultSet.getLong("id"), resultSet.getString("name"),
                    resultSet.getString("email"), resultSet.getString("password"));
            return memberEntity;
        }, id);
        return member;
    }

}
