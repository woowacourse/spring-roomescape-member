package roomescape.service.dto;

public class MemberResponse {

    private final String name;

    public MemberResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
