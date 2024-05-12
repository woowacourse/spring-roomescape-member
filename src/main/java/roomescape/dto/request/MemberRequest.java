package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import roomescape.domain.member.Member;

public record MemberRequest(
        @PositiveOrZero(message = "회원 id는 0이상이어야 해요") Long id,
        @NotBlank(message = "회원 이름을 입력해주세요") String name,
        @NotBlank(message = "회원 이메일을 입력해주세요") String email,
        @NotBlank(message = "회원 비밀번호를 입력해주세요") String password,
        @NotBlank(message = "회원의 권한이 설정되지 않았어요") String role
) {
    public MemberRequest(Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }
}
