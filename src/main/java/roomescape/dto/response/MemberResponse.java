package roomescape.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;

public record MemberResponse(

        @Schema(description = "멤버 ID", example = "1")
        Long id,

        @Schema(description = "멤버 이름", example = "체체")
        String name,

        @Schema(description = "멤버 이메일", example = "email@email.com")
        String email,

        @Schema(description = "멤버 권한", example = "USER")
        MemberRole memberRole
) {
    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getMemberRole());
    }
}
