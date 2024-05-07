package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.dto.ThemeResponse;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RankServiceTest {
    private RankService rankService;

    @Autowired
    public RankServiceTest(final RankService rankService) {
        this.rankService = rankService;
    }

    @DisplayName("기간이 주어지면 가장 많이 예약한 테마 목록 순으로 조회 결과가 반환된다.")
    @Test
    void givenStartDateEndDateCount_when_getPopularThemeListAndGetFirst_then_getMostReservedTheme() {
        //given
        LocalDate startDate = LocalDate.parse("2024-04-30");
        LocalDate endDate = LocalDate.parse("2024-05-02");
        Long count = 10L;

        //when
        final List<ThemeResponse> popularThemeList = rankService.getPopularThemeList(startDate, endDate, count);
        assertThat(popularThemeList.get(0).id()).isEqualTo(2L);
    }
}