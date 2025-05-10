package roomescape.reservation.entity;

import roomescape.exception.badRequest.BadRequestException;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Reservation {
    private final Long id;
    private Long memberId;
    private LocalDate date;
    private ReservationTime time;
    private Long themeId;

    private Reservation(Long id, Long memberId, LocalDate date, ReservationTime time, Long themeId) {
        this.id = id;
        this.memberId = memberId;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
    }

    public static Reservation of(final Long id, Long memberId, LocalDate date, ReservationTime time, final Long themeId) {
        validateFields(id, memberId, date, time, themeId);
        return new Reservation(id, memberId, date, time, themeId);
    }

    public static Reservation create(final Long memberId, LocalDate date, ReservationTime time, final Long themeId) {
        validateFields(0L, memberId, date, time, themeId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if (dateTime.isBefore(now)) {
            throw new BadRequestException("과거 날짜/시간의 예약은 생성할 수 없습니다.");
        }
        return new Reservation(0L, memberId, date, time, themeId);
    }

    private static void validateFields(Long id, Long memberId, LocalDate date, ReservationTime time, Long themeId) {
        if (id == null || memberId == null || date == null || time == null || themeId == null) {
            throw new BadRequestException("필요한 예약 정보가 모두 입력되지 않았습니다.");
        }
    }

    public LocalTime getStartAt() {
        return time.getStartAt();
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Long getThemeId() {
        return themeId;
    }
}
