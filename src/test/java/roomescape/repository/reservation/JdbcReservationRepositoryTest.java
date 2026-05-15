package roomescape.repository.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationLocalDate;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.repository.theme.JdbcThemeRepository;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.time.JdbcReservationTimeRepository;
import roomescape.repository.time.ReservationTimeRepository;

@Import({JdbcReservationRepository.class, JdbcReservationTimeRepository.class, JdbcThemeRepository.class})
@JdbcTest
class JdbcReservationRepositoryTest {

    private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);
    private static final ReservationTime RESERVATION_TIME = new ReservationTime(LocalTime.of(12, 0));
    private static final Theme THEME = new Theme("테마명", "설명", ThemeImageUrl.defaultImageUrl().value());

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public JdbcReservationRepositoryTest(
        ReservationRepository repository,
        ReservationTimeRepository timeRepository,
        ThemeRepository themeRepository
    ) {
        this.reservationRepository = repository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Test
    void 예약을_저장한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme savedTheme = themeRepository.createTheme(THEME);

        // when
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation saved = reservationRepository.createReservation(
            new Reservation("name", tomorrow, savedTime, savedTheme));

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTime()).isEqualTo(savedTime);
        assertThat(saved.getTheme()).isEqualTo(savedTheme);
    }

    @Test
    void 동일한_날짜_시간_테마를_가지는_예약을_추가하면_예외가_발생한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme savedTheme = themeRepository.createTheme(THEME);

        LocalDate date = LocalDate.now().plusDays(1);
        reservationRepository.createReservation(new Reservation("n", date, savedTime, savedTheme));

        Reservation duplicated = new Reservation("n", date, savedTime, savedTheme);

        // when & then
        assertThatThrownBy(() -> reservationRepository.createReservation(duplicated))
            .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void 전체_예약_목록을_조회한다() {
        // given
        ReservationTime savedTime1 = timeRepository.createReservationTime(new ReservationTime("13:00"));
        ReservationTime savedTime2 = timeRepository.createReservationTime(new ReservationTime("10:00"));

        Theme savedTheme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        reservationRepository.createReservation(new Reservation("브라운", tomorrow, savedTime1, savedTheme));
        reservationRepository.createReservation(new Reservation("제임스", tomorrow, savedTime2, savedTheme));

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations)
            .extracting(Reservation::getTime)
            .containsExactlyInAnyOrder(savedTime1, savedTime2);

        assertThat(reservations)
            .extracting(Reservation::getTheme)
            .containsOnly(savedTheme);
    }

    @Test
    void 예약자_이름으로_예약_목록을_조회한다() {
        // given
        ReservationTime savedTime1 = timeRepository.createReservationTime(new ReservationTime("13:00"));
        ReservationTime savedTime2 = timeRepository.createReservationTime(new ReservationTime("10:00"));

        Theme savedTheme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        MemberName name = new MemberName("코로구");
        reservationRepository.createReservation(new Reservation(name.value(), tomorrow, savedTime1, savedTheme));
        reservationRepository.createReservation(new Reservation(name.value(), tomorrow, savedTime2, savedTheme));

        // when
        List<Reservation> reservations = reservationRepository.findAllByMemberName(name);

        // then
        assertThat(reservations)
            .extracting(Reservation::getTime)
            .containsExactlyInAnyOrder(savedTime1, savedTime2);

        assertThat(reservations)
            .extracting(Reservation::getTheme)
            .containsOnly(savedTheme);
    }

    @Test
    void 예약자_이름으로_된_예약이_없다면_빈_목록을_반환한다() {
        // given
        ReservationTime savedTime1 = timeRepository.createReservationTime(new ReservationTime("13:00"));
        ReservationTime savedTime2 = timeRepository.createReservationTime(new ReservationTime("10:00"));

        Theme savedTheme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String targetName = "코로구";
        reservationRepository.createReservation(new Reservation(targetName, tomorrow, savedTime1, savedTheme));
        reservationRepository.createReservation(new Reservation(targetName, tomorrow, savedTime2, savedTheme));

        // when
        List<Reservation> reservations = reservationRepository.findAllByMemberName(new MemberName("다른 이름"));

        // then
        assertThat(reservations).isEmpty();
    }

    @Test
    void 예약을_아이디로_삭제한다() {
        // given
        ReservationTime time = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme theme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation saved = reservationRepository.createReservation(new Reservation("브라운", tomorrow, time, theme));

        // when
        reservationRepository.deleteById(saved.getId());

        // then
        assertThat(reservationRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void 저장된_예약이_있다면_해당_ID의_예약이_존재한다고_조회한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme savedTheme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation saved = reservationRepository.createReservation(
            new Reservation("브라운", tomorrow, savedTime, savedTheme));

        // when
        boolean exists = reservationRepository.existsById(saved.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 저장된_예약이_없다면_해당_ID의_예약이_존재하지_않는다고_조회한다() {
        // given & when
        boolean exists = reservationRepository.existsById(1L);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void 저장되어_있는_예약을_아이디로_조회한다() {
        // given
        ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme savedTheme = themeRepository.createTheme(THEME);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation saved = reservationRepository.createReservation(
            new Reservation("브라운", tomorrow, savedTime, savedTheme));

        // when
        Optional<Reservation> target = reservationRepository.findById(saved.getId());

        // then
        Reservation found = target.get();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getName()).isEqualTo(saved.getName());
        assertThat(found.getDateValue()).isEqualTo(saved.getDateValue());
        assertThat(found.getTime()).isEqualTo(saved.getTime());
        assertThat(found.getTheme()).isEqualTo(saved.getTheme());
    }

    @Test
    void 기존_예약의_날짜와_시간을_수정한다() {
        // given
        ReservationTime time = timeRepository.createReservationTime(RESERVATION_TIME);
        Theme theme = themeRepository.createTheme(THEME);

        LocalTime newTimeValue = time.getStartAt().plusHours(1);
        ReservationTime newTime = timeRepository.createReservationTime(new ReservationTime(newTimeValue));

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation previous = reservationRepository.createReservation(new Reservation("name", tomorrow, time, theme));

        LocalDate newDate = tomorrow.plusDays(1);
        Reservation updated = previous.updateDateTime(newDate, newTime);

        // when
        int version = reservationRepository.findVersionById(previous.getId());
        int affected = reservationRepository.updateById(updated, version);

        // then
        assertThat(affected).isEqualTo(1);

        Reservation actual = reservationRepository.findById(previous.getId()).get();
        assertThat(actual.getName()).isEqualTo(previous.getName());

        assertThat(actual.getTheme().getNameValue()).isEqualTo(previous.getTheme().getNameValue());
        assertThat(actual.getTheme().getDescription()).isEqualTo(previous.getTheme().getDescription());
        assertThat(actual.getTheme().getImageUrlValue()).isEqualTo(previous.getTheme().getImageUrlValue());

        assertThat(actual.getDateValue()).isEqualTo(newDate);
        assertThat(actual.getTimeId()).isEqualTo(newTime.getId());
        assertThat(actual.getTime()).isEqualTo(newTime);
    }

    @Nested
    @DisplayName("특정 테마를 사용하는 예약이 있는 지 조회한다")
    class ExistsReservationByThemeId {

        @Test
        void 해당_테마를_사용하고_있는_테마가_있는_경우_TRUE를_반환한다() {
            // given
            Theme savedTheme = themeRepository.createTheme(
                new Theme("n", "d", ThemeImageUrl.defaultImageUrl().value()));
            ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);

            reservationRepository.createReservation(new Reservation("n", TOMORROW, savedTime, savedTheme));

            // when
            boolean exists = reservationRepository.existsByThemeId(savedTheme.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        void 해당_테마를_사용하고_있는_테마가_없는_경우_FALSE를_반환한다() {
            // given
            Theme savedTheme = themeRepository.createTheme(
                new Theme("n", "d", ThemeImageUrl.defaultImageUrl().value()));
            Theme otherTheme = themeRepository.createTheme(
                new Theme("n", "d", ThemeImageUrl.defaultImageUrl().value()));
            ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);

            reservationRepository.createReservation(new Reservation("n", TOMORROW, savedTime, savedTheme));

            // when
            boolean exists = reservationRepository.existsByThemeId(otherTheme.getId());

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("오늘 이후의 예약 중 특정 시간을 사용하는 예약이 있는 지 조회한다")
    class ExistsByTimeIdAndDateOnOrAfter {

        @Test
        void 오늘_이후의_예약중_같은_시간을_사용하는_예약이_있는지_조회한다() {
            // given
            ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
            Theme savedTheme = themeRepository.createTheme(THEME);

            reservationRepository.createReservation(new Reservation("n", TOMORROW, savedTime, savedTheme));

            // when
            boolean exists = reservationRepository.existsByTimeIdAndDateOnOrAfter(savedTime.getId(), LocalDate.now());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        void 같은_시간이라도_오늘_이후의_예약이_아니면_FALSE를_반환한다() {
            // given
            ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
            Theme savedTheme = themeRepository.createTheme(THEME);

            LocalDate yesterday = LocalDate.now().minusDays(1);
            reservationRepository.createReservation(new Reservation(
                null, new MemberName("n"), new ReservationLocalDate(yesterday), savedTime, savedTheme));

            // when
            boolean exists = reservationRepository.existsByTimeIdAndDateOnOrAfter(savedTime.getId(), LocalDate.now());

            // then
            assertThat(exists).isFalse();
        }

        @Test
        void 오늘_이후의_예약이라도_다른_시간이면_FALSE를_반환한다() {
            // given
            ReservationTime savedTime = timeRepository.createReservationTime(new ReservationTime("12:00"));
            ReservationTime otherTime = timeRepository.createReservationTime(new ReservationTime("13:00"));

            Theme savedTheme = themeRepository.createTheme(THEME);

            reservationRepository.createReservation(new Reservation("n", TOMORROW, savedTime, savedTheme));

            // when
            boolean exists = reservationRepository.existsByTimeIdAndDateOnOrAfter(otherTime.getId(), LocalDate.now());

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("동일한 날짜, 시간, 테마를 가지는 예약이 있는 지 조회한다")
    class ExistsByDateAndTimeIdAndThemeId {

        @Test
        void 동일한_날짜_시간_테마를_가지는_예약이_있다면_TRUE를_반환한다() {
            // given
            ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
            Theme savedTheme = themeRepository.createTheme(THEME);

            LocalDate date = LocalDate.now().plusDays(1);
            reservationRepository.createReservation(new Reservation("n", date, savedTime, savedTheme));

            // when
            boolean exists = reservationRepository
                .existsByDateAndTimeIdAndThemeId(date, savedTime.getId(), savedTheme.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        void 동일한_날짜_시간_이어도_같은_테마를_가지는_예약이_없다면_FALSE를_반환한다() {
            // given
            ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
            Theme savedTheme = themeRepository.createTheme(THEME);

            LocalDate date = LocalDate.now().plusDays(1);
            reservationRepository.createReservation(new Reservation("n", date, savedTime, savedTheme));

            long otherThemeId = savedTheme.getId() + 1;

            // when
            boolean exists = reservationRepository
                .existsByDateAndTimeIdAndThemeId(date, savedTime.getId(), otherThemeId);

            // then
            assertThat(exists).isFalse();
        }

        @Test
        void 동일한_날짜_테마_이어도_같은_시간을_가지는_예약이_없다면_FALSE를_반환한다() {
            // given
            ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
            Theme savedTheme = themeRepository.createTheme(THEME);

            LocalDate date = LocalDate.now().plusDays(1);
            reservationRepository.createReservation(new Reservation("n", date, savedTime, savedTheme));

            long otherTimeId = savedTime.getId() + 1;

            // when
            boolean exists = reservationRepository
                .existsByDateAndTimeIdAndThemeId(date, otherTimeId, savedTheme.getId());

            // then
            assertThat(exists).isFalse();
        }

        @Test
        void 동일한_시간_테마_이어도_같은_날짜를_가지는_예약이_없다면_FALSE를_반환한다() {
            // given
            ReservationTime savedTime = timeRepository.createReservationTime(RESERVATION_TIME);
            Theme savedTheme = themeRepository.createTheme(THEME);

            LocalDate date = LocalDate.now().plusDays(1);
            reservationRepository.createReservation(new Reservation("n", date, savedTime, savedTheme));

            LocalDate otherDate = date.plusDays(1);

            // when
            boolean exists = reservationRepository
                .existsByDateAndTimeIdAndThemeId(otherDate, savedTime.getId(), savedTheme.getId());

            // then
            assertThat(exists).isFalse();
        }
    }
}
