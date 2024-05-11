package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.ExistReservationException;
import roomescape.exception.IllegalThemeException;
import roomescape.repository.mock.InMemoryReservationDao;
import roomescape.repository.mock.InMemoryThemeDao;
import roomescape.repository.mock.InMemoryTimeDao;

class ThemeServiceTest {

    private final InMemoryThemeDao themeDao = new InMemoryThemeDao();
    private final InMemoryTimeDao timeDao = new InMemoryTimeDao();
    private final InMemoryReservationDao reservationDao = new InMemoryReservationDao();
    private final ThemeService themeService = new ThemeService(themeDao, reservationDao);

    @BeforeEach
    void setUp() {
        themeDao.themes = new ArrayList<>();
        timeDao.times = new ArrayList<>();
        reservationDao.reservations = new ArrayList<>();
    }

    @Test
    void findAll() {
        // given
        themeDao.themes.add(new Theme(1L,"이름", "설명", "썸네일"));

        // when
        List<ThemeResponse> allThemes = themeService.findAll();

        // then
        assertThat(allThemes).containsExactly(new ThemeResponse(1L, "이름", "설명", "썸네일"));
    }

    @DisplayName("중복인 테마를 생성하려고 하면 예외를 발생한다.")
    @Test
    void save_duplicate_IllegalThemeException() {
        // given
        themeDao.themes.add(new Theme(1L,"이름", "설명", "썸네일"));

        // when && then
        assertThatThrownBy(() -> themeService.save(new ThemeCreateRequest(null, "이름", "설명", "썸네일")))
                .isInstanceOf(IllegalThemeException.class);
    }

    @DisplayName("테마를 저장한 아이디를 반환한다.")
    @Test
    void save() {
        // given && when
        long id = themeService.save(new ThemeCreateRequest(null, "이름", "설명", "썸네일"));

        // then
        assertThat(id).isEqualTo(1);
    }

    @DisplayName("예약이 존재하는 경우 테마를 삭제하면 예외가 발생한다.")
    @Test
    void deleteById() {
        // given
        themeDao.themes.add(new Theme(1L,"이름", "설명", "썸네일"));
        timeDao.times.add(new ReservationTime(1L, LocalTime.of(10, 0)));
        reservationDao.reservations.add(new Reservation(1L, LocalDate.of(2099,12,31),
                new ReservationTime(1L, LocalTime.of(10,0)),
                new Theme(1L, "이름", "설명", "썸네일"),
                new Member(1, "커비", "email", "password", Role.MEMBER)
        ));

        // when && then
        assertThatThrownBy(() -> themeService.deleteById(1L))
                .isInstanceOf(ExistReservationException.class);
    }
}
