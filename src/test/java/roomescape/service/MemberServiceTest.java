package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    MemberService memberService;

    @DisplayName("존재하지 않는 멤버번호를 찾으려는 경우 예외를 던진다")
    @Test
    void removeReservationTime() {
        //given
        long notExistId = 999;
        when(memberRepository.findById(notExistId)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> memberService.findMemberById(notExistId)).isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 999번에 해당하는 멤버가 없습니다.");
    }
}
