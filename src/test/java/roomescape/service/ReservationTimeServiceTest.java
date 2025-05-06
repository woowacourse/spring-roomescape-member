package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.AvailableReservationTime;
import roomescape.domain.ReservationTime;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.InUseException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.service.dto.ReservationTimeCreation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("시간 데이터를 추가할 수 있어야 한다")
    void addReservationTime() {
        // given // when
        final ReservationTimeCreation creation = new ReservationTimeCreation(LocalTime.of(11, 0));
        reservationTimeService.addReservationTime(creation);

        // then
        assertThat(reservationTimeService.findAllReservationTimes()).hasSize(3);
    }

    @Test
    @DisplayName("이미 존재하는 시간 데이터일 경우 예외를 던진다")
    void cannotAddReservationTime() {
        // given
        final ReservationTimeCreation creation = new ReservationTimeCreation(LocalTime.of(12, 0));

        // when // then
        assertThatThrownBy(() -> reservationTimeService.addReservationTime(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessage("이미 존재하는 예약 가능 시간입니다: %s".formatted(creation.startAt()));
    }

    @Test
    @DisplayName("시간 데이터를 조회할 수 있어야 한다")
    void findAllReservationTimes() {
        // given // when
        final List<ReservationTime> actual = reservationTimeService.findAllReservationTimes();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("이용가능한 예약 시간을 조회한다")
    void findAvailableTime() {
        // given // when
        final List<AvailableReservationTime> actual = reservationTimeService.findAllAvailableTime(
                LocalDate.of(2025, 4, 28), 1
        );

        // then
        assertThat(actual.getFirst().getBookedStatus()).isTrue();
        assertThat(actual.getLast().getBookedStatus()).isFalse();
    }

    @Test
    @DisplayName("id를 기반으로 시간 데이터를 삭제할 수 있다")
    void deleteById() {
        // given // when // then
        assertThatCode(() -> reservationTimeService.deleteById(2L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 시간을 삭제하는 경우 예외를 던진다")
    void deleteNotExistTimeById() {
        // given // when // then
        assertThatThrownBy(() -> reservationTimeService.deleteById(1000L))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 예약 시간입니다");
    }

    @Test
    @DisplayName("사용중인 예약 시간을 삭제하려는 경우 예외를 던진다")
    void deleteUsedTimeById() {
        // given // when // then
        assertThatThrownBy(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(InUseException.class)
                .hasMessageContaining("사용 중인 예약 시간입니다");
    }
}
