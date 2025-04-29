package roomescape.service;

import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.fake.FakeThemeRepository;
import roomescape.service.param.CreateReservationParam;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;
import roomescape.service.result.ThemeResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceTest {

    public static final LocalDate RESERVATION_DATE = LocalDate.now().plusDays(1);

    @Test
    void 예약을_생성한다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        FakeThemeRepository fakeThemeRepository = new FakeThemeRepository(theme);
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(reservationTime);
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository();
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao, fakeThemeRepository);
        CreateReservationParam createReservationParam = new CreateReservationParam("test", RESERVATION_DATE, 1L, 1L);

        //when
        Long createdId = reservationService.create(createReservationParam);

        //then
        assertThat(fakeReservationDao.findById(createdId))
                .hasValue(new Reservation(1L, "test", RESERVATION_DATE, reservationTime, theme));
    }

    @Test
    void 예약을_생성할때_timeId가_데이터베이스에_존재하지_않는다면_예외가_발생한다() {
        //give
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        FakeThemeRepository fakeThemeRepository = new FakeThemeRepository(theme);
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository();
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository();
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao, fakeThemeRepository);
        CreateReservationParam createReservationParam = new CreateReservationParam("test", RESERVATION_DATE, 1L, 1L);

        //when & then
        assertThatThrownBy(() -> reservationService.create(createReservationParam))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("1에 해당하는 reservation_time 튜플이 없습니다.");
    }

    @Test
    void id값으로_예약을_삭제할_수_있다() {
        //given
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        FakeThemeRepository fakeThemeRepository = new FakeThemeRepository(theme);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        Reservation reservation = new Reservation(1L, "test", LocalDate.now(), reservationTime, theme);
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(reservationTime);
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository(reservation);
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao, fakeThemeRepository);

        //when
        reservationService.deleteById(1L);

        //then
        assertThat(fakeReservationDao.findById(1L)).isEmpty();
    }

    @Test
    void 전체_예약을_조회할_수_있다() {
        //given
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(12, 1));
        ReservationTime reservationTime2 = new ReservationTime(1L, LocalTime.of(13, 1));

        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        FakeThemeRepository fakeThemeRepository = new FakeThemeRepository(theme);
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(reservationTime1, reservationTime2);
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository(
                new Reservation(1L, "test1", RESERVATION_DATE, reservationTime1, theme),
                new Reservation(2L, "test2", RESERVATION_DATE, reservationTime2, theme)
        );
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao, fakeThemeRepository);

        //when
        List<ReservationResult> reservationResults = reservationService.findAll();

        //then
        assertThat(reservationResults).isEqualTo(List.of(
                new ReservationResult(1L, "test1", RESERVATION_DATE,
                        new ReservationTimeResult(1L, LocalTime.of(12, 1)),
                        new ThemeResult(1L, "test", "description", "thumbnail")),
                new ReservationResult(2L, "test2", RESERVATION_DATE,
                        new ReservationTimeResult(1L, LocalTime.of(13, 1)),
                        new ThemeResult(1L, "test", "description", "thumbnail"))
        ));
    }

    @Test
    void id_로_예약을_찾을_수_있다() {
        //given
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        FakeThemeRepository fakeThemeRepository = new FakeThemeRepository(theme);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 1));
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(reservationTime);
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository(
                new Reservation(1L, "test1", RESERVATION_DATE, reservationTime, theme));
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao, fakeThemeRepository);

        //when
        ReservationResult reservationResult = reservationService.findById(1L);

        //then
        assertThat(reservationResult).isEqualTo(
                new ReservationResult(1L, "test1", RESERVATION_DATE,
                        new ReservationTimeResult(1L, LocalTime.of(12, 1)),
                        new ThemeResult(1L, "test", "description", "thumbnail"))
        );
    }

    @Test
    void id에_해당하는_예약이_없는경우_예외가_발생한다() {
        //given
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        FakeThemeRepository fakeThemeRepository = new FakeThemeRepository(theme);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(13, 1));
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(reservationTime);
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository(
                new Reservation(1L, "test1", LocalDate.now().plusDays(1), reservationTime, theme));
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao, fakeThemeRepository);

        //when & then
        assertThatThrownBy(() -> reservationService.findById(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("2에 해당하는 reservation 튜플이 없습니다.");
    }

    @Test
    void 날짜와_시간이_중복된_예약이_있으면_예외가_발생한다() {
        //given
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        FakeThemeRepository fakeThemeRepository = new FakeThemeRepository(theme);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(13, 1));
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(reservationTime);
        LocalDate reservationDate = LocalDate.now().plusDays(1);
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository(
                new Reservation(1L, "test1", reservationDate, reservationTime, theme));
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao, fakeThemeRepository);

        //when & then
        assertThatThrownBy(() -> reservationService.create(new CreateReservationParam("test2", reservationDate, 1L, 1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜와 시간이 중복된 예약이 존재합니다.");
    }
}