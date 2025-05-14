package roomescape.member.dto;

public record MemberSignUpResponse(
        String message
) {

    public static MemberSignUpResponse ofSuccess() {
        return new MemberSignUpResponse("회원가입에 성공하였습니다.");
    }
}
