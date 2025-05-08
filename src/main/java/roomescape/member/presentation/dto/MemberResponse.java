package roomescape.member.presentation.dto;

public class MemberResponse {
    private String name;

    public MemberResponse() {
    }

    public MemberResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}