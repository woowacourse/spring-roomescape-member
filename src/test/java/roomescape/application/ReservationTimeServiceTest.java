package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import roomescape.application.dto.request.ReservationTimeCreationRequest;
import roomescape.application.dto.response.AvailableTimeResponse;
import roomescape.application.dto.response.ReservationTimeResponse;
import roomescape.support.extension.TableTruncateExtension;

@SpringBootTest
@ExtendWith(TableTruncateExtension.class)
public class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약_시간을_성공적으로_등록한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(startAt);

        ReservationTimeResponse response = reservationTimeService.register(request);

        assertThat(response.startAt()).isEqualTo(startAt);
    }

    @Test
    void 중복된_예약_시간이_있으면_등록을_실패한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(startAt);
        reservationTimeService.register(request);

        assertThatThrownBy(() -> reservationTimeService.register(request))
                .isExactlyInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void 예약_시간을_삭제한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(startAt);
        ReservationTimeResponse response = reservationTimeService.register(request);

        reservationTimeService.delete(response.id());

        assertThat(reservationTimeService.findReservationTimes()).isEmpty();
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_예외가_발생한다() {
        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 예약 시간입니다.");
    }

    @Test
        // todo sql
    void 예약에서_사용_중인_시간을_삭제하면_예외가_발생한다() {
//        LocalTime startAt = LocalTime.of(13, 0);
//        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(startAt);
//        ReservationTimeResponse reservationTime = reservationTimeService.register(request);
//        Theme theme = themeRepository.save(ThemeFixture.theme());
//        reservationRepository.save(ReservationFixture.reservation("prin", "2024-04-30", reservationTime, theme));
//
//        assertThatThrownBy(() -> reservationTimeService.delete(reservationTime.getId()))
//                .isExactlyInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("해당 시간을 사용하는 예약이 존재합니다");
    }

    @Test
    @Sql("/reservation.sql")
    void 테마의_예약_가능한_시간을_조회한다() {
        List<AvailableTimeResponse> availableTimes = reservationTimeService.findAvailableTimes(2L,
                LocalDate.parse("2024-05-01"));

        assertSoftly(softly -> {
            softly.assertThat(availableTimes.get(0).alreadyBooked()).isTrue();
            softly.assertThat(availableTimes.get(1).alreadyBooked()).isTrue();
            softly.assertThat(availableTimes.get(2).alreadyBooked()).isFalse();
            softly.assertThat(availableTimes.get(3).alreadyBooked()).isFalse();
            softly.assertThat(availableTimes.get(4).alreadyBooked()).isFalse();
        });
    }
}
