package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테마 도메인 테스트")
class ThemeTest {
    @DisplayName("동일한 id는 같은 테마다.")
    @Test
    void equals() {
        //given
        long id1 = 1;
        String name1 = "name1";
        String description1 = "description1";
        String thumbnail1 = "thumbnail1";

        String name2 = "name2";
        String description2 = "description2";
        String thumbnail2 = "thumbnail2";

        //when
        Theme theme1 = new Theme(id1, name1, description1, thumbnail1);
        Theme theme2 = new Theme(id1, name2, description2, thumbnail2);

        //then
        assertThat(theme1).isEqualTo(theme2);
    }
}
