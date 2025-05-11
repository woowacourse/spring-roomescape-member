package roomescape.time.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.time.exception.ReservationTimeDuplicateException;
import roomescape.fake.ReservationTimeFakeRepository;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ReservationTimeServiceTest {

    private final ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
    private final ReservationTimeService reservationService = new ReservationTimeService(reservationTimeRepository);

    @Test
    @DisplayName("조회된 엔티티를 DTO로 매핑해 반환한다.")
    void test_readReservationTimes() {
        //given & when
        List<ReservationTimeResponse> actual = reservationService.readReservationTimes();
        //then
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.getFirst().startAt()).isEqualTo(LocalTime.MAX);
    }

    @Test
    @DisplayName("엔티티를 저장한 후, DTO로 반환한다.")
    void test_createReservationTime() {
        //given
        LocalTime time = LocalTime.MIN;
        ReservationTimeRequest request = new ReservationTimeRequest(time);
        //when
        ReservationTimeResponse actual = reservationService.createReservationTime(request);
        //then
        assertThat(actual.startAt()).isEqualTo(time);
    }

    @Test
    @DisplayName("존재하지 않는 id로 삭제하려고 할 경우, 예외가 발생한다.")
    void error_deleteReservationTimeById() {
        assertThatThrownBy(() -> reservationService.deleteReservationTime(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("존재하는 id로 삭제 시, 성공적으로 삭제한다.")
    void test_deleteReservationTimeById() {
        // given
        List<ReservationTimeResponse> beforeDelete = reservationService.readReservationTimes();
        long idToDelete = beforeDelete.get(0).id();

        // when
        reservationService.deleteReservationTime(idToDelete);

        // then
        List<ReservationTimeResponse> afterDelete = reservationService.readReservationTimes();
        boolean isDeleted = afterDelete.stream()
                .noneMatch(time -> time.id() == idToDelete);
        assertThat(afterDelete.size()).isEqualTo(beforeDelete.size() - 1);
        assertThat(isDeleted).isTrue();
    }

    @Test
    @DisplayName("예약 시간 생성 시, 중복된 시간일 경우 예외가 발생한다.")
    void error_createReservationTimeIfDuplicationDatetime() {
        //given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.MAX);
        //when&then
        assertThatThrownBy(() -> reservationService.createReservationTime(request))
                .isInstanceOf(ReservationTimeDuplicateException.class)
                .hasMessageContaining("중복된 예약 시간이 존재합니다.");
    }
}
