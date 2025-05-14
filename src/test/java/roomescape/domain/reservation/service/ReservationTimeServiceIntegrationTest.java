package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.domain.auth.repository.impl.UserDao;
import roomescape.domain.reservation.dto.reservationtime.ReservationTimeRequest;
import roomescape.domain.reservation.dto.reservationtime.ReservationTimeResponse;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;
import roomescape.domain.reservation.repository.impl.ReservationDAO;
import roomescape.domain.reservation.repository.impl.ReservationTimeDAO;
import roomescape.domain.reservation.repository.impl.ThemeDAO;
import roomescape.utils.PasswordFixture;

@JdbcTest
@Import({ReservationDAO.class, ReservationTimeDAO.class, ThemeDAO.class, UserDao.class})
class ReservationTimeServiceIntegrationTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @DisplayName("모든 시간 정보를 가져온다.")
    @Test
    void test1() {
        // given
        final LocalTime localTime1 = LocalTime.of(8, 0);
        final LocalTime localTime2 = LocalTime.of(9, 0);
        final List<LocalTime> localTimes = List.of(localTime1, localTime2);

        for (final LocalTime localTime : localTimes) {
            reservationTimeRepository.save(ReservationTime.withoutId(localTime));
        }

        // when
        final List<ReservationTimeResponse> result = reservationTimeService.getAll();

        // then
        final List<LocalTime> resultTimes = result.stream()
                .map(ReservationTimeResponse::startAt)
                .toList();
        assertThat(resultTimes).containsExactlyInAnyOrderElementsOf(localTimes);
    }

    @DisplayName("정보가 없다면 빈 리스트를 반환한다.")
    @Test
    void test2() {
        // given & when
        final List<ReservationTimeResponse> result = reservationTimeService.getAll();

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("예약 시간을 저장한다.")
    @Test
    void test3() {
        // given
        final LocalTime localTime1 = LocalTime.of(8, 0);
        final ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(localTime1);

        // when
        final ReservationTimeResponse result = reservationTimeService.create(reservationTimeRequest);

        // then
        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(result.id())
                .isNotNull();
        softly.assertThat(result.startAt())
                .isEqualTo(localTime1);

        softly.assertAll();
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void test4() {
        // given
        final ReservationTime saved = reservationTimeRepository.save(ReservationTime.withoutId(LocalTime.of(8, 0)));
        final Long id = saved.getId();

        // when & then
        assertThatCode(() -> reservationTimeService.delete(id)).doesNotThrowAnyException();
    }

    @DisplayName("예약 시간이 존재하지 않으면 예외를 반환한다.")
    @Test
    void test5() {
        // given
        final Long id = 1L;

        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(id)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("예약이 존재할 때 예약을 삭제하면 예외를 반환한다.")
    @Test
    void test6() {
        // given
        final Theme theme = themeRepository.save(Theme.withoutId("테마1", "테마1", "www.m.com"));

        final ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.of(8, 0)));

        final Name name = new Name("꾹");
        final User user = userRepository.save(
                User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER));

        reservationRepository.save(Reservation.withoutId(user, LocalDate.now(), reservationTime, theme));
        final Long timeId = reservationTime.getId();

        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(timeId)).isInstanceOf(AlreadyInUseException.class);
    }
}
