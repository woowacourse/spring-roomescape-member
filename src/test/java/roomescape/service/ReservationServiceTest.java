package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.ReservationRequest;
import roomescape.exception.RoomescapeException;

@SpringBootTest
@Transactional
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private Clock clock;

    @Test
    void 존재하지_않는_예약을_삭제할_경우_예외가_발생한다() {
        Assertions.assertThatThrownBy(() -> reservationService.removeById(-1L))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 존재하는_예약에_대해서_삭제할_수_있다() {
        Assertions.assertThatCode(() -> reservationService.removeById(1L))
                .doesNotThrowAnyException();
    }

    @Test
    void 이름이_빈_예약을_추가할_경우_예외가_발생한다() {
        LocalDate date = LocalDate.now(clock).plusDays(1);
        ReservationRequest reservationRequest = new ReservationRequest(null, date, 4L, 6L);

        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 날짜가_빈_예약을_추가할_경우_예외가_발생한다() {
        ReservationRequest reservationRequest = new ReservationRequest("포비", null, 4L, 6L);

        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 시간_아이디가_빈_예약을_추가할_경우_예외가_발생한다() {
        LocalDate date = LocalDate.now(clock).plusDays(1);
        ReservationRequest reservationRequest = new ReservationRequest("포비", date, null, 6L);

        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 테마_아이디가_빈_예약을_추가할_경우_예외가_발생한다() {
        LocalDate date = LocalDate.now(clock).plusDays(1);
        ReservationRequest reservationRequest = new ReservationRequest("포비", date, 4L, null);

        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 존재하는_예약을_추가할_경우_예외가_발생한다() {
        LocalDate date = LocalDate.now(clock).plusDays(3);
        ReservationRequest reservationRequest = new ReservationRequest("구바", date, 8L, 2L);

        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 존재하지_않는_예약을_정상적으로_추가할_수_있다() {
        ReservationRequest reservationRequest = new ReservationRequest("무빙", LocalDate.now(clock).plusDays(2), 2L, 2L);

        Assertions.assertThatCode(() -> reservationService.register(reservationRequest))
                .doesNotThrowAnyException();
    }

    @Test
    void 이미_지난_날짜의_예약은_불가능하다() {
        ReservationRequest reservationRequest = new ReservationRequest("무빙", LocalDate.now(clock).minusDays(1), 2L, 2L);

        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 오늘_예약이고_이미_지난_시간의_예약은_불가능하다() {
        ReservationRequest reservationRequest = new ReservationRequest("무빙", LocalDate.now(clock), 1L, 2L);

        Assertions.assertThatThrownBy(() -> reservationService.register(reservationRequest))
                .isInstanceOf(RoomescapeException.class);
    }
    
    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public Clock clock() {
            return Clock.fixed(
                    LocalDate.now().atTime(14, 0).toInstant(ZoneOffset.UTC),
                    ZoneOffset.UTC
            );
        }
    }
}
