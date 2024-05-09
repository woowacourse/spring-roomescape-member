package roomescape.domain.member.domain;

import roomescape.global.exception.RoomEscapeException;

import java.util.Objects;

public class ReservationMember {

    private Long id;
    private String name;

    public ReservationMember(Long id, String name) {
        checkNull(name);
        this.id = id;
        this.name = name;
    }

    private void checkNull(String name) {
        try {
            Objects.requireNonNull(name, "[ERROR] 이름은 null일 수 없습니다.");
        } catch (NullPointerException e) {
            throw new RoomEscapeException(e.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
