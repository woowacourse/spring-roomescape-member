package roomescape.time.domain;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class ReservationTime {

    private Long id;
    private LocalTime startAt;
    private boolean isActive;

    private ReservationTime(Long id, LocalTime startAt, boolean isActive) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
        this.isActive = isActive;
    }

    public static ReservationTime create(LocalTime startAt) {
        return new ReservationTime(null, startAt, false);
    }

    public static ReservationTime load(Long timeId, LocalTime startAt, boolean isActive) {
        validateId(timeId);
        return new ReservationTime(timeId, startAt, isActive);
    }

    private static void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시작 시간은 필수입니다.");
        }
    }

    private static void validateId(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("예약 시간 ID는 필수입니다.");
        }
    }

    public void updateStatus(boolean isActive) {
        this.isActive = isActive;
    }

}
