package roomescape.theme.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.global.exception.badRequest.BadRequestException;
import roomescape.global.exception.conflict.ThemeNameConflictException;
import roomescape.global.exception.notFound.ThemeNotFoundException;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.request.ThemeRequest;
import roomescape.theme.service.dto.response.ThemeResponse;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeServiceTest {
    private final ReservationRepository reservationRepository = new FakeReservationRepository();
    private final ThemeRepository themeRepository = new FakeThemeRepository(reservationRepository);
    private final ThemeService service = new ThemeService(themeRepository);

    @DisplayName("테마를 생성할 수 있다.")
    @Test
    void createTheme() {
        // given
        ThemeRequest request = new ThemeRequest("theme", "desc", "https://example.com/thumbnail.png");

        // when
        ThemeResponse response = service.createTheme(request);

        // then
        assertThat(response).isNotNull();
        assertThat(themeRepository.findById(response.id())).isNotEmpty();
    }

    @DisplayName("테마를 삭제할 수 있다.")
    @Test
    void deleteTheme() {
        // given
        themeRepository.save(new Theme(1L, "theme", "desc", "https://example.com/thumbnail.png"));

        // when
        service.deleteTheme(1L);

        // then
        assertThat(themeRepository.findById(1L)).isEmpty();
    }

    @DisplayName("중복되는 테마 이름이 있을 경우 생성할 수 없다.")
    @Test
    void duplicateByName() {
        // given
        String name = "밍곰 테마";
        themeRepository.save(new Theme(
                1L,
                name,
                "진격의 밍곰",
                "진격의 밍곰 썸네일"
        ));

        ThemeRequest request = new ThemeRequest(
                name,
                "우테코 레벨2 탈출",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        // when & then
        assertThatThrownBy(() -> service.createTheme(request))
                .isInstanceOf(ThemeNameConflictException.class);
    }

    @DisplayName("id가 존재하지 않을 경우 삭제할 수 없다.")
    @Test
    void cannotDeleteNotExist() {
        // given

        // when & then
        assertThatThrownBy(() -> service.deleteTheme(1L))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @DisplayName("인기 테마 조회는 1개 이상 100개 이하까지만 가능하며 그 이외의 범위가 주어지면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void invalidLimit(final int limit) {
        // given

        // when & then
        assertThatThrownBy(() -> {
            service.getPopularThemes(limit);
        }).isInstanceOf(BadRequestException.class);
    }

    @DisplayName("오늘을 제외한 최근 일주일 동안의 예약 내역을 토대로, 지정 개수만큼의 인기있는 테마를 순서대로 반환할 수 있다.")
    @Test
    void popularThemes() {
        // given
        LocalDate now = LocalDate.now();
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        themeRepository.save(new Theme(1L, "theme1", "t", "t"));
        themeRepository.save(new Theme(2L, "theme2", "t", "t"));
        themeRepository.save(new Theme(3L, "theme3", "t", "t"));

        reservationRepository.save(new Reservation(1L, 1L, now.minusDays(1), time, 1L));
        reservationRepository.save(new Reservation(2L, 1L, now.minusDays(2), time, 1L));
        reservationRepository.save(new Reservation(3L, 1L, now.minusDays(2), time, 2L));

        List<ThemeResponse> expect = List.of(
                new ThemeResponse(1L, "theme1", "t", "t"),
                new ThemeResponse(2L, "theme2", "t", "t"),
                new ThemeResponse(3L, "theme3", "t", "t")
        );

        // when
        List<ThemeResponse> actual = service.getPopularThemes(expect.size());

        // then
        assertThat(actual).containsExactlyElementsOf(expect);
    }
}
