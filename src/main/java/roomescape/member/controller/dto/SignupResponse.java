package roomescape.member.controller.dto;

public record SignupResponse(String name) {

    public static SignupResponse from(String name) {
        return new SignupResponse(name);
    }

}
