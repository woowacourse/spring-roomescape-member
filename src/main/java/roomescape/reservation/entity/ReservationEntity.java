package roomescape.reservation.entity;

import roomescape.exception.BadRequestException;
import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public boolean isDuplicatedWith(ReservationEntity other) {
        return date.isEqual(other.date) && time.isDuplicatedWith(other.time);
    }

    public String getFormattedDate() {
        return date.toString();
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
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
