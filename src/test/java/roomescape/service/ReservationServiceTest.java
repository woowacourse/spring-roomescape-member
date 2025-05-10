package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import roomescape.common.exception.*;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationServiceTest {
    private final ReservationService reservationService;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public ReservationServiceTest(ReservationService reservationService,
                                   ReservationTimeRepository reservationTimeRepository,
                                   ThemeRepository themeRepository) {
        this.reservationService = reservationService;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @BeforeEach
    void beforeEach() {
        reservationTimeRepository.save(
                new ReservationTime(1L, LocalTime.of(10, 0))
        );
        themeRepository.save(
                new Theme(1L, "Test Theme", "Test Description", "image.jpg")
        );
    }

    @Test
    void createReservationTest() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1);
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(givenDate, 1L, 1L, 1L);

        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(1L, "히스타");

        // when
        ReservationResponse reservationResponse = reservationService.createReservation(reservationCreateRequest, loginMemberRequest);

        // then
        assertAll(
                () -> assertThat(reservationResponse.loginMemberResponse()).isEqualTo("히스타"),
                () -> assertThat(reservationResponse.reservationTimeResponse().id()).isEqualTo(1L),
                () -> assertThat(reservationResponse.date()).isEqualTo(givenDate),
                () -> assertThat(reservationResponse.reservationTimeResponse().startAt()).isEqualTo("10:00"),
                () -> assertThat(reservationResponse.themeResponse().id()).isEqualTo(1L),
                () -> assertThat(reservationResponse.themeResponse().name()).isEqualTo("Test Theme"),
                () -> assertThat(reservationResponse.themeResponse().description()).isEqualTo("Test Description"),
                () -> assertThat(reservationResponse.themeResponse().thumbnail()).isEqualTo("image.jpg")
        );
    }

    @ParameterizedTest
    @MethodSource("getInvalidReservationCreateRequest")
    void createReservationFailureTest(ReservationCreateRequest reservationCreateRequest, LoginMemberRequest loginMemberRequest,Class<Exception> exceptionClass) {
        assertThatThrownBy(() -> reservationService.createReservation(reservationCreateRequest, loginMemberRequest)).isInstanceOf(exceptionClass);
    }

    static Stream<Arguments> getInvalidReservationCreateRequest() {
        LocalDate givenDate = LocalDate.now().plusDays(1);
        return Stream.of(
                Arguments.arguments(new ReservationCreateRequest(LocalDate.now().minusDays(1), 1L, 1L, 1L), new LoginMemberRequest(1L, "히스타"), NotAbleReservationException.class),
                Arguments.arguments(new ReservationCreateRequest(givenDate, 2L, 1L, 1L), new LoginMemberRequest(1L, "히스타"), NotFoundReservationTimeException.class),
                Arguments.arguments(new ReservationCreateRequest(givenDate, 1L, 2L, 1L), new LoginMemberRequest(1L, "히스타"), NotFoundThemeException.class)
        );
    }

    @Test
    void findAllTest() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1);
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(givenDate, 1L, 1L, 1L);
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(1L, "히스타");
        reservationService.createReservation(reservationCreateRequest, loginMemberRequest);

        // when
        List<ReservationResponse> reservationResponses = reservationService.findAll();

        // then
        assertThat(reservationResponses).hasSize(1);
        assertAll(
                () -> assertThat(reservationResponses.getFirst().loginMemberResponse().id()).isEqualTo(1L),
                () -> assertThat(reservationResponses.getFirst().loginMemberResponse().name()).isEqualTo("히스타"),
                () -> assertThat(reservationResponses.getFirst().date()).isEqualTo(givenDate),
                () -> assertThat(reservationResponses.getFirst().reservationTimeResponse().id()).isEqualTo(1L),
                () -> assertThat(reservationResponses.getFirst().themeResponse().id()).isEqualTo(1L),
                () -> assertThat(reservationResponses.getFirst().themeResponse().name()).isEqualTo("Test Theme"),
                () -> assertThat(reservationResponses.getFirst().themeResponse().description()).isEqualTo("Test Description"),
                () -> assertThat(reservationResponses.getFirst().themeResponse().thumbnail()).isEqualTo("image.jpg"),
                () -> assertThat(reservationResponses.getFirst().reservationTimeResponse().startAt()).isEqualTo("10:00")
        );
    }

    @Test
    void deleteReservationByIdReservationTest() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1);
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(givenDate, 1L, 1L, 1L);
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(1L, "히스타");
        reservationService.createReservation(reservationCreateRequest, loginMemberRequest);

        // when
        List<ReservationResponse> reservationResponsesBeforeDelete = reservationService.findAll();
        reservationService.deleteReservationById(reservationResponsesBeforeDelete.get(0).id());
        List<ReservationResponse> reservationResponsesAfterDelete = reservationService.findAll();

        // then
        assertAll(
                () -> assertThat(reservationResponsesBeforeDelete).hasSize(1),
                () -> assertThat(reservationResponsesAfterDelete).isEmpty()
        );
    }

    @Test
    void createReservationWithPastDateTest() {
        // given
        LocalDate givenDate = LocalDate.now().minusDays(1);
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(givenDate, 1L, 1L, 1L);
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(1L, "히스타");

        // when
        assertThrows(NotAbleReservationException.class, () -> reservationService.createReservation(reservationCreateRequest, loginMemberRequest));
    }

    @Test
    void createReservationWithAlreadyBookedTimeTest() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1);
        ReservationCreateRequest request1 = new ReservationCreateRequest(givenDate, 1L, 1L, 1L);
        LoginMemberRequest loginMemberRequest1 = new LoginMemberRequest(1L, "히스타");
        reservationService.createReservation(request1, loginMemberRequest1);

        ReservationCreateRequest request2 = new ReservationCreateRequest(givenDate, 1L, 1L, 2L);
        LoginMemberRequest loginMemberRequest2 = new LoginMemberRequest(2L, "히스타2");

        // when
        assertThrows(NotAbleReservationException.class, () -> reservationService.createReservation(request2, loginMemberRequest2));
    }

    @Test
    void createReservationWithInvalidThemeTest() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1);
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(givenDate, 1L, 2L, 1L);
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(1L, "히스타");

        // when
        assertThrows(NotFoundThemeException.class, () -> reservationService.createReservation(reservationCreateRequest, loginMemberRequest));
    }

    @Test
    void createReservationWithInvalidTimeTest() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1);
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(givenDate, 1L, 2L, 1L);
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(1L, "히스타");

        // when
        assertThrows(NotFoundReservationTimeException.class, () -> reservationService.createReservation(reservationCreateRequest, loginMemberRequest));
    }

    @Test
    void deleteReservationByIdReservationWithInvalidIdTest() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1);
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(givenDate, 1L, 2L, 1L);
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(1L, "히스타");

        // when
        assertThrows(NotAbleDeleteException.class, () -> reservationService.deleteReservationById(2L));
    }
}
