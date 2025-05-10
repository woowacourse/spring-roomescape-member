package roomescape.member.application.dto;

import roomescape.member.domain.Role;

public record CreateMemberRequest(String name,
                                  String email,
                                  String password,
                                  Role role) {
}
