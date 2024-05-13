package roomescape.member.domain;

import roomescape.global.exception.RoomEscapeException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static roomescape.global.exception.ExceptionMessage.NAME_CANNOT_NULL;

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
            Objects.requireNonNull(name, NAME_CANNOT_NULL.getMessage());
        } catch (NullPointerException e) {
            throw new RoomEscapeException(BAD_REQUEST, e.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
