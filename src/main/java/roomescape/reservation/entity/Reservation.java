package roomescape.reservation.entity;

import roomescape.global.exception.badRequest.BadRequestException;
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

    public Reservation(Long id, Long memberId, LocalDate date, ReservationTime time, Long themeId) {
        if (memberId == null || date == null || time == null || themeId == null) {
            throw new BadRequestException("필요한 예약 정보가 모두 입력되지 않았습니다.");
        }
        this.id = id;
        this.memberId = memberId;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
    }

    public boolean isBefore(LocalDateTime compare) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        return dateTime.isBefore(compare);
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
