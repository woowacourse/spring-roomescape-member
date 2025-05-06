package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.FakeReservationDaoImpl;
import roomescape.dao.FakeReservationTimeDaoImpl;
import roomescape.dao.FakeThemeDaoImpl;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.repository.impl.ThemeRepositoryImpl;

public class ThemeRepositoryTest {

    private final ThemeDao themeDao = new FakeThemeDaoImpl();
    private final ReservationTimeDao reservationTimeDao = new FakeReservationTimeDaoImpl();
    private final ReservationDao reservationDao = new FakeReservationDaoImpl();
    private final ThemeRepository themeRepository = new ThemeRepositoryImpl(themeDao,
        reservationDao);

    @Test
    @DisplayName("존재하지 않는 테마ID를 가져오려고 할 경우, 예외가 발생해야 한다.")
    void not_exist_theme_id_get_then_throw_exception() {
        assertThatThrownBy(() -> themeRepository.findById(999L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void delete_already_use_other_theme_id_then_throw_exception() {

        //given
        Theme theme = new Theme("공포", "공포테마입니다", "http://aaa");
        long savedThemeId = themeDao.save(theme);
        theme.setId(savedThemeId);

        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedReservationTimeId = reservationTimeDao.save(reservationTime);
        reservationTime.setId(savedReservationTimeId);

        Reservation reservation = new Reservation(
            new Person("jenson"),
            new ReservationDate(LocalDate.of(2025, 5, 5)),
            reservationTime,
            theme
        );

        long savedId = reservationDao.save(reservation);
        reservation.setId(savedId);

        //when
        assertThatThrownBy(
            () -> themeRepository.delete(savedReservationTimeId))
            .isInstanceOf(InvalidReservationException.class)
            .hasMessage("이미 예약된 테마를 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하는 테마ID를 가져오려고 할 경우, 성공해야 한다")
    void exist_theme_id_get_then_success() {
        Theme theme = new Theme("공포", "공포테마입니다", "http://aaa");
        long savedId = themeDao.save(theme);
        theme.setId(savedId);
        assertThatCode(() -> themeRepository.findById(savedId)).doesNotThrowAnyException();
    }


    @Test
    @DisplayName("존재하지 않는 테마ID를 삭제하려고 할 경우, 예외가 발생해야 한다.")
    void not_exist_theme_id_delete_then_throw_exception() {
        Theme theme = new Theme("공포", "공포테마입니다", "http://aaa");
        long savedId = themeDao.save(theme);
        theme.setId(savedId);
        assertThatCode(() -> themeRepository.delete(savedId)).doesNotThrowAnyException();
    }
}
