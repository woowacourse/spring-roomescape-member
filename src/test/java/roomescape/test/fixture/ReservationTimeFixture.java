package roomescape.test.fixture;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

public class ReservationTimeFixture {

    public static ReservationTime addReservationTimeInRepository(
            ReservationTimeRepository repository, LocalTime time) {
        ReservationTime reservationTime = ReservationTime.createWithoutId(time);
        long timeId = repository.add(reservationTime);
        return repository.findById(timeId).get();
    }
}
