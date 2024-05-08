package roomescape.controller;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.CollectionReservationRepository;
import roomescape.repository.CollectionReservationTimeRepository;
import roomescape.repository.CollectionThemeRepository;
import roomescape.service.AvailableTimeService;
import roomescape.service.ReservationTimeService;

class ReservationTimeControllerTest {

    private CollectionReservationTimeRepository reservationTimeRepository;
    private ReservationTimeController reservationTimeController;

    @BeforeEach
    void init() {
        reservationTimeRepository = new CollectionReservationTimeRepository();
        CollectionReservationRepository reservationRepository = new CollectionReservationRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(reservationRepository,
                reservationTimeRepository);
        CollectionThemeRepository themeRepository = new CollectionThemeRepository();
        AvailableTimeService availableTimeService = new AvailableTimeService(reservationTimeRepository,
                themeRepository);
        reservationTimeController = new ReservationTimeController(reservationTimeService, availableTimeService);
    }

    @Test
    @DisplayName("시간을 잘 저장하는지 확인한다.")
    void save() {
        LocalTime time = LocalTime.now();
        ReservationTimeResponse save = reservationTimeController.save(new ReservationTimeRequest(time)).getBody();

        ReservationTimeResponse expected = new ReservationTimeResponse(save.id(), time);
        Assertions.assertThat(save)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("시간을 잘 불러오는지 확인한다.")
    void findAll() {
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeController.findAll();

        Assertions.assertThat(reservationTimeResponses)
                .isEmpty();
    }

    @Test
    @DisplayName("시간을 잘 지우는지 확인한다.")
    void delete() {
        //given
        reservationTimeRepository.save(new ReservationTime(1L, LocalTime.now()));

        //when
        reservationTimeController.delete(1);

        //then
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeController.findAll();
        Assertions.assertThat(reservationTimeResponses)
                .isEmpty();
    }

    @Test
    @DisplayName("내부에 Repository를 의존하고 있지 않은지 확인한다.")
    void checkRepositoryDependency() {
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
