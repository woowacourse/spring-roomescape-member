package roomescape.repository.dto;

import java.time.LocalDate;
import java.util.Objects;

public class ReservationSavedDto {

    private final long id;
    private final LocalDate date;
    private final long timeId;
    private final long themeId;
    private final long memberId;

    public ReservationSavedDto(long id, LocalDate date, long timeId, long themeId, long memberId) {
        this.id = id;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
        this.memberId = memberId;
    }

    public ReservationSavedDto(LocalDate date, long timeId, long themeId, long memberId) {
        this.id = 0;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
        this.memberId = memberId;
    }

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getTimeId() {
        return timeId;
    }

    public long getThemeId() {
        return themeId;
    }

    public long getMemberId() {
        return memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationSavedDto that = (ReservationSavedDto) o;
        return id == that.id && timeId == that.timeId && themeId == that.themeId && memberId == that.memberId && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, timeId, themeId, memberId);
    }

    @Override
    public String toString() {
        return "ReservationSavedDto{" +
                "id=" + id +
                ", date=" + date +
                ", timeId=" + timeId +
                ", themeId=" + themeId +
                ", memberId=" + memberId +
                '}';
    }
}
