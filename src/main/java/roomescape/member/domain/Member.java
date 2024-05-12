package roomescape.member.domain;

import roomescape.global.exception.RoomEscapeException;

import java.util.Objects;

public class Member {

    private ReservationMember reservationMember;
    private String email;
    private String password;
    private Role role;

    public Member(ReservationMember reservationMember, String email, String password, Role role) {
        checkNull(email, password, role);
        this.reservationMember = reservationMember;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    private void checkNull(String email, String password, Role role) {
        try {
            Objects.requireNonNull(email, "[ERROR] 이메일은 null일 수 없습니다.");
            Objects.requireNonNull(password, "[ERROR] 비밀번호는 null일 수 없습니다.");
            Objects.requireNonNull(role, "[ERROR] 권한은 null일 수 없습니다.");
        } catch (NullPointerException e) {
            throw new RoomEscapeException(e.getMessage());
        }
    }
}
