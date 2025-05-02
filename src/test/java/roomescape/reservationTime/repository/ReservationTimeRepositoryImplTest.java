package roomescape.reservationTime.repository;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.globalException.NotFoundException;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.fixture.ReservationTimeFixture;

@JdbcTest
@Import({ReservationTimeRepositoryImpl.class})
class ReservationTimeRepositoryImplTest {

    @Autowired
    private ReservationTimeRepositoryImpl repository;

    @DisplayName("존재하지 않는 예약 시간 ID로 조회하면 예외가 발생한다.")
    @Test
    void findById_throwsExceptionByNonExistentId() {
        // given
        LocalTime dummyTime1 = LocalTime.of(12, 10, 13);
        ReservationTime reservationTime1 = ReservationTimeFixture.create(dummyTime1);

        LocalTime dummyTime2 = LocalTime.of(14, 54, 32);
        ReservationTime reservationTime2 = ReservationTimeFixture.create(dummyTime2);

        List<ReservationTime> reservationTimes = List.of(reservationTime1, reservationTime2);

        for (ReservationTime reservationTime : reservationTimes) {
            repository.add(reservationTime);
        }

        // when & then
        Assertions.assertThatCode(
                () -> repository.findByIdOrThrow(Long.MAX_VALUE)
        ).isInstanceOf(NotFoundException.class);
    }
}
