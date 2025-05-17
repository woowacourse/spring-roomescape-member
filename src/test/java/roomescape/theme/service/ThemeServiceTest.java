package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import roomescape.error.ReservationException;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
import roomescape.member.domain.Password;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.fake.FakeReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.fake.FakeThemeRepository;

class ThemeServiceTest {
    private final LocalTime t1 = LocalTime.of(10, 0);
    private final LocalTime t2 = LocalTime.of(11, 0);

    private final ReservationTime rt1 = new ReservationTime(1L, t1);
    private final ReservationTime rt2 = new ReservationTime(2L, t2);

    private final Theme th1 = new Theme(1L, "이름1", "설명1", "썸네일1");
    private final Theme th2 = new Theme(2L, "이름2", "이름2", "이름2");

    Member member1 = Member.builder()
            .name("유저")
            .email("email")
            .password(Password.createForMember("비번"))
            .role(MemberRole.MEMBER).build();

    private final Reservation r1 = new Reservation(1L, LocalDate.of(2025, 5, 11), rt1, th1, member1);
    private final Reservation r2 = new Reservation(2L, LocalDate.of(2025, 6, 11), rt2, th2, member1);

    private final FakeReservationRepository fakeReservationRepo = new FakeReservationRepository(r1, r2);
    private final FakeThemeRepository fakeThemeRepo = new FakeThemeRepository(th1, th2);

    private final ThemeService service = new ThemeService(fakeThemeRepo, fakeReservationRepo);


    @Test
    void 테마가_저장된다() {
        // given
        ThemeRequest request = new ThemeRequest("이름3", "설명3", "썸네일3");

        // when
        ThemeResponse response = service.saveTheme(request);

        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(response.name()).isEqualTo(request.name());
            soft.assertThat(response.description()).isEqualTo(request.description());
            soft.assertThat(response.thumbnail()).isEqualTo(request.thumbnail());
        });
    }

    @Test
    void 모든_테마를_조회한다() {
        // when
        List<ThemeResponse> all = service.findAll();

        // then
        assertThat(all).hasSize(2);
    }

    @Test
    void 테마가_삭제된다() {
        // given
        assertThat(service.findAll()).hasSize(2);

        // when
        service.delete(1L);

        // then
        List<ThemeResponse> afterDelete = service.findAll();
        assertThat(afterDelete)
                .hasSize(1)
                .extracting(ThemeResponse::id)
                .doesNotContain(1L);
    }

    @Test
    void 예약이_존재하는_테마를_삭제하지_못_한다() {
        // given
        fakeReservationRepo.setExistsByThemeId(true);

        // when
        // then
        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 테마로 예약된 건이 존재합니다.");
        fakeReservationRepo.setExistsByThemeId(false);
    }
}
