package roomescape.controller;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.CollectionReservationRepository;
import roomescape.repository.CollectionReservationTimeRepository;
import roomescape.service.ReservationService;

class ReservationControllerTest {
    static final long timeId = 1L;
    static final LocalTime time = LocalTime.now();
    private final CollectionReservationTimeRepository timeRepository = new CollectionReservationTimeRepository(
            new ArrayList<>(List.of(new ReservationTime(timeId, time)))
    );

    @Test
    @DisplayName("예약 정보를 잘 저장하는지 확인한다.")
    void saveReservation() {
        CollectionReservationRepository collectionReservationRepository = new CollectionReservationRepository(
                timeRepository);
        ReservationService reservationService = new ReservationService(collectionReservationRepository);
        ReservationController reservationController = new ReservationController(reservationService);
        LocalDate date = LocalDate.now();

        ReservationResponse saveResponse = reservationController.saveReservation(
                new ReservationRequest(date, "폴라", timeId));

        long id = Objects.requireNonNull(saveResponse).id();
        ReservationResponse expected = new ReservationResponse(id, "폴라", date,
                new ReservationTimeResponse(timeId, time));

        Assertions.assertThat(saveResponse)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("예약 정보를 잘 불러오는지 확인한다.")
    void findAllReservations() {
        CollectionReservationRepository collectionReservationRepository = new CollectionReservationRepository(
                timeRepository);
        ReservationService reservationService = new ReservationService(collectionReservationRepository);
        ReservationController reservationController = new ReservationController(reservationService);
        List<ReservationResponse> allReservations = reservationController.findAllReservations();

        Assertions.assertThat(allReservations)
                .isEmpty();
    }

    @Test
    @DisplayName("예약 정보를 잘 지우는지 확인한다.")
    void delete() {
        List<Reservation> reservations = List.of(
                new Reservation(1L, "폴라", LocalDate.now(), new ReservationTime(LocalTime.now())));
        CollectionReservationRepository collectionReservationRepository = new CollectionReservationRepository(
                new ArrayList<>(reservations), timeRepository);
        ReservationService reservationService = new ReservationService(collectionReservationRepository);
        ReservationController reservationController = new ReservationController(reservationService);

        reservationController.delete(1L);
        List<ReservationResponse> reservationResponses = reservationController.findAllReservations();

        Assertions.assertThat(reservationResponses)
                .isEmpty();
    }

    @Test
    @DisplayName("내부에 Repository를 의존하고 있지 않은지 확인한다.")
    void checkRepositoryDependency() {
        CollectionReservationRepository collectionReservationRepository = new CollectionReservationRepository(
                timeRepository);
        ReservationService reservationService = new ReservationService(collectionReservationRepository);
        ReservationController reservationController = new ReservationController(reservationService);

        boolean isRepositoryInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().getName().contains("Repository")) {
                isRepositoryInjected = true;
                break;
            }
        }

        Assertions.assertThat(isRepositoryInjected).isFalse();
    }
}
