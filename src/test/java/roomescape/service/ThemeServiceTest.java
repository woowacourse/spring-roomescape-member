package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.exception.ExceptionType.DELETE_USED_THEME;
import static roomescape.exception.ExceptionType.DUPLICATE_THEME;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Sha256Encryptor;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.repository.CollectionMemberRepository;
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
        themeRepository.save(new Theme("name1", "description1", "http://thumbnail1"));
        themeRepository.save(new Theme("name2", "description2", "http://thumbnail2"));
        themeRepository.save(new Theme("name3", "description3", "http://thumbnail3"));
        themeRepository.save(new Theme("name4", "description4", "http://thumbnail4"));

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
        Theme theme1 = themeRepository.save(new Theme("name1", "description1", "http://thumbnail1"));
        Theme theme2 = themeRepository.save(new Theme("name2", "description2", "http://thumbnail2"));
        Theme theme3 = themeRepository.save(new Theme("name3", "description3", "http://thumbnail3"));
        ReservationTime reservationTime1 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(1, 30)));
        ReservationTime reservationTime2 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(2, 30)));
        ReservationTime reservationTime3 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(3, 30)));
        Member member = new Member(1L, "name", "email@email.com", new Sha256Encryptor().encrypt("1234"));

        ReservationService reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
                themeRepository, new CollectionMemberRepository(List.of(member)));
        reservationService.save(new ReservationRequest(date, member.getId(), reservationTime2.getId(), theme2.getId()));
        reservationService.save(new ReservationRequest(date, member.getId(), reservationTime1.getId(), theme2.getId()));
        reservationService.save(new ReservationRequest(date, member.getId(), reservationTime3.getId(), theme2.getId()));

        reservationService.save(new ReservationRequest(date, member.getId(), reservationTime1.getId(), theme1.getId()));
        reservationService.save(new ReservationRequest(date, member.getId(), reservationTime2.getId(), theme1.getId()));

        reservationService.save(new ReservationRequest(date, member.getId(), reservationTime1.getId(), theme3.getId()));
    }

    @DisplayName("테마, 시간이 하나 존재할 때")
    @Nested
    class OneThemeTest {
        private ReservationTime defaultTime = new ReservationTime(LocalTime.now().plusMinutes(5));
        private Theme defaultTheme = new Theme("name", "description", "http://thumbnail");

        @BeforeEach
        void addDefaultData() {
            defaultTime = reservationTimeRepository.save(defaultTime);
            defaultTheme = themeRepository.save(defaultTheme);
        }

        @DisplayName("동일한 이름의 테마를 예약할 수 없다.")
        @Test
        void duplicatedThemeSaveFailTest() {
            assertThatThrownBy(() -> themeService.save(new ThemeRequest(
                    defaultTheme.getName(), "description", "http://thumbnail"
            ))).isInstanceOf(RoomescapeException.class)
                    .hasMessage(DUPLICATE_THEME.getMessage());
        }

        @DisplayName("다른 이름의 테마를 예약할 수 있다.")
        @Test
        void notDuplicatedThemeNameSaveTest() {
            themeService.save(new ThemeRequest("otherName", "description", "http://thumbnail"));

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
            Member member = new Member(1L, "name", "email@email.com", new Sha256Encryptor().encrypt("1234"));
            reservationRepository.save(new Reservation(
                    member, LocalDate.now().plusDays(1), defaultTime, defaultTheme));

            //when & then
            assertThatThrownBy(() -> themeService.delete(1L))
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
