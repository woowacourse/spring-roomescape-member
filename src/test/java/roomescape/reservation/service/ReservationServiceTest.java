package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.common.globalexception.ConflictException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationReqDto;
import roomescape.reservation.fixture.ReservationFixture;
import roomescape.reservation.repository.ReservationRepositoryImpl;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.fixture.ReservationTimeFixture;
import roomescape.reservationtime.repository.ReservationTimeRepositoryImpl;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService service;
    @Autowired
    private ReservationRepositoryImpl reservationRepository;
    @Autowired
    private ReservationTimeRepositoryImpl reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;

    private ReservationTime createAndSaveReservationTime(LocalTime time) {
        ReservationTime reservationTime = ReservationTimeFixture.create(time);
        return reservationTimeRepository.add(reservationTime);
    }

    private Reservation createReservation(String name, int plusDays, ReservationTime time, Theme theme) {
        LocalDate date = LocalDate.now().plusDays(plusDays);
        return Reservation.withoutId(name, date, time, theme);
    }

    private ReservationReqDto createReqDto(String name, int plusDays, Long timeId, Long themeId) {
        LocalDate date = LocalDate.now().plusDays(plusDays);

        return ReservationFixture.createReqDto(name, date, timeId, themeId);
    }

    @Nested
    @DisplayName("예약 추가하기 기능")
    class add {

        @DisplayName("이미 같은 시간에 예약이 존재한다면 예외 처리한다.")
        @Test
        void add_failure_byDuplicateDateTime() {
            // given
            ReservationTime reservationTime1 = createAndSaveReservationTime(LocalTime.of(11, 33));

            Theme savedTheme = themeRepository.add(new Theme("name1", "dd", "tt"));

            Reservation reservation1 = createReservation("kali", 1, reservationTime1, savedTheme);

            ReservationTime reservationTime2 = createAndSaveReservationTime(LocalTime.of(22, 44));
            Reservation reservation2 = createReservation("pobi", 2, reservationTime2, savedTheme);

            reservationRepository.add(reservation1);
            reservationRepository.add(reservation2);

            // when & then
            LocalDate duplicateDate = reservation1.getDate();
            Long duplicateReservationTimeId = reservationTime1.getId();
            ReservationReqDto reqDto = ReservationFixture.createReqDto("jason", duplicateDate,
                duplicateReservationTimeId, savedTheme.getId());

            Assertions.assertThatThrownBy(
                () -> service.add(reqDto)
            ).isInstanceOf(ConflictException.class);
        }

        @DisplayName("시간이 같아도 날짜가 다르다면 예약이 가능하다.")
        @Test
        void add_success_withDifferenceDateAndSameTime() {
            // given
            ReservationTime reservationTime1 = createAndSaveReservationTime(LocalTime.of(11, 33));

            Theme savedTheme = themeRepository.add(new Theme("name1", "dd", "tt"));

            Reservation reservation1 = createReservation("kali", 1, reservationTime1, savedTheme);

            ReservationTime reservationTime2 = createAndSaveReservationTime(LocalTime.of(22, 44));
            Reservation reservation2 = createReservation("pobi", 2, reservationTime2, savedTheme);

            reservationRepository.add(reservation1);
            reservationRepository.add(reservation2);

            // when & then
            Long duplicateReservationTimeId = reservationTime1.getId();
            ReservationReqDto reqDto = createReqDto("jason", 3, duplicateReservationTimeId, savedTheme.getId());

            Assertions.assertThatCode(
                () -> service.add(reqDto)
            ).doesNotThrowAnyException();
        }
    }
}
