package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.global.exception.exceptions.ExistingEntryException;
import roomescape.global.exception.exceptions.ReferencedRowExistsException;
import roomescape.member.domain.MemberRepository;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.ReservationTimeRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeJdbcRepositoryTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("중복된 테마 추가가 불가능한 지 확인한다.")
    void checkDuplicatedTheme() {
        //given
        Theme theme1 = new Theme("테마명", "테마 설명1", "테마 이미지1");
        Theme theme2 = new Theme("테마명", "테마 설명2", "테마 이미지2");
        themeRepository.save(theme1);

        //when & then
        assertThatThrownBy(() ->
                themeRepository.save(theme2)
        ).isInstanceOf(ExistingEntryException.class)
                .hasMessage("이미 동일한 이름을 가진 테마가 존재합니다.");
    }

    @Test
    @DisplayName("삭제하려는 테마에 예약이 존재한다면 삭제가 불가능한지 확인한다.")
    void checkDeleteReservedTheme() {
        //given
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(1L);
        themeRepository.save(new Theme("테마명", "테마 설명", "테마 이미지"));
        Theme theme = themeRepository.findByThemeId(1L);
        Member member = memberRepository.findByMemberId(1L);
        reservationRepository.save(new Reservation(
                LocalDate.parse("2025-10-05"),
                reservationTime,
                theme,
                member
        ));

        //when & then
        assertThatThrownBy(() ->
                themeRepository.deleteById(1L)
        ).isInstanceOf(ReferencedRowExistsException.class)
                .hasMessage("현 테마에 예약이 존재합니다.");
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("주간 인기 테마 목록이 10개인지 확인한다.")
    void checkWeeklyHotThemesSize() {
        //given
        LocalDate start = LocalDate.parse("2024-05-12");
        LocalDate end = LocalDate.parse("2024-05-19");
        Integer page = 10;
        Integer size = 0;

        //when
        List<Theme> themes = themeRepository.findHotThemesByDurationAndCount(start, end, page, size);

        //then
        assertThat(themes.size()).isEqualTo(10);
    }
}
