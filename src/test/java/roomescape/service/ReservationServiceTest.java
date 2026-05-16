package roomescape.service;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationUpdateRequest;
import roomescape.exception.ErrorCode;
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
        String username = "토리";
        ReservationRequest registerRequest = new ReservationRequest(username, LocalDate.now().plusDays(1L), 3L, 3L);
        Long reservationId = reservationService.register(registerRequest).id();

        // when
        Assertions.assertThatCode(() -> reservationService.cancelByIdAndName(reservationId, username))
                .doesNotThrowAnyException();
    }

    @Test
    public void 일반_사용자는_다른_사용자의_예약은_취소할_경우_예외가_발생한다() {
        // given
        String username = "토리";
        ReservationRequest registerRequest = new ReservationRequest(username, LocalDate.now().plusDays(1L), 3L, 3L);
        Long reservationId = reservationService.register(registerRequest).id();
        String otherName = "윤기";

        // when
        Assertions.assertThatThrownBy(() -> reservationService.cancelByIdAndName(reservationId, otherName))
                .isInstanceOf(RoomescapeException.class)
                .hasMessageContaining(ErrorCode.RESERVATION_NOT_OWNER.getMessage());
    }

    @Test
    public void 사용자는_이미_지난_예약을_취소하면_예외가_발생한다() {
        // given
        ReservationResponse response = reservationService.read().getFirst();
        Long reservationId = response.id();
        String username = response.name();

        // when
        Assertions.assertThatThrownBy(() -> reservationService.cancelByIdAndName(reservationId, username))
                .isInstanceOf(RoomescapeException.class)
                .hasMessageContaining(ErrorCode.RESERVATION_PAST_UPDATE.getMessage());
    }

    @Test
    public void 예약을_정상적으로_변경할_수_있다() {
        // given
        ReservationRequest registerRequest = new ReservationRequest("토리", LocalDate.now().plusDays(1L), 1L, 1L);
        Long id = reservationService.register(registerRequest).id();
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(2L), 2L);

        // when
        Assertions.assertThatCode(() -> reservationService.update(id, "토리", updateRequest))
                .doesNotThrowAnyException();
    }

    @Test
    public void 존재하지_않는_예약을_변경하면_예외가_발생한다() {
        // given
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(1L), 1L);

        // when
        Assertions.assertThatThrownBy(() -> reservationService.update(-1L, "토리", updateRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    public void 이미_지난_예약을_변경하면_예외가_발생한다() {
        // given
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(1L), 1L);

        // when
        Assertions.assertThatThrownBy(() -> reservationService.update(1L, "토리", updateRequest))
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
        Assertions.assertThatThrownBy(() -> reservationService.update(id, "토리", updateRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    public void 같은_값으로_예약을_변경_해도_본인_예약은_중복으로_막히지_않는다() {
        // given
        String username = "토리";
        ReservationRequest registerRequest = new ReservationRequest(username, LocalDate.now().plusDays(1L), 1L, 1L);
        Long id = reservationService.register(registerRequest).id();
        ReservationUpdateRequest sameValueRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(1L), 1L);

        // when & then
        Assertions.assertThatCode(() -> reservationService.update(id, username, sameValueRequest))
                .doesNotThrowAnyException();
    }
}
