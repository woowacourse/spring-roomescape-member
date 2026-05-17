package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DuplicatedResourceException;
import roomescape.exception.ResourceNotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReservationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ThemeService themeService;
    @Autowired
    private ReservationDao reservationDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE theme RESTART IDENTITY");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void 존재하지_않는_예약_삭제_시_에러() {
        //given
        Long fakeId = 999L;

        //when&then
        assertThatThrownBy(() -> reservationService.deleteByIdFromAdmin(fakeId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("존재하지 않는 예약");
    }

    @Test
    void 예약을_저장하고_조회한다() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Reservation savedReservation = reservationService.save("맥스", LocalDate.of(2030, 5, 6), savedTime.id(),
                savedTheme.id());

        //when
        List<Reservation> reservations = reservationService.findAll();

        //then
        assertThat(reservations.getFirst().id()).isEqualTo(savedReservation.id());
    }

    @Test
    void 예약을_저장하고_삭제한다() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Reservation savedReservation = reservationService.save("맥스", LocalDate.of(2030, 5, 6), savedTime.id(),
                savedTheme.id());

        //when
        reservationService.deleteByIdFromAdmin(savedReservation.id());

        //then
        assertThat(reservationService.findAll()).hasSize(0);
    }

    @Test
    void 중복예약_불가() {
        //given
        ReservationTime savedTime = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        reservationService.save("맥스", LocalDate.of(2030, 5, 6), savedTime.id(), savedTheme.id());

        //when & then
        assertThatThrownBy(() -> reservationService.save(
                "피노",
                LocalDate.of(2030, 5, 6),
                savedTime.id(),
                savedTheme.id()))
                .isInstanceOf(DuplicatedResourceException.class)
                .hasMessageContaining("이미 존재하는 예약");
    }

    @Test
    void 이름으로_예약찾기() {
        //given
        String pobi = "포비";
        ReservationTime reservationTimeTen = reservationTimeService.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime reservationTimeEleven = reservationTimeService.save(
                new ReservationTime(LocalTime.parse("11:00")));
        Theme theme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        reservationService.save(pobi, LocalDate.of(2026, 6, 1), reservationTimeTen.id(), theme.id());
        reservationService.save(pobi, LocalDate.of(2026, 6, 1), reservationTimeEleven.id(), theme.id());

        //when
        List<Reservation> reservations = reservationService.findByName(pobi);

        //then
        assertThat(reservations).hasSize(2);
    }

    @Test
    void 과거는_삭제_불가() {
        //given
        String pobi = "포비";
        ReservationTime reservationTimeTen = reservationTimeService.save(new ReservationTime(LocalTime.parse("10:00")));
        Theme theme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        LocalDate localDate = LocalDate.of(2026, 5, 1);
        Reservation savedReservation = reservationDao.save(
                new Reservation(pobi, localDate, reservationTimeTen, theme));

        //when && then
        assertThatThrownBy(() -> reservationService.deleteByIdFromMember(savedReservation.id()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("과거");
    }

    @Test
    void 예약_날짜_시간_수정_성공() {
        //given
        String pobi = "포비";
        ReservationTime reservationTimeTen = reservationTimeService.save(new ReservationTime(LocalTime.parse("10:00")));
        Theme theme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        LocalDate localDate = LocalDate.of(2030, 1, 1);
        Reservation savedReservation = reservationDao.save(
                new Reservation(pobi, localDate, reservationTimeTen, theme));
        LocalDate newDate = LocalDate.of(2031, 1, 1);

        //when
        reservationService.update(savedReservation.id(), newDate, reservationTimeTen.id(), theme.id());

        //then
        assertThat(reservationService.findByName(pobi).getFirst().date())
                .isEqualTo(LocalDate.of(2031, 1, 1));
    }

    @Test
    void 과거_예약은_수정_실패() {
        //given
        String pobi = "포비";
        ReservationTime reservationTimeTen = reservationTimeService.save(new ReservationTime(LocalTime.parse("10:00")));
        Theme theme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        LocalDate localDate = LocalDate.of(2000, 12, 16);
        Reservation savedReservation = reservationDao.save(
                new Reservation(pobi, localDate, reservationTimeTen, theme));
        LocalDate newDate = LocalDate.of(2031, 1, 1);

        //when && then
        assertThatThrownBy(
                () -> reservationService.update(savedReservation.id(), newDate, reservationTimeTen.id(),
                        theme.id()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("과거 예약");
    }

    @Test
    void 중복된_예약이_있어서_수정_실패() {
        //given
        String pobi = "포비";
        ReservationTime reservationTimeTen = reservationTimeService.save(new ReservationTime(LocalTime.parse("10:00")));
        Theme theme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        LocalDate localDate = LocalDate.of(2030, 1, 1);
        LocalDate duplicatedDate = LocalDate.of(2031, 1, 1);
        Reservation savedReservation = reservationDao.save(
                new Reservation(pobi, localDate, reservationTimeTen, theme));
        Reservation duplicatedReservation = reservationDao.save(
                new Reservation(pobi, duplicatedDate, reservationTimeTen, theme));
        LocalDate newDate = LocalDate.of(2031, 1, 1);

        //when && then
        assertThatThrownBy(() -> reservationService.update(
                savedReservation.id(),
                newDate,
                reservationTimeTen.id(),
                theme.id()))
                .isInstanceOf(DuplicatedResourceException.class)
                .hasMessageContaining("이미 존재");
    }

    @Test
    void 존재하지_않는_예약을_수정하면_예외가_발생한다() {
        //given
        Long fakeId = 9999L;
        LocalDate newDate = LocalDate.of(2031, 1, 1);

        //when & then
        assertThatThrownBy(() -> reservationService.update(fakeId, newDate, 1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("존재하지 않습니다");
    }

    @Test
    void 같은값으로_업데이트_할_시_성공() {
        //given
        String pobi = "포비";
        ReservationTime reservationTimeTen = reservationTimeService.save(new ReservationTime(LocalTime.parse("10:00")));
        Theme theme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        LocalDate localDate = LocalDate.of(2030, 1, 1);
        Reservation savedReservation = reservationDao.save(
                new Reservation(pobi, localDate, reservationTimeTen, theme));

        //when
        reservationService.update(savedReservation.id(), localDate, reservationTimeTen.id(), theme.id());

        //then
        Reservation updatedReservation = reservationDao.findById(savedReservation.id()).get();
        assertThat(updatedReservation.date()).isEqualTo(localDate);
    }
}
