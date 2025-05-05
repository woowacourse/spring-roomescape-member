package roomescape.time.entity;

import roomescape.exception.badRequest.BadRequestException;

import java.time.LocalTime;

public class ReservationTime {
    private static final LocalTime OPERATING_START = LocalTime.of(10, 0);
    private static final LocalTime OPERATING_END = LocalTime.of(22, 0);

    private final Long id;
    private LocalTime startAt;

    private ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime create(LocalTime startAt) {
        if (startAt.isAfter(OPERATING_END) || startAt.isBefore(OPERATING_START)) {
            throw new BadRequestException("운영 시간 이외의 시간이 입력되었습니다.");
        }
        return ReservationTime.of(0L, startAt);
    }

    public static ReservationTime of(final Long id, LocalTime startAt) {
        validateFields(id, startAt);
        return new ReservationTime(id, startAt);
    }

    private static void validateFields(Long id, LocalTime startAt) {
        if (id == null || startAt == null) {
            throw new BadRequestException("필요한 시간 정보가 모두 입력되지 않았습니다.");
        }
    }

    public String getFormattedTime() {
        return startAt.toString();
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
