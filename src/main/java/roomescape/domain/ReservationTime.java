package roomescape.domain;

import java.time.LocalTime;
import roomescape.domain.exception.InvalidDomainException;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(Long id, LocalTime startAt) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    // 새 시간 생성 (저장 전)
    public static ReservationTime create(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    // DB 재구성 (저장 후)
    public static ReservationTime reconstitute(Long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }

    private static void validate(LocalTime startAt) {
        validateStartAt(startAt);
    }

    private static void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidDomainException("예약 시간은 비어 있을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
