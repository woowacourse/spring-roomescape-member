package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.MemberRequest;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ThemeRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    ThemeService themeService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("테마를 저장할 수 있다.")
    void save() {
        final Theme theme = themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        assertThat(theme).isNotNull();
    }

    @Test
    @DisplayName("전체 테마를 조회할 수 있다.")
    void findAll() {
        themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        final List<Theme> theme = themeService.findAll();

        assertThat(theme).hasSize(1);
    }

    @Test
    @DisplayName("테마를 삭제할 수 있다.")
    void delete() {
        themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        themeService.delete(1L);
        final List<Theme> theme = themeService.findAll();

        assertThat(theme).hasSize(0);
    }

    @Test
    @DisplayName("이미 예약된 테마를 삭제하려 하면 예외가 발생한다.")
    void invalidDelete() {
        ReservationTime reservationTime = reservationTimeService.save(new ReservationTimeRequest(LocalTime.now().plusHours(1)));
        Theme savedTheme = themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        Member member = memberService.join(new MemberRequest("email@email.com", "1234", "뽀로로"));
        reservationService.save(new ReservationRequest(LocalDate.now().plusDays(1), reservationTime.getId(), savedTheme.getId(), member.getId()));

        assertThatThrownBy(() -> themeService.delete(savedTheme.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("해당 테마에 대한 예약이 존재하여 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("많이 예약한 순으로 테마 10개를 조회한다. 만약 예약 수가 같다면 id 순서로 정렬한다.")
    @Sql("/testdata.sql")
    void findTopRanking() {
        List<Theme> topRanking = themeService.findTopRanking();

        assertAll(
                () -> assertThat(topRanking.get(0).getName()).isEqualTo("테마 2"),
                () -> assertThat(topRanking.get(1).getName()).isEqualTo("테마 1"),
                () -> assertThat(topRanking.get(2).getName()).isEqualTo("테마 3"),
                () -> assertThat(topRanking.get(3).getName()).isEqualTo("테마 4"),
                () -> assertThat(topRanking.get(4).getName()).isEqualTo("테마 5"),
                () -> assertThat(topRanking.get(5).getName()).isEqualTo("테마 6"),
                () -> assertThat(topRanking.get(6).getName()).isEqualTo("테마 7"),
                () -> assertThat(topRanking.get(7).getName()).isEqualTo("테마 8"),
                () -> assertThat(topRanking.get(8).getName()).isEqualTo("테마 9"),
                () -> assertThat(topRanking.get(9).getName()).isEqualTo("테마 10")
        );
    }
}
