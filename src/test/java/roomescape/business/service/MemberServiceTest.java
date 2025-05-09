package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.fake.FakeMemberDao;
import roomescape.presentation.dto.MemberRequest;
import roomescape.presentation.dto.MemberResponse;
import roomescape.presentation.exception.BadRequestException;

class MemberServiceTest {

    private final FakeMemberDao fakeMemberDao = new FakeMemberDao();
    private final MemberService memberService = new MemberService(fakeMemberDao);

    @DisplayName("멤버 생성 테스트")
    @Nested
    class CreateMemberTest {

        private static final String SAVED_NAME = "사용자";
        private static final String SAVED_EMAIL = "aaa@gmail.com";

        @BeforeEach
        void setUp() {
            memberService.createMember(new MemberRequest(SAVED_NAME, SAVED_EMAIL, "1234"));
        }

        @DisplayName("새로운 사용자를 등록할 수 있다.")
        @Test
        void testCreateMember() {
            // given
            String name = "노랑";
            String email1 = "bbb@gmail.com";
            String password = "1234";
            MemberRequest request = new MemberRequest(name, email1, password);
            // when
            MemberResponse savedMember = memberService.createMember(request);
            // then
            assertAll(
                    () -> assertThat(savedMember.id()).isEqualTo(2L),
                    () -> assertThat(savedMember.name()).isEqualTo(name),
                    () -> assertThat(fakeMemberDao.findAll().size()).isEqualTo(2)
            );
        }

        @DisplayName("중복 사용자 이름일 경우 예외가 발생한다.")
        @Test
        void testDuplicateName() {
            // given
            MemberRequest request = new MemberRequest(SAVED_NAME, "bbb@gmail.com", "1234");
            // when
            // then
            assertThatThrownBy(() -> memberService.createMember(request))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("이미 사용중인 이름입니다.");
        }

        @DisplayName("중복 이메일일 경우 예외가 발생한다.")
        @Test
        void testDuplicateEmail() {
            // given
            MemberRequest request = new MemberRequest("노랑", SAVED_EMAIL, "1234");
            // when
            // then
            assertThatThrownBy(() -> memberService.createMember(request))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("이미 사용중인 이메일입니다.");
        }
    }
}
