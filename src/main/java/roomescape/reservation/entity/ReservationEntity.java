package roomescape.reservation.entity;

import roomescape.exception.BadRequestException;
import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationEntity {
    private final Long id;
    private String name;
    private LocalDate date;
    private ReservationTimeEntity time;
    private Long themeId;

    public ReservationEntity(Long id, String name, LocalDate date, ReservationTimeEntity time, Long themeId) {
        if (id == null || name == null || date == null || time == null || themeId == null) {
            throw new BadRequestException("필요한 예약 정보가 모두 입력되지 않았습니다.");
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeId = themeId;
    }

    public boolean isBefore(LocalDateTime other) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        return dateTime.isBefore(other);
    }

    public LocalTime getStartAt() {
        return time.getStartAt();
    }

    public String getFormattedDate() {
        return date.toString();
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTimeEntity getTime() {
        return time;
    }

    public Long getThemeId() {
        return themeId;
    }
}
