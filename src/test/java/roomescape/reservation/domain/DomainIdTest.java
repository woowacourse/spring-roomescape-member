package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.domain.DomainId;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeThumbnail;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DomainIdTest {

    @Test
    @DisplayName("unassigned된 DomainId에서 getValue를 호출하면 예외가 발생한다")
    void getValueWhenUnassignedThrowsException() {
        // given
        final DomainId id = ReservationId.unassigned();

        // when
        // then
        assertThatThrownBy(id::getValue)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("저장되지 않아서 식별할 수 없습니다.");
    }

    @Test
    @DisplayName("unassigned된 DomainId끼리 비교할 수 없다")
    void unassignedDomainIdsAreNotEqual() {
        // given
        final DomainId id1 = ReservationId.unassigned();
        final DomainId id2 = ReservationId.unassigned();

        // when
        // then
        assertThatThrownBy(() -> id1.equals(id2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("저장되지 않아서 식별할 수 없습니다.");
    }

    @Test
    @DisplayName("DomainId는 null로 설정할 수 없다")
    void cannotAssignNullToDomainId() {
        // given
        // when
        // then
        assertThatThrownBy(() -> ReservationId.from(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("DomainId.value 은(는) null일 수 없습니다.");
    }

    @Test
    @DisplayName("DomainId의 값이 같은 두 객체는 같은 객체로 취급한다")
    void sameIdSameObject() {
        // given
        final Long sameIdValue = 1L;
        final ReservationId sameId1 = ReservationId.from(sameIdValue);
        final ReservationId sameId2 = ReservationId.from(sameIdValue);

        final Theme theme = Theme.withoutId(ThemeName.from("공포"),
                        ThemeDescription.from("지구별 방탈출 최고"),
                        ThemeThumbnail.from("www.making.com"));


        final Reservation sameReservation1 = Reservation.withId(
                sameId1,
                ReserverName.from("일번입니다"),
                ReservationDate.from(LocalDate.now()),
                ReservationTime.withoutId(LocalTime.now()),
                theme);

        final Reservation sameReservation2 = Reservation.withId(
                sameId2,
                ReserverName.from("이번입니다 위와 다릅니다"),
                ReservationDate.from(LocalDate.now()),
                ReservationTime.withoutId(LocalTime.now()),
                theme);

        // when
        // then
        assertThat(sameReservation1).isEqualTo(sameReservation2);
    }
}
