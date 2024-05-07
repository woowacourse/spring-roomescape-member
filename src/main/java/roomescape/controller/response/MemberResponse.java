package roomescape.controller.response;

import roomescape.domain.User;

public record MemberResponse(Long id, String name, String email, String password) {
    public static MemberResponse from(User user) {
        return new MemberResponse(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
}
