package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.exception.ExceptionType.DUPLICATE_THEME;
import static roomescape.exception.ExceptionType.INVALID_DELETE_THEME;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.repository.CollectionReservationRepository;
import roomescape.repository.CollectionReservationTimeRepository;
import roomescape.repository.CollectionThemeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

class ThemeServiceTest {

    private ThemeRepository themeRepository;
    private CollectionReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;
    private ThemeService themeService;

    @BeforeEach
    void initService() {
        themeRepository = new CollectionThemeRepository();
        reservationTimeRepository = new CollectionReservationTimeRepository();
        reservationRepository = new CollectionReservationRepository(reservationTimeRepository);
        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @DisplayName("테마, 시간이 하나 존재할 때")
    @Nested
    class OneThemeTest {
        private ReservationTime defaultTime = new ReservationTime(LocalTime.now().plusMinutes(5));
        private Theme defaultTheme = new Theme("name", "description", "thumbnail");

        @BeforeEach
        void addDefaultData() {
            defaultTime = reservationTimeRepository.save(defaultTime);
            defaultTheme = themeRepository.save(defaultTheme);
        }

        @DisplayName("동일한 이름의 테마를 예약할 수 없다.")
        @Test
        void duplicatedThemeSaveFailTest() {
            assertThatThrownBy(() -> themeService.save(new ThemeRequest(
                    defaultTheme.getName(), "description", "thumbnail"
            ))).isInstanceOf(RoomescapeException.class)
                    .hasMessage(DUPLICATE_THEME.getMessage());
        }

        @DisplayName("다른 이름의 테마를 예약할 수 있다.")
        @Test
        void notDuplicatedThemeNameSaveTest() {
            themeService.save(new ThemeRequest("otherName", "description", "thumbnail"));

            assertThat(themeRepository.findAll())
                    .hasSize(2);
        }

        @DisplayName("테마에 예약이 없다면 테마를 삭제할 수 있다.")
        @Test
        void removeSuccessTest() {

            themeService.delete(1L);
            assertThat(themeRepository.findById(1L)).isEmpty();
        }

        @DisplayName("테마에 예약이 있다면 테마를 삭제할 수 없다.")
        @Test
        void removeFailTest() {
            //given
            reservationRepository.save(new Reservation(
                    "name", LocalDate.now().plusDays(1), defaultTime, defaultTheme));

            //when & then
            assertThatThrownBy(() -> themeService.delete(1L))
                    .isInstanceOf(RoomescapeException.class)
                    .hasMessage(INVALID_DELETE_THEME.getMessage());
        }

        @DisplayName("존재하지 않는 테마 id로 삭제하더라도 오류로 간주하지 않는다.")
        @Test
        void notExistThemeDeleteTest() {
            assertThatCode(() -> themeService.delete(2L))
                    .doesNotThrowAnyException();
        }
    }

    @DisplayName("테마가 여러개 있으면 테마를 모두 조회할 수 있다.")
    @Test
    void findAllTest() {
        //given
        themeRepository.save(new Theme("name1", "description1", "thumbnail1"));
        themeRepository.save(new Theme("name2", "description2", "thumbnail2"));
        themeRepository.save(new Theme("name3", "description3", "thumbnail3"));
        themeRepository.save(new Theme("name4", "description4", "thumbnail4"));

        //when
        List<ThemeResponse> themeResponses = themeService.findAll();

        //then
        assertThat(themeResponses).hasSize(4);
    }
}
