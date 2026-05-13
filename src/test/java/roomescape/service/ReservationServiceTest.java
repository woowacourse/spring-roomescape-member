package roomescape.service;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationUpdateRequest;
import roomescape.exception.RoomescapeException;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void 존재하지_않는_예약을_삭제할_경우_예외가_발생한다() {
        // when
        Assertions.assertThatThrownBy(() -> reservationService.removeById(-1L))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 존재하는_예약을_추가할_경우_예외가_발생한다() {
        // given
        ReservationRequest reservationRequest = new ReservationRequest("포비", LocalDate.of(2026, 5, 1), 1L, 1L);

        // when
        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 존재하지_않는_예약을_정상적으로_추가할_수_있다() {
        // given
        ReservationRequest reservationRequest = new ReservationRequest("무빙", LocalDate.now().plusDays(1L), 2L, 2L);

        // when
        Assertions.assertThatCode(() -> reservationService.register(reservationRequest))
                .doesNotThrowAnyException();
    }

    @Test
    public void 이미_지난_날짜_시간으로_예약할_경우_예외가_발생한다() {
        // given
        ReservationRequest reservationRequest = new ReservationRequest("토리", LocalDate.of(2026, 5, 1), 2L, 2L);
        // when
        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    public void 존재하지_않는_예약_시간으로_예약은_예외가_발생한다() {
        // given
        ReservationRequest reservationRequest = new ReservationRequest("토리임", LocalDate.now().plusDays(1L), -1L, 2L);
        // when
        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    public void 존재하지_않는_테마로의_예약은_예외가_발생한다() {
        // given
        ReservationRequest reservationRequest = new ReservationRequest("토리임", LocalDate.now().plusDays(1L), 1L, -2L);
        // when
        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    public void 사용자는_자신의_예약을_취소할_수_있다() {
        // given
        Long reservationId = 1L;
        String username = "토리";

        // when
        Assertions.assertThatThrownBy(() -> reservationService.removeByIdAndName(reservationId, username))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    public void 다른_사용자가_예약을_취소에_대한_요청은_예외가_발생한다() {
        // given
        Long reservationId = 1L;
        String username = "수라";

        // when
        Assertions.assertThatThrownBy(() -> reservationService.removeByIdAndName(reservationId, username))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    public void 이미_지난_예약을_취소하면_예외가_발생한다() {
        // given
        Long reservationId = 1L;

        // when
        Assertions.assertThatThrownBy(() -> reservationService.removeById(reservationId))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    public void 예약을_정상적으로_변경할_수_있다() {
        // given
        ReservationRequest registerRequest = new ReservationRequest("토리", LocalDate.now().plusDays(1L), 1L, 1L);
        Long id = reservationService.register(registerRequest).id();
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(2L), 2L);

        // when
        Assertions.assertThatCode(() -> reservationService.update(id, updateRequest))
                .doesNotThrowAnyException();
    }

    @Test
    public void 존재하지_않는_예약을_변경하면_예외가_발생한다() {
        // given
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(1L), 1L);

        // when
        Assertions.assertThatThrownBy(() -> reservationService.update(-1L, updateRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    public void 이미_지난_예약을_변경하면_예외가_발생한다() {
        // given
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(1L), 1L);

        // when
        Assertions.assertThatThrownBy(() -> reservationService.update(1L, updateRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    public void 변경하려는_날짜_시간에_이미_예약이_존재하면_예외가_발생한다() {
        // given
        ReservationRequest registerRequest = new ReservationRequest("토리", LocalDate.now().plusDays(1L), 1L, 1L);
        Long id = reservationService.register(registerRequest).id();
        ReservationRequest anotherRequest = new ReservationRequest("포비", LocalDate.now().plusDays(2L), 2L, 1L);
        reservationService.register(anotherRequest);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(2L), 2L);

        // when
        Assertions.assertThatThrownBy(() -> reservationService.update(id, updateRequest))
                .isInstanceOf(RoomescapeException.class);
    }

}
