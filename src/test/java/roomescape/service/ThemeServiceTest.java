package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.error.ReservationException;
import roomescape.stub.StubReservationRepository;
import roomescape.stub.StubThemeRepository;

class ThemeServiceTest {

    private final ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
    private final ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(11, 0));

    private final Theme theme1 = new Theme(1L, "이름1", "설명1", "썸네일1");
    private final Theme theme2 = new Theme(2L, "이름2", "이름2", "이름2");

    private final Reservation reservation1 = new Reservation(1L, "테스트", LocalDate.of(2025, 5, 11), reservationTime1, theme1);
    private final Reservation reservation2 = new Reservation(2L, "테스트2", LocalDate.of(2025, 6, 11), reservationTime2, theme2);

    private StubReservationRepository stubReservationRepository;

    private ThemeService sut;

    @BeforeEach
    void setUp() {
        stubReservationRepository = new StubReservationRepository(reservation1, reservation2);
        StubThemeRepository stubThemeRepository = new StubThemeRepository(theme1, theme2);

        sut = new ThemeService(stubThemeRepository, stubReservationRepository);
    }

    @Test
    void 테마가_저장된다() {
        // given
        ThemeRequest request = new ThemeRequest("이름3", "설명3", "썸네일3");

        // when
        ThemeResponse response = sut.saveTheme(request);

        // then
        assertSoftly(soft -> {
            soft.assertThat(response.id()).isNotNull();
            soft.assertThat(response.name()).isEqualTo("이름3");
            soft.assertThat(response.description()).isEqualTo("설명3");
            soft.assertThat(response.thumbnail()).isEqualTo("썸네일3");
        });
    }

    @Test
    void 모든_테마를_조회한다() {
        // when
        List<ThemeResponse> all = sut.findAll();

        // then
        assertThat(all).hasSize(2);
    }

    @Test
    void 테마가_삭제된다() {
        // when
        sut.delete(theme1.getId());

        // then
        List<ThemeResponse> afterDelete = sut.findAll();
        assertThat(afterDelete).hasSize(1);
    }

    @Test
    void 예약이_존재하는_테마를_삭제하지_못_한다() {
        // given
        stubReservationRepository.setExistsByThemeId(true);

        // when
        // then
        assertThatThrownBy(() -> sut.delete(1L))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 테마로 예약된 건이 존재합니다.");
    }
}
