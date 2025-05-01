package roomescape.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class IdTest {

    @Test
    void getReservations_shouldReturnAllCreatedReservations() {
        Id id1 = Id.from(1L);
        Id id2 = Id.from(2L);

        assertThat(id1.getValue()).isEqualTo(1L);
        assertThat(id2.getValue()).isEqualTo(2L);
    }

    @Test
    void deleteReservation_shouldThrowException_WhenIdNotFound() {
        Id unassignedId = Id.unassigned();

        assertThatThrownBy(unassignedId::getValue)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("할당되지 않은 ID에 접근할 수 없습니다.");
    }
}
