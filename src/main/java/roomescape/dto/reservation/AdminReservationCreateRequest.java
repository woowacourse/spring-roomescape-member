package roomescape.dto.reservation;

import java.util.Objects;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public class AdminReservationCreateRequest {

    private final String date;
    private final Long memberId;
    private final Long timeId;
    private final Long themeId;

    private AdminReservationCreateRequest(String date, Long memberId, Long timeId, Long themeId) {
        this.date = date;
        this.memberId = memberId;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public static AdminReservationCreateRequest of(String date, Long memberId, Long timeId, Long themeId) {
        return new AdminReservationCreateRequest(date, memberId, timeId, themeId);
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

    public Long getMemberId() {
        return memberId;
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
        AdminReservationCreateRequest other = (AdminReservationCreateRequest) o;
        return Objects.equals(this.date, other.date)
                && Objects.equals(this.memberId, other.memberId)
                && Objects.equals(this.timeId, other.timeId)
                && Objects.equals(this.themeId, other.themeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, memberId, timeId, themeId);
    }

    @Override
    public String toString() {
        return "ReservationCreateRequest{" +
                ", date='" + date + '\'' +
                ", memberId='" + memberId + '\'' +
                ", timeId=" + timeId +
                ", themeId=" + themeId +
                '}';
    }
}
