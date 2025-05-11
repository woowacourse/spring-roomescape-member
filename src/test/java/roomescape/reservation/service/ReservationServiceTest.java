package roomescape.reservation.service;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.global.exception.ConflictException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationRequestDto;
import roomescape.reservation.fixture.ReservationFixture;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.fixture.ReservationTimeFixture;
import roomescape.reservationTime.repository.JdbcReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;
import roomescape.user.fixture.UserFixture;
import roomescape.user.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DirtiesContext(classMode = AFTER_CLASS)
class ReservationServiceTest {

    @Autowired
    private ReservationService service;
    @Autowired
    private JdbcReservationRepository reservationRepository;
    @Autowired
    private JdbcReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private UserRepository userRepository;

    private Theme savedTheme;
    private User savedUser;

    @BeforeEach
    void beforeEach() {
        savedTheme = themeRepository.save(new Theme("name1", "dd", "tt"));
        savedUser = userRepository.save(UserFixture.create(Role.ROLE_MEMBER, "n1", "e1", "p1"));
    }

    private ReservationTime createAndSaveReservationTime(LocalTime time) {
        ReservationTime reservationTime = ReservationTimeFixture.create(time);
        return reservationTimeRepository.add(reservationTime);
    }

    private Reservation createReservation(int plusDays, ReservationTime time) {
        LocalDate date = LocalDate.now().plusDays(plusDays);
        return Reservation.of(date, time, savedTheme, savedUser);
    }

    private ReservationRequestDto createRequestDto(int plusDays, Long timeId, Long themeId) {
        LocalDate date = LocalDate.now().plusDays(plusDays);

        return ReservationFixture.createRequestDto(date, timeId, themeId);
    }

    @Nested
    @DisplayName("예약 추가하기 기능")
    class add {

        @DisplayName("이미 같은 시간에 예약이 존재한다면 예외 처리한다.")
        @Test
        void add_failure_byDuplicateDateTime() {
            // given
            ReservationTime reservationTime1 = createAndSaveReservationTime(LocalTime.of(11, 33));
            Reservation reservation1 = createReservation(1, reservationTime1);

            ReservationTime reservationTime2 = createAndSaveReservationTime(LocalTime.of(22, 44));
            Reservation reservation2 = createReservation(2, reservationTime2);

            reservationRepository.add(reservation1);
            reservationRepository.add(reservation2);

            // when & then
            LocalDate duplicateDate = reservation1.getDate();
            Long duplicateReservationTimeId = reservationTime1.getId();
            ReservationRequestDto requestDto = ReservationFixture.createRequestDto(duplicateDate,
                    duplicateReservationTimeId, savedTheme.getId());

            Assertions.assertThatThrownBy(
                    () -> service.add(requestDto, savedUser)
            ).isInstanceOf(ConflictException.class);
        }

        @DisplayName("시간이 같아도 날짜가 다르다면 예약이 가능하다.")
        @Test
        void add_success_withDifferenceDateAndSameTime() {
            // given
            ReservationTime reservationTime1 = createAndSaveReservationTime(LocalTime.of(11, 33));
            Reservation reservation1 = createReservation(1, reservationTime1);

            ReservationTime reservationTime2 = createAndSaveReservationTime(LocalTime.of(22, 44));
            Reservation reservation2 = createReservation(2, reservationTime2);

            reservationRepository.add(reservation1);
            reservationRepository.add(reservation2);

            // when & then
            Long duplicateReservationTimeId = reservationTime1.getId();
            ReservationRequestDto requestDto = createRequestDto(3, duplicateReservationTimeId,
                    savedTheme.getId());

            Assertions.assertThatCode(
                    () -> service.add(requestDto, savedUser)
            ).doesNotThrowAnyException();
        }
    }
}
