package roomescape.service.dto;

import java.time.LocalTime;
import roomescape.domain.exception.InvalidDomainException;

public class ReservationTimeCreateCommand {
    private final LocalTime startAt;

    public ReservationTimeCreateCommand(LocalTime startAt) {
        validate(startAt);
        this.startAt = startAt;
    }


    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidDomainException("예약 시간은 비어 있을 수 없습니다.");
        }
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
