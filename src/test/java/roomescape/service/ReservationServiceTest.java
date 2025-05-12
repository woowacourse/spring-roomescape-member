package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.CreateReservationRequest;
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
    void saveReservationTest() {
        // given
        LocalDate givenDate = LocalDate.now().plusDays(1);
        CreateReservationRequest request = new CreateReservationRequest(1L, givenDate, 1L, 1L);

        // when
        ReservationResponse reservationResponse = reservationService.saveReservation(request);

        // then
        Assertions.assertAll(
                () -> assertThat(reservationResponse.member().getName()).isEqualTo("히스타"),
                () -> assertThat(reservationResponse.reservationTimeResponse().id()).isEqualTo(1L),
                () -> assertThat(reservationResponse.date()).isEqualTo(givenDate),
                () -> assertThat(reservationResponse.reservationTimeResponse().startAt()).isEqualTo("10:00"),
                () -> assertThat(reservationResponse.themeResponse().id()).isEqualTo(1L),
                () -> assertThat(reservationResponse.themeResponse().name()).isEqualTo("Test Theme"),
                () -> assertThat(reservationResponse.themeResponse().description()).isEqualTo("Test Description"),
                () -> assertThat(reservationResponse.themeResponse().thumbnail()).isEqualTo("image.jpg")
        );
    }
//
//    @Test
//    void readReservationsTest() {
//        // given
//        LocalDate givenDate = LocalDate.now().plusDays(1);
//        CreateReservationRequest request = new CreateReservationRequest("히스타", givenDate, 1L, 1L);
//        reservationService.saveReservation(request);
//
//        // when
//        List<ReservationResponse> reservationResponses = reservationService.readReservations();
//
//        // then
//        assertThat(reservationResponses).hasSize(1);
//        Assertions.assertAll(
//                () -> assertThat(reservationResponses.getFirst().name()).isEqualTo("히스타"),
//                () -> assertThat(reservationResponses.getFirst().date()).isEqualTo(givenDate),
//                () -> assertThat(reservationResponses.getFirst().reservationTimeResponse().id()).isEqualTo(1L),
//                () -> assertThat(reservationResponses.getFirst().themeResponse().id()).isEqualTo(1L),
//                () -> assertThat(reservationResponses.getFirst().themeResponse().name()).isEqualTo("Test Theme"),
//                () -> assertThat(reservationResponses.getFirst().themeResponse().description()).isEqualTo("Test Description"),
//                () -> assertThat(reservationResponses.getFirst().themeResponse().thumbnail()).isEqualTo("image.jpg"),
//                () -> assertThat(reservationResponses.getFirst().reservationTimeResponse().startAt()).isEqualTo("10:00")
//        );
//    }
//
//    @Test
//    void deleteReservationReservationTest() {
//        // given
//        LocalDate givenDate = LocalDate.now().plusDays(1);
//        CreateReservationRequest request = new CreateReservationRequest("히스타", givenDate, 1L, 1L);
//        reservationService.saveReservation(request);
//
//        // when
//        List<ReservationResponse> reservationResponsesBeforeDelete = reservationService.readReservations();
//        reservationService.deleteReservation(reservationResponsesBeforeDelete.get(0).id());
//        List<ReservationResponse> reservationResponsesAfterDelete = reservationService.readReservations();
//
//        // then
//        Assertions.assertAll(
//                () -> assertThat(reservationResponsesBeforeDelete).hasSize(1),
//                () -> assertThat(reservationResponsesAfterDelete).isEmpty()
//        );
//    }
//
//    @Test
//    void saveReservationWithPastDateTest() {
//        // given
//        LocalDate givenDate = LocalDate.now().minusDays(1);
//        CreateReservationRequest request = new CreateReservationRequest("히스타", givenDate, 1L, 1L);
//
//        // when
//        Assertions.assertThrows(NotAbleReservationException.class, () -> reservationService.saveReservation(request));
//    }
//
//    @Test
//    void saveReservationWithAlreadyBookedTimeTest() {
//        // given
//        LocalDate givenDate = LocalDate.now().plusDays(1);
//        CreateReservationRequest request1 = new CreateReservationRequest("히스타", givenDate, 1L, 1L);
//        CreateReservationRequest request2 = new CreateReservationRequest("히스타2", givenDate, 1L, 1L);
//        reservationService.saveReservation(request1);
//
//        // when
//        Assertions.assertThrows(NotAbleReservationException.class, () -> reservationService.saveReservation(request2));
//    }
//
//    @Test
//    void saveReservationWithInvalidThemeTest() {
//        // given
//        LocalDate givenDate = LocalDate.now().plusDays(1);
//        CreateReservationRequest request = new CreateReservationRequest("히스타", givenDate, 1L, 2L);
//
//        // when
//        Assertions.assertThrows(NotFoundThemeException.class, () -> reservationService.saveReservation(request));
//    }
//
//    @Test
//    void saveReservationWithInvalidTimeTest() {
//        // given
//        LocalDate givenDate = LocalDate.now().plusDays(1);
//        CreateReservationRequest request = new CreateReservationRequest("히스타", givenDate, 2L, 1L);
//
//        // when
//        Assertions.assertThrows(NotFoundReservationTimeException.class, () -> reservationService.saveReservation(request));
//    }
//
//    @Test
//    void deleteReservationReservationWithInvalidIdTest() {
//        // given
//        LocalDate givenDate = LocalDate.now().plusDays(1);
//        CreateReservationRequest request = new CreateReservationRequest("히스타", givenDate, 1L, 1L);
//        reservationService.saveReservation(request);
//
//        // when
//        Assertions.assertThrows(NotAbleDeleteException.class, () -> reservationService.deleteReservation(2L));
//    }
}
