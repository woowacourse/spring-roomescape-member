package roomescape.domain.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.JdbcThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.JdbcTimeRepository;

class JdbcReservationRepositoryTest {

    private JdbcReservationRepository reservationRepository;
    private JdbcTimeRepository timeRepository;
    private JdbcThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
            "jdbc:h2:mem:" + System.nanoTime() + ";MODE=MySQL;DB_CLOSE_DELAY=-1",
            "sa",
            ""
        );

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);

        reservationRepository = new JdbcReservationRepository(dataSource);
        timeRepository = new JdbcTimeRepository(dataSource);
        themeRepository = new JdbcThemeRepository(dataSource);
    }

    @Nested
    class SaveTest {

        @Test
        void 성공() {
            // given
            Time time = timeRepository.save(Time.create(LocalTime.of(10, 0)));
            Theme theme = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));
            Reservation reservation = Reservation.create("예약자", LocalDate.of(2026, 5, 1), time, theme);

            // when
            Reservation actual = reservationRepository.save(reservation);

            // then
            assertThat(actual.getId()).isEqualTo(1L);
            assertThat(actual.getName()).isEqualTo("예약자");
            assertThat(actual.getDate()).isEqualTo(LocalDate.of(2026, 5, 1));
            assertThat(actual.getTime().getId()).isEqualTo(time.getId());
            assertThat(actual.getTheme().getId()).isEqualTo(theme.getId());
        }
    }

    @Nested
    class FindAllReservationsTest {

        @Test
        void 시간과_테마를_JOIN해서_예약을_조회한다() {
            // given
            Time time1 = timeRepository.save(Time.create(LocalTime.of(10, 0)));
            Time time2 = timeRepository.save(Time.create(LocalTime.of(11, 0)));
            Theme theme1 = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));
            Theme theme2 = themeRepository.save(Theme.create("테마2", "설명2", "image2.png"));
            Reservation reservation1 = reservationRepository.save(
                Reservation.create("예약자1", LocalDate.of(2026, 5, 1), time1, theme1));
            Reservation reservation2 = reservationRepository.save(
                Reservation.create("예약자2", LocalDate.of(2026, 5, 2), time2, theme2));

            // when
            List<Reservation> actual = reservationRepository.findAllReservations();

            // then
            assertThat(actual)
                .extracting(
                    Reservation::getId,
                    Reservation::getName,
                    Reservation::getDate,
                    reservation -> reservation.getTime().getStartAt(),
                    reservation -> reservation.getTheme().getName()
                )
                .containsExactly(
                    tuple(reservation1.getId(), "예약자1", LocalDate.of(2026, 5, 1), LocalTime.of(10, 0), "테마1"),
                    tuple(reservation2.getId(), "예약자2", LocalDate.of(2026, 5, 2), LocalTime.of(11, 0), "테마2")
                );
        }
    }

    @Nested
    class FindTimeIdsByDateAndThemeIdTest {

        @Test
        void 같은_날짜와_테마에_예약된_시간_ID만_조회한다() {
            // given
            LocalDate date = LocalDate.of(2026, 5, 1);
            Time time1 = timeRepository.save(Time.create(LocalTime.of(10, 0)));
            Time time2 = timeRepository.save(Time.create(LocalTime.of(11, 0)));
            Time time3 = timeRepository.save(Time.create(LocalTime.of(12, 0)));
            Theme theme1 = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));
            Theme theme2 = themeRepository.save(Theme.create("테마2", "설명2", "image2.png"));
            reservationRepository.save(Reservation.create("예약자1", date, time1, theme1));
            reservationRepository.save(Reservation.create("예약자2", date, time2, theme2));
            reservationRepository.save(Reservation.create("예약자3", date.plusDays(1), time3, theme1));

            // when
            List<Long> actual = reservationRepository.findTimeIdsByDateAndThemeId(date, theme1.getId());

            // then
            assertThat(actual).containsExactly(time1.getId());
        }
    }

    @Nested
    class ExistsReservationByDateAndTimeAndThemeTest {

        @Test
        void 같은_날짜_시간_테마의_예약이_있으면_true를_반환한다() {
            // given
            LocalDate date = LocalDate.of(2026, 5, 1);
            Time time = timeRepository.save(Time.create(LocalTime.of(10, 0)));
            Theme theme = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));
            reservationRepository.save(Reservation.create("예약자", date, time, theme));

            // when
            boolean actual = reservationRepository.existsReservationByDateAndTimeAndTheme(date, time, theme);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 같은_날짜_시간_테마의_예약이_없으면_false를_반환한다() {
            // given
            LocalDate date = LocalDate.of(2026, 5, 1);
            Time time = timeRepository.save(Time.create(LocalTime.of(10, 0)));
            Theme theme = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));

            // when
            boolean actual = reservationRepository.existsReservationByDateAndTimeAndTheme(date, time, theme);

            // then
            assertThat(actual).isFalse();
        }
    }

    @Nested
    class DeleteReservationByIdTest {

        @Test
        void 성공() {
            // given
            Time time = timeRepository.save(Time.create(LocalTime.of(10, 0)));
            Theme theme = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));
            Reservation reservation1 = reservationRepository.save(
                Reservation.create("예약자1", LocalDate.of(2026, 5, 1), time, theme));
            Reservation reservation2 = reservationRepository.save(
                Reservation.create("예약자2", LocalDate.of(2026, 5, 2), time, theme));

            // when
            reservationRepository.deleteReservationById(reservation1.getId());

            // then
            assertThat(reservationRepository.findAllReservations())
                .extracting(Reservation::getId)
                .containsExactly(reservation2.getId());
        }
    }
}
