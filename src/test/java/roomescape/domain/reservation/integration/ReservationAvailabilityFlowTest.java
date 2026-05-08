package roomescape.domain.reservation.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDTO;
import roomescape.domain.reservation.repository.JdbcReservationRepository;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.JdbcThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.dto.response.TimeResponseDTO;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.JdbcTimeRepository;
import roomescape.domain.time.repository.TimeRepository;
import roomescape.domain.time.service.TimeService;

class ReservationAvailabilityFlowTest {

    private ReservationService reservationService;
    private TimeService timeService;
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

        ReservationRepository reservationRepository = new JdbcReservationRepository(dataSource);
        timeRepository = new JdbcTimeRepository(dataSource);
        themeRepository = new JdbcThemeRepository(dataSource);
        reservationService = new ReservationService(reservationRepository, timeRepository, themeRepository);
        timeService = new TimeService(reservationRepository, timeRepository);
    }

    @Test
    void 예약을_생성하면_같은_날짜와_테마의_예약_가능_시간에서_제외된다() {
        // given
        LocalDate date = LocalDate.of(2026, 5, 10);
        Time time1 = timeRepository.save(Time.create(LocalTime.of(10, 0)));
        Time time2 = timeRepository.save(Time.create(LocalTime.of(11, 0)));
        Theme theme = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));

        // when
        List<TimeResponseDTO> beforeReservation = timeService.getAvailableTimes(date, theme.getId());
        reservationService.saveReservation(new ReservationCreateRequestDTO("예약자", date, time1.getId(), theme.getId()));
        List<TimeResponseDTO> afterReservation = timeService.getAvailableTimes(date, theme.getId());

        // then
        assertThat(beforeReservation)
            .extracting(TimeResponseDTO::id)
            .containsExactly(time1.getId(), time2.getId());
        assertThat(afterReservation)
            .extracting(TimeResponseDTO::id)
            .containsExactly(time2.getId());
    }
}
