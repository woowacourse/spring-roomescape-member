package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.exception.ExceptionType.DELETE_USED_TIME;
import static roomescape.exception.ExceptionType.DUPLICATE_RESERVATION_TIME;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Sha256Encryptor;
import roomescape.domain.Theme;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.repository.CollectionReservationRepository;
import roomescape.repository.CollectionReservationTimeRepository;

class ReservationTimeServiceTest {

    private CollectionReservationRepository reservationRepository;
    private CollectionReservationTimeRepository reservationTimeRepository;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void initService() {
        reservationRepository = new CollectionReservationRepository();
        reservationTimeRepository = new CollectionReservationTimeRepository();
        reservationTimeService = new ReservationTimeService(reservationRepository, reservationTimeRepository);
    }

    @DisplayName("저장된 시간을 모두 조회할 수 있다.")
    @Test
    void findAllTest() {
        //given
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(12, 0)));
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(13, 0)));

        //when
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.findAll();

        //then
        assertThat(reservationTimeResponses)
                .hasSize(4);
    }

    @DisplayName("예약 시간이 하나 존재할 때")
    @Nested
    class OneReservationTimeExists {
        private static final LocalTime SAVED_TIME = LocalTime.of(10, 0);

        @BeforeEach
        void addDefaultTime() {
            ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(SAVED_TIME);
            reservationTimeService.save(reservationTimeRequest);
        }

        @DisplayName("정상적으로 시간을 생성할 수 있다.")
        @Test
        void saveReservationTimeTest() {
            assertThatCode(() ->
                    reservationTimeService.save(new ReservationTimeRequest(SAVED_TIME.plusHours(1))))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("중복된 시간은 생성할 수 없는지 검증")
        void saveFailCauseDuplicate() {
            assertThatThrownBy(() -> reservationTimeService.save(new ReservationTimeRequest(SAVED_TIME)))
                    .isInstanceOf(RoomescapeException.class)
                    .hasMessage(DUPLICATE_RESERVATION_TIME.getMessage());
        }

        @DisplayName("저장된 시간을 삭제할 수 있다.")
        @Test
        void deleteByIdTest() {
            //when
            reservationTimeService.delete(1L);

            //then
            assertThat(reservationTimeRepository.findAll())
                    .isEmpty();
        }

        @DisplayName("예약 시간을 사용하는 예약이 있으면 예약을 삭제할 수 없다.")
        @Test
        void usedReservationTimeDeleteTest() {
            //given
            reservationRepository.save(new Reservation(
                    new Member(1L, "name", "email@email.com",
                            new Sha256Encryptor().encrypt("1234")),
                    LocalDate.now(),
                    new ReservationTime(1L, SAVED_TIME),
                    new Theme(1L, "name", "description", "http://thumbnail")
            ));

            //when & then
            assertThatCode(() -> reservationTimeService.delete(1L))
                    .isInstanceOf(RoomescapeException.class)
                    .hasMessage(DELETE_USED_TIME.getMessage());
        }
    }
}
