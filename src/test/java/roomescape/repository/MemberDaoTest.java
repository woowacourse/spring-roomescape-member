package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.dto.SignupRequest;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class MemberDaoTest {
    private final MemberDao memberDao;

    @Autowired
    public MemberDaoTest(final JdbcTemplate jdbcTemplate) {
        this.memberDao = new MemberDao(jdbcTemplate);
    }

    private long getItemSize() {
        return memberDao.findAll().size();
    }

    @DisplayName("Db에 등록된 모든 회원 목록을 조회한다.")
    @Test
    void given_when_findAll_then_returnReservations() {
        //given, when, then
        assertThat(memberDao.findAll().size()).isEqualTo(1);
    }

    @DisplayName("Db에 회원 정보를 저장한다.")
    @Test
    void given_reservation_when_create_then_returnNoting() {
        //given
        SignupRequest signupRequest = new SignupRequest("ash@test.com", "123456", "ash");
        //when
        memberDao.create(signupRequest);
        //then
        assertThat(getItemSize()).isEqualTo(2);
    }
}