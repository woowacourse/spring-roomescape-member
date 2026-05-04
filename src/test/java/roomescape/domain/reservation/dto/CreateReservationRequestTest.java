package roomescape.domain.reservation.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import roomescape.support.exception.RoomescapeException;

class CreateReservationRequestTest {

    @Test
    void 이름이_null이면_예외가_발생한다() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
            null,
            LocalDate.of(2023, 8, 5),
            1L
        );

        // when & then
        assertThatThrownBy(request::validate)
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 이름이_공백이면_예외가_발생한다() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
            "   ",
            LocalDate.of(2023, 8, 5),
            1L
        );

        // when & then
        assertThatThrownBy(request::validate)
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 날짜가_null이면_예외가_발생한다() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            null,
            1L
        );

        // when & then
        assertThatThrownBy(request::validate)
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("날짜는 필수입니다.");
    }

    @Test
    void 시간_id가_null이면_예외가_발생한다() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            LocalDate.of(2023, 8, 5),
            null
        );

        // when & then
        assertThatThrownBy(request::validate)
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("시간은 필수입니다.");
    }
}
