package roomescape.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import roomescape.domain.ReservationRepository;

@JdbcTest
class JdbcReservationRepositoryTest {

    private final ReservationRepository reservationRepository;

    public JdbcReservationRepositoryTest(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


}
