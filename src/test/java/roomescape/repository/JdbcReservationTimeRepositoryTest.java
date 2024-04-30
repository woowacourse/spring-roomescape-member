package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestSetting.createReservationTime;
import static roomescape.TestSetting.isEqualsReservationTime;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JdbcReservationTimeRepositoryTest {

    @Autowired
    JdbcReservationTimeRepository jdbcReservationTimeRepository;

    @Test
    void 예약_시간_저장() {
        //given
        ReservationTime reservationTime = createReservationTime();

        //when
        Long savedId = jdbcReservationTimeRepository.save(reservationTime);

        //then
        ReservationTime savedReservationTime = jdbcReservationTimeRepository.findById(savedId);
        assertThat(isEqualsReservationTime(reservationTime, savedReservationTime)).isTrue();
    }

    @Test
    void 전체_예약_시간_조회() {
        //given
        ReservationTime reservationTime1 = createReservationTime();
        ReservationTime reservationTime2 = createReservationTime();
        jdbcReservationTimeRepository.save(reservationTime1);
        jdbcReservationTimeRepository.save(reservationTime2);

        //when
        List<ReservationTime> reservationTimes = jdbcReservationTimeRepository.findAll();

        //then
        assertThat(reservationTimes).usingElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrder(reservationTime1, reservationTime2);
    }

    @Test
    void 예약_시간_삭제() {
        //given
        ReservationTime reservationTime = createReservationTime();
        Long savedId = jdbcReservationTimeRepository.save(reservationTime);

        //when
        jdbcReservationTimeRepository.delete(savedId);

        //then
        List<ReservationTime> reservationTimes = jdbcReservationTimeRepository.findAll();
        assertThat(reservationTimes).usingElementComparatorIgnoringFields("id")
                .doesNotContain(reservationTime);
    }
}
