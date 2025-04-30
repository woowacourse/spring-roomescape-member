package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.service.dto.ReservationTimeCreation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("시간 데이터를 조회할 수 있어야 한다")
    void findAll() {
        //given
        //when
        List<ReservationTime> actual = reservationTimeService.findAll();

        //then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("시간 데이터를 추가할 수 있어야 한다")
    void addReservationTime() {
        //given
        //when
        ReservationTimeCreation creation = new ReservationTimeCreation(LocalTime.of(11, 0));
        reservationTimeService.addReservationTime(creation);

        //then
        assertThat(reservationTimeService.findAll()).hasSize(3);
    }

    @Test
    @DisplayName("이미 존재하는 시간 데이터일 경우 예외를 던진다")
    void cannotAddReservationTime() {
        //given
        ReservationTimeCreation creation = new ReservationTimeCreation(LocalTime.of(12, 0));

        //when & then
        assertThatThrownBy(() -> reservationTimeService.addReservationTime(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessage("이미 존재하는 예약 가능 시간입니다: %s".formatted(creation.startAt()));
    }

    @Test
    @DisplayName("id를 기반으로 시간 데이터를 삭제할 수 있어야 한다")
    void deleteById() {
        //given
        //when
        // then
        assertThatCode(() -> reservationTimeService.deleteById(2L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 id를 기반으로 시간 데이터를 삭제하면 false를 리턴해야 한다")
    void deleteNotExistTimeById() {
        //given
        //when
        //then
        assertThatThrownBy(() -> reservationTimeService.deleteById(1000L))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 예약 시간입니다");
    }
}
