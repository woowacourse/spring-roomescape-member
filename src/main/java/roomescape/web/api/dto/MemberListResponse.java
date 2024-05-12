package roomescape.web.api.dto;

import java.util.List;

public class MemberListResponse {
    private List<?> members;

    private MemberListResponse() {
    }

    public MemberListResponse(List<?> members) {
        this.members = members;
    }

    public List<?> getMembers() {
        return members;
    }
}
