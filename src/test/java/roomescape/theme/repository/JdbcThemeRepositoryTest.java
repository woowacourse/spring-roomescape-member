package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.fixture.Fixture;
import roomescape.member.repositoy.JdbcMemberRepository;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.theme.model.Theme;

@JdbcTest
@Sql("/delete-data.sql")
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private JdbcThemeRepository themeRepository;
    private JdbcReservationTimeRepository reservationTimeRepository;
    private JdbcReservationRepository reservationRepository;
    private JdbcMemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new JdbcThemeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        reservationRepository = new JdbcReservationRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        memberRepository = new JdbcMemberRepository(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("Theme 저장한 후 저장한 테마 값을 반환한다.")
    void save() {
        Theme theme = new Theme(null, "마크", "노력함", "https://asd.cmom");

        assertThat(themeRepository.save(theme))
                .isEqualTo(new Theme(1L, "마크", "노력함", "https://asd.cmom"));
    }

    @Test
    @DisplayName("Theme 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);

        assertThat(themeRepository.findAll())
                .containsExactly(
                        Fixture.THEME_1,
                        Fixture.THEME_2);
    }

    @Test
    @DisplayName("Theme 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        themeRepository.save(Fixture.THEME_1);

        assertThat(themeRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.THEME_1));
    }

    @Test
    @DisplayName("Theme 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(themeRepository.findById(99999L))
                .isNotPresent();
    }

    @Test
    @DisplayName("Theme 테이블에서 많이 예약된 테마 10개를 내림차순으로 가져온다.")
    void findOrderByReservation() {
        memberRepository.save(Fixture.MEMBER_1);
        memberRepository.save(Fixture.MEMBER_2);
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservationRepository.save(Fixture.RESERVATION_1);
        reservationRepository.save(Fixture.RESERVATION_2);

        assertThat(themeRepository.findOrderByReservation())
                .containsExactlyInAnyOrderElementsOf(List.of(
                        Fixture.THEME_2,
                        Fixture.THEME_1));
    }

    @Test
    @DisplayName("예약되지 않은 테마는 인기 테마로 가져오지 않는다.")
    void findOrderByReservation2() {
        memberRepository.save(Fixture.MEMBER_1);
        memberRepository.save(Fixture.MEMBER_2);
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);
        themeRepository.save(new Theme(null, "예약되지 않은 테마", "테마 설명", "https://asd.cmom"));
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservationRepository.save(Fixture.RESERVATION_1);
        reservationRepository.save(Fixture.RESERVATION_2);

        List<Theme> orderByReservation = themeRepository.findOrderByReservation();

        assertThat(orderByReservation)
                .containsExactlyInAnyOrderElementsOf(List.of(
                        Fixture.THEME_2,
                        Fixture.THEME_1));
    }

    @Test
    @DisplayName("주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        themeRepository.deleteById(3L);

        assertThat(themeRepository.findById(3L))
                .isNotPresent();
    }
}
