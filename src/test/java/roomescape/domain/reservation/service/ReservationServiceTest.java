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
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDto;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDto;
import roomescape.domain.reservation.dto.response.ReservationResponseDto;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.error.exception.ReservationException;
import roomescape.domain.reservation.error.exception.ReservationNotFoundException;
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
import roomescape.global.error.exception.dto.FieldErrorResponseDto;

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
            List<ReservationResponseDto> actual = reservationService.getReservations();

            // then
            assertThat(actual).containsExactly(
                new ReservationResponseDto(1L, "제이콥", date, TimeMapper.toResponseDto(time1),
                    ThemeMapper.toResponseDto(theme)),
                new ReservationResponseDto(2L, "라이", date.plusDays(1), TimeMapper.toResponseDto(time2),
                    ThemeMapper.toResponseDto(theme)),
                new ReservationResponseDto(3L, "티모", date.plusDays(2), TimeMapper.toResponseDto(time3),
                    ThemeMapper.toResponseDto(theme))
            );
        }
    }

    @Nested
    class SaveReservationTest {

        @Nested
        class Success {

            @Test
            void 성공() {
                // given
                Theme theme = themeRepository.save(
                    Theme.create("피온", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png"));
                Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
                ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                    "보예",
                    LocalDate.of(2026, 5, 1),
                    time.getId(),
                    theme.getId()
                );

                // when
                ReservationCreateResponseDto actual = reservationService.saveReservation(request);

                // then
                assertThat(actual).isEqualTo(
                    new ReservationCreateResponseDto(1L, "보예", LocalDate.of(2026, 5, 1), time.getId(), theme.getId()));
                assertThat(reservationRepository.findAllReservations()).hasSize(1);
            }

            @Test
            void 같은_날짜_시간이어도_테마가_다르면_예약할_수_있다() {
                // given
                LocalDate date = LocalDate.of(2026, 5, 1);
                Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
                Theme theme = themeRepository.save(
                    Theme.create("피온", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png"));
                Theme otherTheme = themeRepository.save(
                    Theme.create("다른 테마", "다른 설명", "https://roomescape.com/images/themes/other-banner.png"));
                reservationRepository.save(Reservation.create("기존 예약자", date, time, theme));
                ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                    "보예",
                    date,
                    time.getId(),
                    otherTheme.getId()
                );

                // when
                ReservationCreateResponseDto actual = reservationService.saveReservation(request);

                // then
                assertThat(actual).isEqualTo(new ReservationCreateResponseDto(
                    2L,
                    "보예",
                    date,
                    time.getId(),
                    otherTheme.getId()
                ));
                assertThat(reservationRepository.findAllReservations()).hasSize(2);
            }

            @Test
            void 같은_날짜_테마여도_시간이_다르면_예약할_수_있다() {
                // given
                LocalDate date = LocalDate.of(2026, 5, 1);
                Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
                Time otherTime = timeRepository.save(Time.create(LocalTime.of(16, 30)));
                Theme theme = themeRepository.save(
                    Theme.create("피온", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png"));
                reservationRepository.save(Reservation.create("기존 예약자", date, time, theme));
                ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                    "보예",
                    date,
                    otherTime.getId(),
                    theme.getId()
                );

                // when
                ReservationCreateResponseDto actual = reservationService.saveReservation(request);

                // then
                assertThat(actual).isEqualTo(new ReservationCreateResponseDto(
                    2L,
                    "보예",
                    date,
                    otherTime.getId(),
                    theme.getId()
                ));
                assertThat(reservationRepository.findAllReservations()).hasSize(2);
            }

            @Test
            void 같은_시간_테마여도_날짜가_다르면_예약할_수_있다() {
                // given
                LocalDate date = LocalDate.of(2026, 5, 1);
                Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
                Theme theme = themeRepository.save(
                    Theme.create("피온", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png"));
                reservationRepository.save(Reservation.create("기존 예약자", date, time, theme));
                ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                    "보예",
                    date.plusDays(1),
                    time.getId(),
                    theme.getId()
                );

                // when
                ReservationCreateResponseDto actual = reservationService.saveReservation(request);

                // then
                assertThat(actual).isEqualTo(new ReservationCreateResponseDto(
                    2L,
                    "보예",
                    date.plusDays(1),
                    time.getId(),
                    theme.getId()
                ));
                assertThat(reservationRepository.findAllReservations()).hasSize(2);
            }
        }

        @Nested
        class Failed {

            @Test
            void 같은_날짜_시간_테마에_이미_예약이_있으면_예외가_발생한다() {
                // given
                LocalDate date = LocalDate.of(2026, 5, 1);
                Theme theme = themeRepository.save(
                    Theme.create("피온", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png"));
                Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
                reservationRepository.save(Reservation.create("기존 예약자", date, time, theme));
                ReservationCreateRequestDto request = new ReservationCreateRequestDto("보예", date, time.getId(),
                    theme.getId());

                // when & then
                assertThatThrownBy(() -> reservationService.saveReservation(request))
                    .isInstanceOf(ReservationException.class)
                    .hasMessage("이미 예약된 날짜, 시간, 테마입니다.");
            }

            @Test
            void timeId가_존재하지_않으면_예외가_발생한다() {
                // given
                Theme theme = themeRepository.save(
                    Theme.create("피온", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png")
                );
                ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                    "보예",
                    LocalDate.of(2026, 5, 1),
                    999L,
                    theme.getId()
                );

                // when & then
                assertThatThrownBy(() -> reservationService.saveReservation(request))
                    .isInstanceOf(ReservationNotFoundException.class)
                    .hasMessage("조회할 자원이 존재하지 않습니다.");
            }

            @Test
            void themeId가_존재하지_않으면_예외가_발생한다() {
                // given
                Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
                ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                    "보예",
                    LocalDate.of(2026, 5, 1),
                    time.getId(),
                    999L
                );

                // when & then
                assertThatThrownBy(() -> reservationService.saveReservation(request))
                    .isInstanceOf(ReservationNotFoundException.class)
                    .hasMessage("조회할 자원이 존재하지 않습니다.");
            }

            @Test
            void timeId와_themeId가_모두_존재하지_않으면_필드_에러를_모두_포함한다() {
                // given
                ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                    "보예",
                    LocalDate.of(2026, 5, 1),
                    999L,
                    999L
                );

                // when & then
                assertThatThrownBy(() -> reservationService.saveReservation(request))
                    .isInstanceOfSatisfying(ReservationNotFoundException.class, exception -> {
                        assertThat(exception.getFieldErrors())
                            .extracting(FieldErrorResponseDto::field)
                            .containsExactly("timeId", "themeId");
                        assertThat(exception.getFieldErrors())
                            .extracting(FieldErrorResponseDto::message)
                            .containsExactly("존재 하지 않는 시간대입니다.", "존재 하지 않는 테마입니다.");
                    });
            }
        }
    }

    @Nested
    class DeleteReservationByIdTest {

        @Nested
        class Success {

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
                List<ReservationResponseDto> actual = reservationService.getReservations();
                assertThat(actual)
                    .hasSize(1)
                    .extracting(ReservationResponseDto::name)
                    .containsExactly("시오");
            }
        }

        @Nested
        class Failed {

            @Test
            void 예약_ID가_존재하지_않으면_예외가_발생한다() {
                // when & then
                assertThatThrownBy(() -> reservationService.deleteReservationById(999L))
                    .isInstanceOf(ReservationException.class)
                    .hasMessage("예약을 찾을 수 없습니다.");
            }
        }
    }
}
