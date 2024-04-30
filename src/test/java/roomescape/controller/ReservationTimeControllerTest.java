package roomescape.controller;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.CollectionReservationTimeRepository;
import roomescape.service.ReservationTimeService;

class ReservationTimeControllerTest {
    @Test
    @DisplayName("시간을 잘 저장하는지 확인한다.")
    void save() {
        CollectionReservationTimeRepository reservationTimeRepository = new CollectionReservationTimeRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeRepository);
        ReservationTimeController reservationTimeController = new ReservationTimeController(reservationTimeService);
        LocalTime time = LocalTime.now();

        ReservationTimeResponse save = reservationTimeController.save(new ReservationTimeRequest(time));

        ReservationTimeResponse expected = new ReservationTimeResponse(save.id(), time);
        Assertions.assertThat(save)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("시간을 잘 불러오는지 확인한다.")
    void findAll() {
        CollectionReservationTimeRepository reservationTimeRepository = new CollectionReservationTimeRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeRepository);
        ReservationTimeController reservationTimeController = new ReservationTimeController(reservationTimeService);
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeController.findAll();

        Assertions.assertThat(reservationTimeResponses)
                .isEmpty();
    }

    @Test
    @DisplayName("시간을 잘 지우는지 확인한다.")
    void delete() {
        CollectionReservationTimeRepository reservationTimeRepository = new CollectionReservationTimeRepository(
                new ArrayList<>(List.of(new ReservationTime(1L, LocalTime.now())))
        );
        ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeRepository);
        ReservationTimeController reservationTimeController = new ReservationTimeController(reservationTimeService);

        reservationTimeController.delete(1);

        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeController.findAll();
        Assertions.assertThat(reservationTimeResponses)
                .isEmpty();
    }

    @Test
    @DisplayName("내부에 Repository를 의존하고 있지 않은지 확인한다.")
    void checkRepositoryDependency() {
        CollectionReservationTimeRepository reservationTimeRepository = new CollectionReservationTimeRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeRepository);
        ReservationTimeController reservationTimeController = new ReservationTimeController(reservationTimeService);

        boolean isRepositoryInjected = false;

        for (Field field : reservationTimeController.getClass().getDeclaredFields()) {
            if (field.getType().getName().contains("Repository")) {
                isRepositoryInjected = true;
                break;
            }
        }

        Assertions.assertThat(isRepositoryInjected).isFalse();
    }
}
