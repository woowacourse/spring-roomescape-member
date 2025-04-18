package roomescape.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reservations {

    private final List<Reservation> reservations;

    public Reservations() {
        this.reservations = new ArrayList<>();
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public void save(Reservation reservation) {
        reservations.add(reservation);
    }

    public void deleteById(long id) {
        Reservation reservation = findById(id);
        reservations.remove(reservation);
    }

    private Reservation findById(long id) {
        return reservations.stream()
            .filter(reservation -> reservation.id() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약번호 입니다."));
    }
}
