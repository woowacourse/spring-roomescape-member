package roomescape.member.dto;

import roomescape.global.exception.RoomEscapeException;

import java.util.Objects;

public record LoginRequest(
        String email,
        String password
) {
    public LoginRequest {
        try {
            Objects.requireNonNull(email, "[ERROR] 이메일은 null일 수 없습니다.");
            Objects.requireNonNull(password, "[ERROR] 비밀번호는 null일 수 없습니다.");
        } catch (NullPointerException e) {
            throw new RoomEscapeException(e.getMessage());
        }
    }
}
