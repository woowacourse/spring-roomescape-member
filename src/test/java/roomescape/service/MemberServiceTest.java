package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dto.SignUpRequest;
import roomescape.exception.exception.DataNotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @DisplayName("존재하지 않는 멤버번호를 찾으려는 경우 예외를 던진다")
    @Test
    void removeReservationTime() {
        //given
        memberService.signUpMember(new SignUpRequest("제프리", "jeffrey@gmail.com", "1234!@#$"));
        long notExistId = 999;

        //when & then
        assertThatThrownBy(() -> memberService.findMemberById(notExistId)).isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 999번에 해당하는 멤버가 없습니다.");
    }
}
