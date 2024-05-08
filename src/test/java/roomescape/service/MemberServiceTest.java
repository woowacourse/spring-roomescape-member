package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.dto.MemberResponse;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("id에 해당하는 사용자를 조회한다.")
    void findById() {
        // given
        final Member expectedMember = new Member(1L, new Name("냥인"), "nyangin@email.com", "1234");
        given(memberDao.findById(anyLong()))
                .willReturn(Optional.of(expectedMember));

        // when
        final MemberResponse memberResponse = memberService.findById(1L);

        // then
        assertThat(memberResponse.id()).isEqualTo(expectedMember.getId());
    }
}
