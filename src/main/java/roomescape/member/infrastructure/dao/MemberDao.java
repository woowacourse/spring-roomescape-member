package roomescape.member.infrastructure.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.application.repository.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.SignUpRequest;
import roomescape.member.presentation.dto.TokenRequest;

@Repository
public class MemberDao implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member insert(final SignUpRequest signUpRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", signUpRequest.getEmail());
        params.put("password", signUpRequest.getPassword());
        params.put("name", signUpRequest.getName());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getName());
    }

    @Override
    public Optional<Member> findByEmail(TokenRequest tokenRequest) {
        String sql = "select id, email, password, name from member where email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    sql, (resultSet, rowNum) -> {
                        return new Member(
                                resultSet.getLong("id"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getString("name")
                        );
                    }, tokenRequest.getEmail()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

//    @Override
//    public Optional<ReservationTime> findById(final Long timeId) {
//        String sql = "select id, start_at from reservation_time where id = ?";
//        try {
//            return Optional.ofNullable(jdbcTemplate.queryForObject(
//                    sql, RESERVATION_TIME_ROW_MAPPER, timeId));
//        } catch (EmptyResultDataAccessException e) {
//            return Optional.empty();
//        }
//    }
}
