package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.date.domain.ReservationDate;
import roomescape.date.repository.JdbcReservationDateRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.JdbcTemplateReservationTimeRepository;

@JdbcTest
class ReservationRepositoryTest {

    private final String name = "한다";
    private final LocalDate date = LocalDate.now().plusMonths(1);
    private ReservationTime time;
    private ReservationDate reservationDate;
    private Theme theme;

    private JdbcTemplateReservationRepository jdbcTemplateReservationRepository;
    private JdbcTemplateReservationTimeRepository jdbcTemplateReservationTimeRepository;
    private JdbcReservationDateRepository jdbcReservationDateRepository;
    private JdbcThemeRepository jdbcThemeRepository;
    private Long timeId;
    private Long reservationId;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplateReservationRepository = new JdbcTemplateReservationRepository(jdbcTemplate);
        jdbcTemplateReservationTimeRepository = new JdbcTemplateReservationTimeRepository(jdbcTemplate);
        jdbcReservationDateRepository = new JdbcReservationDateRepository(jdbcTemplate);
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);

        timeId = jdbcTemplateReservationTimeRepository.save(ReservationTime.create(LocalTime.of(15, 40)));
        reservationDate = jdbcReservationDateRepository.save(ReservationDate.create(date));
        time = jdbcTemplateReservationTimeRepository.findById(timeId).get();
        theme = jdbcThemeRepository.save(Theme.create("테마", "설명", "썸네일"));

        reservationId = jdbcTemplateReservationRepository.save(Reservation.create(name, reservationDate.date(), time.startAt(), theme));
        jdbcTemplateReservationRepository.save(Reservation.create("판다", reservationDate.date(), time.startAt(), theme));
    }

    @Test
    @DisplayName("모든 예약 정보를 조회한다.")
    void findAll() {
        assertThat(jdbcTemplateReservationRepository.findAll())
                .hasSize(2);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void save() {
        //given & when
        jdbcTemplateReservationRepository.save(Reservation.create("새로운사람", date, time.startAt(), theme));

        //then
        assertThat(jdbcTemplateReservationRepository.findAll())
                .hasSize(3);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        //given & when
        jdbcTemplateReservationRepository.delete(reservationId);

        //then
        assertThat(jdbcTemplateReservationRepository.findAll())
                .hasSize(1);
    }

    @Test
    @DisplayName("예약 날짜와 시간 ID 정보로 존재하는지 확인한다.")
    void exitsByDateAndTimeId() {
        // given
        LocalDate wrongDate = LocalDate.now().plusWeeks(3);

        // when & then
        assertThat(jdbcTemplateReservationRepository.existsByDateAndTimeId(date, time.startAt()))
                .isTrue();
        assertThat(jdbcTemplateReservationRepository.existsByDateAndTimeId(wrongDate, time.startAt()))
                .isFalse();
    }
}
