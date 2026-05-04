package roomescape.domain.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NameTest {

    @Test
    void 같은_이름_동일_객체_테스트() {
        //given
        Name name1 = new Name("제임스");
        Name name2 = new Name("제임스");

        // when, then
        assertThat(name1).isEqualTo(name2);
    }

    @Test
    void 다른_이름_다른_객체_테스트() {
        //given
        Name name1 = new Name("제임스");
        Name name2 = new Name("포비");

        // when, then
        assertThat(name1).isNotEqualTo(name2);
    }
}