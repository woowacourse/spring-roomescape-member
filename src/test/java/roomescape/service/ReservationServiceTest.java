package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static roomescape.constant.TestData.RESERVATION_COUNT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.DataBasedTest;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.CreateReservationServiceRequest;
import roomescape.service.dto.response.ReservationServiceResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationServiceTest extends DataBasedTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @Autowired
    ReservationThemeRepository reservationThemeRepository;

    @Test
    void create() {
        // given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        ReservationTheme theme = reservationThemeRepository.save(new ReservationTheme("theme", "desc", "thumb"));
        CreateReservationServiceRequest request = new CreateReservationServiceRequest("moko",
                LocalDate.now().plusDays(1), time.id(), theme.id());

        // when
        reservationService.create(request);

        // then
        assertThat(reservationRepository.getAll()).hasSize(RESERVATION_COUNT + 1);
    }

    @Test
    void getAll() {
        // when
        List<ReservationServiceResponse> responses = reservationService.getAll();

        // then
        assertThat(reservationRepository.getAll()).hasSize(responses.size());
    }

    @Test
    void delete() {
        // when
        reservationService.delete(1L);

        // then
        assertThat(reservationRepository.getAll()).hasSize(RESERVATION_COUNT - 1);
    }

    @Test
    @DisplayName("삭제 대상이 없더라도 정상 처리된다.")
    void delete2() {
        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> reservationService.delete(1000L);

        // then
        assertThatCode(throwingCallable).doesNotThrowAnyException();
    }
}
