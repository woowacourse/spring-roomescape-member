package roomescape.member.controller.dto;

import roomescape.member.service.dto.CreateMemberServiceRequest;

public record CreateMemberRequest(String email, String password, String name) {

    public CreateMemberServiceRequest toCreateMemberServiceRequest() {
        return new CreateMemberServiceRequest(this.email, this.password, this.name);
    }
}
