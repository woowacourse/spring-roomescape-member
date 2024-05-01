package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeJdbcRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("중복된 예약 시간 추가가 불가능한 지 확인한다.")
    void checkDuplicatedReservationTIme() {
        //given
        ReservationTime reservationTime1 = new ReservationTime(LocalTime.parse("10:00"));
        ReservationTime reservationTime2 = new ReservationTime(LocalTime.parse("10:00"));
        reservationTimeRepository.save(reservationTime1);

        //when & then
        assertThatThrownBy(() ->
            reservationTimeRepository.save(reservationTime2)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 추가된 예약 시간입니다.");
    }
}
