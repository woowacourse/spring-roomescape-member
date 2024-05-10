package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.exception.ExceptionType.DELETE_USED_THEME;
import static roomescape.exception.ExceptionType.DUPLICATE_THEME;
import static roomescape.fixture.MemberBuilder.DEFAULT_MEMBER;
import static roomescape.fixture.ReservationBuilder.DEFAULT_RESERVATION_WITHOUT_ID;
import static roomescape.fixture.ReservationTimeBuilder.DEFAULT_TIME;
import static roomescape.fixture.ThemeBuilder.DEFAULT_THEME;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.fixture.ReservationBuilder;
import roomescape.fixture.ThemeBuilder;
import roomescape.repository.CollectionReservationRepository;
import roomescape.repository.CollectionReservationTimeRepository;
import roomescape.repository.CollectionThemeRepository;
import roomescape.repository.ThemeRepository;

class ThemeServiceTest {

    private ThemeRepository themeRepository;
    private CollectionReservationTimeRepository reservationTimeRepository;
    private CollectionReservationRepository reservationRepository;
    private ThemeService themeService;

    @BeforeEach
    void initService() {
        reservationTimeRepository = new CollectionReservationTimeRepository();
        reservationRepository = new CollectionReservationRepository();
        themeRepository = new CollectionThemeRepository();
        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @DisplayName("테마가 여러개 있으면 테마를 모두 조회할 수 있다.")
    @Test
    void findAllTest() {
        //given
        themeRepository.save(ThemeBuilder.from("name1"));
        themeRepository.save(ThemeBuilder.from("name2"));
        themeRepository.save(ThemeBuilder.from("name3"));
        themeRepository.save(ThemeBuilder.from("name4"));

        //when
        List<ThemeResponse> themeResponses = themeService.findAll();

        //then
        assertThat(themeResponses).hasSize(4);
    }

    @DisplayName("인기 테마를 조회할 수 있다.")
    @Test
    void findAndOrderByPopularity() {
        themeRepository = new CollectionThemeRepository(reservationRepository);
        themeService = new ThemeService(themeRepository, reservationRepository);
        LocalDate date = LocalDate.now().plusDays(1);

        addReservations(date);

        LocalDate end = date.plusDays(6);
        List<Long> themeIds = themeService.findAndOrderByPopularity(date, end, 10)
                .stream()
                .map(ThemeResponse::id)
                .toList();
        Assertions.assertThat(themeIds)
                .containsExactly(2L, 1L, 3L);
    }

    private void addReservations(LocalDate date) {
        Theme theme1 = themeRepository.save(ThemeBuilder.from("name1"));
        Theme theme2 = themeRepository.save(ThemeBuilder.from("name2"));
        Theme theme3 = themeRepository.save(ThemeBuilder.from("name3"));
        ReservationTime reservationTime1 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(1, 30)));
        ReservationTime reservationTime2 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(2, 30)));
        ReservationTime reservationTime3 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(3, 30)));

        reservationRepository.save(ReservationBuilder.withOutId(DEFAULT_MEMBER, date, reservationTime2, theme2));
        reservationRepository.save(ReservationBuilder.withOutId(DEFAULT_MEMBER, date, reservationTime1, theme2));
        reservationRepository.save(ReservationBuilder.withOutId(DEFAULT_MEMBER, date, reservationTime3, theme2));

        reservationRepository.save(ReservationBuilder.withOutId(DEFAULT_MEMBER, date, reservationTime1, theme1));
        reservationRepository.save(ReservationBuilder.withOutId(DEFAULT_MEMBER, date, reservationTime2, theme1));

        reservationRepository.save(ReservationBuilder.withOutId(DEFAULT_MEMBER, date, reservationTime1, theme3));
    }

    @DisplayName("테마, 시간이 하나 존재할 때")
    @Nested
    class OneThemeTest {
        private ReservationTime defaultTime = DEFAULT_TIME;
        private Theme defaultTheme = DEFAULT_THEME;

        @BeforeEach
        void addDefaultData() {
            defaultTime = reservationTimeRepository.save(defaultTime);
            defaultTheme = themeRepository.save(defaultTheme);
        }

        @DisplayName("동일한 이름의 테마를 예약할 수 없다.")
        @Test
        void duplicatedThemeSaveFailTest() {
            ThemeRequest themeRequest = new ThemeRequest(defaultTheme.getName(), "d", "http://thumbnail");
            assertThatThrownBy(() -> themeService.save(themeRequest))
                    .isInstanceOf(RoomescapeException.class)
                    .hasMessage(DUPLICATE_THEME.getMessage());
        }

        @DisplayName("다른 이름의 테마를 예약할 수 있다.")
        @Test
        void notDuplicatedThemeNameSaveTest() {
            ThemeRequest otherThemeRequest = new ThemeRequest("otherName", "description", "http://thumbnail");
            themeService.save(otherThemeRequest);

            assertThat(themeRepository.findAll())
                    .hasSize(2);
        }

        @DisplayName("테마에 예약이 없다면 테마를 삭제할 수 있다.")
        @Test
        void removeSuccessTest() {
            themeService.delete(defaultTheme.getId());
            assertThat(themeRepository.findById(defaultTheme.getId())).isEmpty();
        }

        @DisplayName("테마에 예약이 있다면 테마를 삭제할 수 없다.")
        @Test
        void removeFailTest() {
            //given
            reservationRepository.save(DEFAULT_RESERVATION_WITHOUT_ID);

            //when & then
            assertThatThrownBy(() -> themeService.delete(defaultTheme.getId()))
                    .isInstanceOf(RoomescapeException.class)
                    .hasMessage(DELETE_USED_THEME.getMessage());
        }

        @DisplayName("존재하지 않는 테마 id로 삭제하더라도 오류로 간주하지 않는다.")
        @Test
        void notExistThemeDeleteTest() {
            assertThatCode(() -> themeService.delete(2L))
                    .doesNotThrowAnyException();
        }
    }
}
