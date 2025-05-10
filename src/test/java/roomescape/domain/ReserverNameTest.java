package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.exception.ReserverNameEmptyException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ReserverNameTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void 예약자_이름은_비어있거나_null일_수_없다(String name) {
        assertThatThrownBy(() -> new ReserverName(name))
                .isInstanceOf(ReserverNameEmptyException.class);
    }
}
