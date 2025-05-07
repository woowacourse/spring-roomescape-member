package roomescape.controller.request;

public record LoginMemberInfo(String name) {

    public static LoginMemberInfo of(String name) {
        return new LoginMemberInfo(name);
    }
}
