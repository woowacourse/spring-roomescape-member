package roomescape.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.entity.Member;
import roomescape.domain.entity.Reservation;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.entity.Role;
import roomescape.domain.entity.Theme;
import roomescape.error.NotFoundException;

@JdbcTest
@Import({
        JdbcThemeRepository.class,
        JdbcReservationRepository.class,
        JdbcMemberRepository.class,
        JdbcReservationTimeRepository.class
})
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcThemeRepository sut;

    @Autowired
    private JdbcReservationRepository reservationRepository;

    @Autowired
    private JdbcMemberRepository memberRepository;

    @Autowired
    private JdbcReservationTimeRepository timeRepository;

    private Member savedMember;
    private ReservationTime savedTime;

    @BeforeEach
    void setUp() {
        savedMember = memberRepository.save(new Member(null, "홍길동", "hong@example.com", "pw123", Role.USER));
        savedTime = timeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
    }

    @DisplayName("테마를 올바르게 저장한다")
    @Test
    void save() {
        // given
        var theme = new Theme("테마이름", "테마설명", "테마썸네일");

        // when
        var saved = sut.save(theme);

        // then
        assertSoftly(soft -> {
            soft.assertThat(saved.getId()).isNotNull();
            soft.assertThat(saved.getName()).isEqualTo("테마이름");
            soft.assertThat(saved.getDescription()).isEqualTo("테마설명");
            soft.assertThat(saved.getThumbnail()).isEqualTo("테마썸네일");
        });
    }

    @DisplayName("모든 테마를 조회한다")
    @Test
    void findAll() {
        // given
        sut.save(new Theme("테마1", "설명1", "썸네일1"));
        sut.save(new Theme("테마2", "설명2", "썸네일2"));

        // when
        var founds = sut.findAll();

        // then
        assertThat(founds).hasSize(2);
    }

    @DisplayName("인기 있는 테마 10개를 조회한다")
    @Test
    void findAllPopular() {
        // given
        var theme1 = sut.save(new Theme("이름1", "설명1", "썸네일1"));
        var theme2 = sut.save(new Theme("이름2", "설명2", "썸네일2"));

        reservationRepository.save(new Reservation(savedMember, LocalDate.now().minusDays(3), savedTime, theme1));
        reservationRepository.save(new Reservation(savedMember, LocalDate.now().minusDays(4), savedTime, theme1));
        reservationRepository.save(new Reservation(savedMember, LocalDate.now().minusDays(3), savedTime, theme2));

        // when
        var popularThemes = sut.findAllPopular();

        // then
        assertSoftly(soft -> {
            soft.assertThat(popularThemes.get(0).name()).isEqualTo("이름1");
            soft.assertThat(popularThemes.get(1).name()).isEqualTo("이름2");
        });
    }

    @DisplayName("id에 알맞은 테마를 삭제한다")
    @Test
    void deleteById() {
        // given
        var theme = sut.save(new Theme("테마1", "설명1", "썸네일1"));

        // when
        sut.deleteById(theme.getId());
        var founds = sut.findById(theme.getId());

        // then
        assertThat(founds).isEmpty();
    }

    @DisplayName("존재하지 않는 테마를 삭제할 때 예외 처리")
    @Test
    void deleteById_not_found() {
        // when & then
        assertThatThrownBy(() -> sut.deleteById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("삭제할 테마가 없습니다. id=999");
    }

    @DisplayName("id에 알맞은 테마를 가져온다")
    @Test
    void findById() {
        // given
        var theme = sut.save(new Theme("숨겨진 방", "설명", "썸네일"));

        // when
        var found = sut.findById(theme.getId()).get();

        // then
        assertThat(found).isEqualTo(theme);
    }
}
