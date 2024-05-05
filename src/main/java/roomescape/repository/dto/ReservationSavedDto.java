package roomescape.repository.dto;

import java.time.LocalDate;
import java.util.Objects;

public class ReservationSavedDto {

    private final long id;
    private final String name;
    private final LocalDate date;
    private final long timeId;
    private final long themeId;

    public ReservationSavedDto(long id, String name, LocalDate date, long timeId, long themeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationSavedDto that = (ReservationSavedDto) o;
        return id == that.id && timeId == that.timeId && themeId == that.themeId && Objects.equals(name, that.name) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, timeId, themeId);
    }
}
