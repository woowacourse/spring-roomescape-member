package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.request.MemberRequest;
import roomescape.error.MemberException;
import roomescape.stub.StubMemberRepository;

class MemberServiceTest {

    private final StubMemberRepository stubMemberRepository = new StubMemberRepository();
    private final MemberService sut = new MemberService(stubMemberRepository);

    @Test
    @DisplayName("멤버를 저장한다")
    void saveMember() {
        // given
        var request = new MemberRequest("홍길동", "hong@example.com", "pw123");

        // when
        sut.saveMember(request);
        var member = stubMemberRepository.findByEmail(request.email()).get();

        // then
        assertSoftly(soft -> {
            soft.assertThat(member.getId()).isNotNull();
            soft.assertThat(member.getName()).isEqualTo(request.name());
            soft.assertThat(member.getEmail()).isEqualTo(request.email());
            soft.assertThat(member.getPassword()).isEqualTo(request.password());
        });
    }

    @Test
    @DisplayName("저장된 이메일일 경우 예외를 던진다")
    void saveMember_duplicateEmail() {
        // given
        var request = new MemberRequest("홍길동", "hong@example.com", "pw123");
        sut.saveMember(request);

        // when & then
        assertThatThrownBy(() -> sut.saveMember(request))
                .isInstanceOf(MemberException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("모든 멤버를 조회한다")
    void findAllMember() {
        // given
        var request1 = new MemberRequest("홍길동", "hong@example.com", "pw123");
        var request2 = new MemberRequest("머피", "joon6093@example.com", "pw123");
        sut.saveMember(request1);
        sut.saveMember(request2);

        // when
        var members = sut.findAllMember();

        // then
        assertThat(members).hasSize(2);
    }
}
