package roomescape.dto.reservation;

import java.util.Objects;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.dto.reservationtime.ReservationTimeResponse;

public class ReservationResponse {

    private final Long id;
    private final String name;
    private final String date;
    private final ReservationTimeResponse time;

    private ReservationResponse(Long id,
                                String name,
                                String date,
                                ReservationTimeResponse time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static ReservationResponse from(Reservation reservation) {
        ReservationDate reservationDate = reservation.getDate();
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName().getValue(),
                reservationDate.toStringDate(),
                ReservationTimeResponse.from(reservation.getReservationTime())
        );
    }

    public static ReservationResponse of(Long id, String name, String date, ReservationTimeResponse timeResponseDto) {
        return new ReservationResponse(id, name, date, timeResponseDto);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponse getTime() {
        return time;
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
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.date, other.date)
                && Objects.equals(this.time, other.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time);
    }

    @Override
    public String toString() {
        return "ReservationCreateResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", time=" + time +
                '}';
    }
}
