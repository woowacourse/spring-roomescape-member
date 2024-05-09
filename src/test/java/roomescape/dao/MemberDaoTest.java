package roomescape.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.domain.Member;

@SpringBootTest
class MemberDaoTest {
    @Autowired
    private MemberDao memberDao;

    private Member dummyMember;

    @BeforeEach
    void setUp() {
        dummyMember = new Member("name", "email", "password");
    }

    @AfterEach
    void tearDown() {
        memberDao.deleteAll();
    }

    @Test
    @DisplayName("사용자 정보를 저장한다")
    void save_ShouldStorePersistence() {
        // given
        dummyMember = new Member("name", "email", "password");

        // when
        Member savedMember = memberDao.save(dummyMember);

        // then
        Assertions.assertThat(memberDao.findById(savedMember.getId())).isPresent();
    }

    @Test
    @DisplayName("영속성에 사용자 있으면 참을 반환한다")
    void existsByEmailAndPassword_ShouldGetPersistence() {
        // given
        dummyMember = new Member("name", "email", "password");
        memberDao.save(dummyMember);

        // when & then
        Assertions.assertThat(memberDao.findByEmailAndPassword(dummyMember.getEmail(), dummyMember.getPassword()))
                .isPresent();
    }

    @Test
    @DisplayName("영속성에 사용자 없으면 거짓을 반환한다")
    void existsByEmailAndPassword_ShouldReturnNull_WhenTargetDoesNotExist() {
        // when & then
        Assertions.assertThat(memberDao.findByEmailAndPassword(dummyMember.getEmail(), dummyMember.getPassword()))
                .isEmpty();
    }
}
