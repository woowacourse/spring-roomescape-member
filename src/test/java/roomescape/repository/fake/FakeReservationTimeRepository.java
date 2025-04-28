package roomescape.repository.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {
    private final ReservationTimes reservationTimes = new ReservationTimes();
    private final AtomicLong id = new AtomicLong(1);

    @Override
    public ReservationTime saveReservationTime(ReservationTime reservationTime) {
        ReservationTime createdReservationTime = ReservationTime.generateWithPrimaryKey(reservationTime,
                id.getAndIncrement());
        reservationTimes.add(createdReservationTime);
        return createdReservationTime;
    }

    @Override
    public List<ReservationTime> readReservationTimes() {
        return reservationTimes.getReservationTimes();
    }

    @Override
    public Optional<ReservationTime> readReservationTime(Long id) {
        return Optional.ofNullable(
                reservationTimes.getReservationTimes().stream()
                        .filter(reservationTime -> reservationTime.isSameId(id))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("일치하는 예약 시간 ID를 찾을 수 없습니다."))
        );
    }

    @Override
    public void deleteReservationTime(Long id) {
        reservationTimes.remove(id);
    }

    public static class ReservationTimes {
        private final List<ReservationTime> reservationTimes;

        public ReservationTimes() {
            this.reservationTimes = new ArrayList<>();
        }

        public void add(ReservationTime reservationTime) {
            reservationTimes.add(reservationTime);
        }

        public void remove(Long id) {
            boolean removed = reservationTimes.removeIf(reservationTime -> reservationTime.isSameId(id));
            if (!removed) {
                throw new IllegalArgumentException("일치하는 ID의 예약을 찾을 수 없습니다.");
            }
        }

        public List<ReservationTime> getReservationTimes() {
            return new ArrayList<>(reservationTimes);
        }
    }
}
