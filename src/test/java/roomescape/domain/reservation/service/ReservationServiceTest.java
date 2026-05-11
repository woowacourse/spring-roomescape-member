package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDTO;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDTO;
import roomescape.domain.reservation.dto.response.ReservationResponseDTO;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.JdbcReservationRepository;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.mapper.ThemeMapper;
import roomescape.domain.theme.repository.JdbcThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.mapper.TimeMapper;
import roomescape.domain.time.repository.JdbcTimeRepository;
import roomescape.domain.time.repository.TimeRepository;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;

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
        reservationService = new ReservationService(reservationRepository, timeRepository, themeRepository);
    }

    @Nested
    class GetReservationTest {

        @Test
        void 성공() {
            // given
            LocalDate date = LocalDate.of(2026, 4, 30);
            Time time1 = timeRepository.save(Time.create(LocalTime.of(10, 0)));
            Time time2 = timeRepository.save(Time.create(LocalTime.of(11, 0)));
            Time time3 = timeRepository.save(Time.create(LocalTime.of(12, 0)));
            Theme theme = themeRepository.save(
                Theme.create("테마 이름", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png"));

            reservationRepository.save(Reservation.create("제이콥", date, time1, theme));
            reservationRepository.save(Reservation.create("라이", date.plusDays(1), time2, theme));
            reservationRepository.save(Reservation.create("티모", date.plusDays(2), time3, theme));

            // when
            List<ReservationResponseDTO> actual = reservationService.getReservations();

            // then
            assertThat(actual).containsExactly(
                new ReservationResponseDTO(1L, "제이콥", date, TimeMapper.toResponseDTO(time1),
                    ThemeMapper.toResponseDTO(theme)),
                new ReservationResponseDTO(2L, "라이", date.plusDays(1), TimeMapper.toResponseDTO(time2),
                    ThemeMapper.toResponseDTO(theme)),
                new ReservationResponseDTO(3L, "티모", date.plusDays(2), TimeMapper.toResponseDTO(time3),
                    ThemeMapper.toResponseDTO(theme))
            );
        }
    }

    @Nested
    class SaveReservationTest {

        @Test
        void 성공() {
            // given
            Theme theme = themeRepository.save(
                Theme.create("피온", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png"));
            Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
            ReservationCreateRequestDTO request = new ReservationCreateRequestDTO(
                "보예",
                LocalDate.of(2026, 5, 1),
                time.getId(),
                theme.getId()
            );

            // when
            ReservationCreateResponseDTO actual = reservationService.saveReservation(request);

            // then
            assertThat(actual).isEqualTo(
                new ReservationCreateResponseDTO(1L, "보예", LocalDate.of(2026, 5, 1), time.getId(), theme.getId()));
            assertThat(reservationRepository.findAllReservations()).hasSize(1);
        }

        @Test
        void 같은_날짜_시간_테마에_이미_예약이_있으면_예외가_발생한다() {
            // given
            LocalDate date = LocalDate.of(2026, 5, 1);
            Theme theme = themeRepository.save(
                Theme.create("피온", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png"));
            Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
            reservationRepository.save(Reservation.create("기존 예약자", date, time, theme));
            ReservationCreateRequestDTO request = new ReservationCreateRequestDTO("보예", date, time.getId(),
                theme.getId());

            // when & then
            assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    class DeleteReservationByIdTest {

        @Test
        void 성공() {
            // given
            Time time1 = timeRepository.save(Time.create(LocalTime.of(12, 0)));
            Time time2 = timeRepository.save(Time.create(LocalTime.of(13, 0)));
            Theme theme = themeRepository.save(
                Theme.create("테마 이름", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png"));
            Reservation savedReservation = reservationRepository.save(
                Reservation.create("제이슨", LocalDate.of(2026, 5, 2), time1, theme));
            reservationRepository.save(Reservation.create("시오", LocalDate.of(2026, 5, 3), time2, theme));

            // when
            reservationService.deleteReservationById(savedReservation.getId());

            // then
            List<ReservationResponseDTO> actual = reservationService.getReservations();
            assertThat(actual)
                .hasSize(1)
                .extracting(ReservationResponseDTO::name)
                .containsExactly("시오");
        }
    }
}
