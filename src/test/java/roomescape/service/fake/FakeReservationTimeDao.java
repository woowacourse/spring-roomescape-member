package roomescape.service.fake;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import roomescape.model.ReservationTime;
import roomescape.repository.dao.ReservationTimeDao;

public class FakeReservationTimeDao implements ReservationTimeDao {

    private final List<ReservationTime> reservationTimes = new ArrayList<>();

    public void add(ReservationTime reservationTime) {
        reservationTimes.add(reservationTime);
    }

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimes;
    }

    @Override
    public List<ReservationTime> findAllReservedTimes(LocalDate date, long themeId) {
        return List.of(reservationTimes.get(1));
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
        return new ReservationTime(3L, reservationTime.getStartAt());
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
