package roomescape.dto.reservation;

import java.util.Objects;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.dto.member.MemberNameResponse;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

public class ReservationResponse {

    private final Long id;
    private final MemberNameResponse member;
    private final String date;
    private final ReservationTimeResponse time;
    private final ThemeResponse theme;

    private ReservationResponse(Long id,
                                MemberNameResponse member,
                                String date,
                                ReservationTimeResponse time,
                                ThemeResponse theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static ReservationResponse from(Reservation reservation) {
        ReservationDate reservationDate = reservation.getDate();
        return new ReservationResponse(
                reservation.getId(),
                new MemberNameResponse(reservation.getMember()),
                reservationDate.toStringDate(),
                ReservationTimeResponse.from(reservation.getReservationTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    public static ReservationResponse of(Long id,
                                         MemberNameResponse member,
                                         String date,
                                         ReservationTimeResponse timeResponse,
                                         ThemeResponse themeResponse) {
        return new ReservationResponse(id, member, date, timeResponse, themeResponse);
    }

    public Long getId() {
        return id;
    }

    public MemberNameResponse getMember() {
        return member;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponse getTime() {
        return time;
    }

    public ThemeResponse getTheme() {
        return theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationResponse other = (ReservationResponse) o;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.member, other.member)
                && Objects.equals(this.date, other.date)
                && Objects.equals(this.time, other.time)
                && Objects.equals(this.theme, other.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, date, time, theme);
    }

    @Override
    public String toString() {
        return "ReservationResponse{" +
                "id=" + id +
                ", member='" + member + '\'' +
                ", date='" + date + '\'' +
                ", time=" + time +
                ", theme=" + theme +
                '}';
    }
}
