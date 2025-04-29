package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.reservation.domain.exception.PastReservationException;
import roomescape.time.domain.ReservationTime;

public class Reservation {

    private final Long id;
    private final ReserverName reserverName;
    private final ReservationDate reservationDate;
    private final ReservationTime reservationTime;

    public Reservation(
            Long id,
            String reserverName,
            LocalDate reservationDate,
            ReservationTime reservationTime
    ) {
        this.id = id;
        this.reserverName = new ReserverName(reserverName);
        this.reservationDate = new ReservationDate(reservationDate);
        this.reservationTime = reservationTime;
    }

    public static Reservation create(
            String reserverName,
            LocalDate reservationDate,
            ReservationTime reservationTime
    ) {
        validatePast(reservationDate, reservationTime);
        return new Reservation(null, reserverName, reservationDate, reservationTime);
    }

    private static void validatePast(LocalDate reservationDate, ReservationTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationTime.getStartAt());
        LocalDateTime now = LocalDateTime.now();

        if (reservationDateTime.isBefore(now) || reservationDateTime.isEqual(now)) {
            throw new PastReservationException("[ERROR] 현재 시간 이후로 예약할 수 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getReserverName() {
        return reserverName.getName();
    }

    public LocalDate getDate() {
        return reservationDate.getDate();
    }

    public LocalTime getStartAt() {
        return reservationTime.getStartAt();
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Long getTimeId() {
        return reservationTime.getId();
    }
}
