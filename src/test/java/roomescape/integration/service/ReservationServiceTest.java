package roomescape.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.common.exception.NotAbleReservationException;
import roomescape.common.exception.NotFoundMemberException;
import roomescape.common.exception.NotFoundReservationTimeException;
import roomescape.common.exception.NotFoundThemeException;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationServiceTest {
    private final ReservationService reservationService;

    // ? 이렇게 다양한 Repository 를 주입 받아서 환경을 구성하는 게 나은 선택일까, @Sql 을 사용하는 게 나은 선택일까?
    // ? Repository 가 변경되는 시점이나, @Sql 의 쿼리가 변경되는 시점이 모두 DB 테이블 구조가 변경되는 상황밖에 없을 것 같다.
    // ? 그러므로, 변경의 관점에서 둘 간의 차이가 거의 없어 보인다. 테이블 구조가 단순하면 @Sql 을 사용하는 게 좋을 수 있고,
    // ? 테이블 구조를 쿼리로 작성하는 게 어렵다면 Repository 를 사용하는 게 좋을 것 같다.
//    private final ReservationRepository reservationRepository;
//    private final MemberRepository memberRepository;
//    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationServiceTest(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Sql("classpath:fixture/sql/insert_reservation_dependencies.sql")
    @Test
    void saveReservation() {
        LocalDate givenDate = LocalDate.now().plusDays(1L);
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                givenDate,
                1L,
                1L
        );

        // when
        ReservationResponse reservationResponse = reservationService.saveReservation(request);

        // then - 반환 값 테스트
        assertAll(
                () -> Assertions.assertEquals(1L, reservationResponse.id()),
                () -> Assertions.assertEquals(1L, reservationResponse.member().getId()),
                () -> Assertions.assertEquals(givenDate, reservationResponse.date()),
                () -> Assertions.assertEquals(1L, reservationResponse.reservationTimeResponse().id()),
                () -> Assertions.assertEquals(1L, reservationResponse.themeResponse().id())
        );

        // then - 실제 DB 저장 여부 테스트
        assertThat(reservationService.readReservations()).hasSize(1);
    }

    @Sql("classpath:fixture/sql/insert_reservation_dependencies.sql")
    @Test
    void saveReservationWithInvalidDate() {
        // given
        LocalDate givenDate = LocalDate.now().minusDays(1L);
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                givenDate,
                1L,
                1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(NotAbleReservationException.class)
                .hasMessage("과거 시점의 예약을 할 수 없습니다.");
    }

    @Sql("classpath:fixture/sql/insert_reservation_dependencies.sql")
    @Test
    void saveReservationWithInvalidTime() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1L);
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                givenDate,
                1L,
                1L
        );

        reservationService.saveReservation(request);

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(NotAbleReservationException.class)
                .hasMessage("이미 해당 시간과 테마에 예약이 존재하여 예약할 수 없습니다.");
    }

    @Sql("classpath:fixture/sql/insert_reservation_dependencies.sql")
    @Test
    void saveReservationWithInvalidMember() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1L);
        CreateReservationRequest request = new CreateReservationRequest(
                2L,
                givenDate,
                1L,
                1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(NotFoundMemberException.class)
                .hasMessage("올바른 멤버가 아닙니다.");
    }

    @Sql("classpath:fixture/sql/insert_reservation_dependencies.sql")
    @Test
    void saveReservationWithInvalidTheme() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1L);
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                givenDate,
                1L,
                2L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(NotFoundThemeException.class)
                .hasMessage("올바른 방탈출 테마가 없습니다.");
    }

    @Sql("classpath:fixture/sql/insert_reservation_dependencies.sql")
    @Test
    void saveReservationWithInvalidReservationTime() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1L);
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                givenDate,
                2L,
                1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(NotFoundReservationTimeException.class)
                .hasMessage("올바른 예약 시간을 찾을 수 없습니다. 나중에 다시 시도해주세요.");
    }

    @Sql("classpath:fixture/sql/insert_reservation_dependencies.sql")
    @Test
    void readReservations() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1L);
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                givenDate,
                1L,
                1L
        );

        reservationService.saveReservation(request);

        // when
        var reservations = reservationService.readReservations();

        // then
        assertThat(reservations).hasSize(1);
    }

    @Sql("classpath:fixture/sql/insert_reservation_dependencies.sql")
    @Test
    void deleteReservation() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1L);
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                givenDate,
                1L,
                1L
        );

        reservationService.saveReservation(request);

        // when
        reservationService.deleteReservation(1L);

        // then
        assertThat(reservationService.readReservations()).isEmpty();
    }

    // 테스트 코드 보완 필요 - 일단 시간 부족으로 생략
    @Sql("classpath:fixture/sql/insert_reservation_dependencies.sql")
    @Test
    void readAllWithFilter() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1L);
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                givenDate,
                1L,
                1L
        );

        reservationService.saveReservation(request);

        // when
        var reservations = reservationService.readAllWithFilter(Map.of("memberId", 1L));

        // then
        assertThat(reservations).hasSize(1);
    }
}
