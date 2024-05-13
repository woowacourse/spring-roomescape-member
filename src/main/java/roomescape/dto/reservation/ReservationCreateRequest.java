package roomescape.dto.reservation;

import java.util.Objects;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public class ReservationCreateRequest {

    private final String date;
    private final Long timeId;
    private final Long themeId;

    private ReservationCreateRequest(String date, Long timeId, Long themeId) {
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public static ReservationCreateRequest of(String date, Long timeId, Long themeId) {
        return new ReservationCreateRequest(date, timeId, themeId);
    }

    public Reservation toDomain(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                null,
                member,
                ReservationDate.from(date),
                reservationTime,
                theme
        );
    }

    public String getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationCreateRequest other = (ReservationCreateRequest) o;
        return Objects.equals(this.date, other.date)
                && Objects.equals(this.timeId, other.timeId)
                && Objects.equals(this.themeId, other.themeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, timeId, themeId);
    }

    @Override
    public String toString() {
        return "ReservationCreateRequest{" +
                ", date='" + date + '\'' +
                ", timeId=" + timeId +
                ", themeId=" + themeId +
                '}';
    }
}
