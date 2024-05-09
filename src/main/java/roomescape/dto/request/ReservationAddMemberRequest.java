package roomescape.dto.request;

import roomescape.dto.response.MemberResponse;

public record ReservationAddMemberRequest(Long id, String name, String email) {
    public ReservationAddMemberRequest(MemberResponse memberResponse) {
        this(memberResponse.id(), memberResponse.name(), memberResponse.email());
    }
}
