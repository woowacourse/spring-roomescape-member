package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.member.Member;
import roomescape.repository.MemberDao;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest { //mockito의 효용성을 가장 많이 느꼈던 테스트
    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 id를 통해 회원조회가 가능하다")
    void should_FindMemberById() {
        //given
        Member member = new Member(1L, "콜리", "email@naver.com", "psssword", "USER");
        Mockito.when(memberDao.findById(anyLong()))
                .thenReturn(Optional.of(member));

        //when
        Member foundMember = memberService.findMemberById(1L);

        //then
        assertAll(
                () -> verify(memberDao, times(1)).findById(anyLong()),
                () -> assertThat(foundMember.getId()).isEqualTo(member.getId())
        );
    }

    @Test
    @DisplayName("성공 : 해당 이메일과 비밀번호를 가진 회원을 조회할 수 있다")
    void should_FindMemberByEmailAndPassword() {
        //given
        Member member = new Member(1L, "콜리", "email@naver.com", "psssword", "USER");
        Mockito.when(memberDao.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(member));

        //when
        Member foundMember = memberService.findMemberByEmailAndPassword("email@naver.com", "password");

        //then
        assertAll(
                () -> verify(memberDao, times(1)).findByEmailAndPassword(anyString(), anyString()),
                () -> assertThat(foundMember.getId()).isEqualTo(member.getId())
        );
    }

    @Test
    @DisplayName("실패 : 해당 이메일과 비밀번호를 가진 회원이 없을 경우 오류를 발생시킨다")
    void should_ThrowIllegalArgumentException_When_NoMatchedEmailAndPasswordMember() {
        //given
        Mockito.when(memberDao.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(Optional.ofNullable(null));

        //when - then
        assertThatThrownBy(() -> memberService.findMemberByEmailAndPassword("email@naver.com", "password"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("모든 회원을 조회할 수 있다")
    void should_FindAllMembers() {
        //given
        Member member = new Member(1L, "콜리", "email@naver.com", "psssword", "USER");
        Mockito.when(memberDao.findAll())
                .thenReturn(List.of(member));

        //when
        List<Member> allMembers = memberService.findAllMembers();

        //then
        assertAll(
                () -> verify(memberDao, times(1)).findAll(),
                () -> assertThat(allMembers).hasSize(1)
        );
    }
}