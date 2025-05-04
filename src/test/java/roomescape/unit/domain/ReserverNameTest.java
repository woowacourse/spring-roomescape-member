package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.ReserverName;

public class ReserverNameTest {

    @Test
    void 예약자_이름은_null일_수_없다() {
        assertThatThrownBy(() -> new ReserverName(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 예약자_이름은_공백일_수_없다() {
        assertThatThrownBy(() -> new ReserverName(""))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 예약자_이름은_5자를_초과할_수_없다() {
        String longName = "너무긴이름1";
        assertThatThrownBy(() -> new ReserverName(longName))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 정상적으로_생성된다() {
        ReserverName name = new ReserverName("길동");
        assertThat(name.getName()).isEqualTo("길동");
    }
}
