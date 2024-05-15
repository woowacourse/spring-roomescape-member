package roomescape.infrastructure.persistence.dynamic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationQueryConditionsTest {

    @Test
    @DisplayName("조건의 수가 1개 이상이라면, WHERE 를 추가한다.")
    void addWhere() {
        ReservationQueryConditions build = ReservationQueryConditions.builder()
                .themeId(2L)
                .build();

        String expected = "SELECT * FROM base WHERE theme_id = ?";
        String result = build.createDynamicQuery("SELECT * FROM base");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("마지막 AND 는 제거한다.")
    void removeLastAnd() {
        ReservationQueryConditions build = ReservationQueryConditions.builder()
                .themeId(2L)
                .memberId(1L)
                .period("2023-12-24", "2023-12-25")
                .build();

        String expected
                = "SELECT * FROM base WHERE theme_id = ? AND member_id = ? AND convert(date, DATE) BETWEEN ? AND ?";
        String result = build.createDynamicQuery("SELECT * FROM base");

        assertThat(result).isEqualTo(expected);
    }
}
