package roomescape.service;

import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class FakeReservationTimeRepository implements ReservationTimeRepository {

    private List<ReservationTime> reservationTimes = new ArrayList<>(List.of(
            new ReservationTime(1, LocalTime.of(10, 0)),
            new ReservationTime(2, LocalTime.of(11, 0))
    ));

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimes;
    }

    @Override
    public ReservationTime findReservationById(long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당하는 아이디가 없습니다."));
    }

    @Override
    public ReservationTime addReservationTime(ReservationTime reservationTime) {
        reservationTimes.add(reservationTime);
        return new ReservationTime(3, reservationTime.getStartAt());
    }

    @Override
    public void deleteReservationTime(long id) {
        ReservationTime findReservationTime = reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당하는 아이디가 없습니다."));

        reservationTimes.remove(findReservationTime);
    }

    @Override
    public Long countReservationTimeById(long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId() == id)
                .count();
    }

    @Override
    public Long countReservationTimeByStartAt(LocalTime startAt) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getStartAt() == startAt)
                .count();
    }
}
