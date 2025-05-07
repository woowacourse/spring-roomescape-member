package roomescape.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.validate.InvalidInputException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.domain.ReserverName;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeThumbnail;
import roomescape.time.domain.ReservationTime;

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
                .hasMessage("식별자가 할당되지 않았습니다.");
    }

    @Test
    @DisplayName("unassigned된 DomainId끼리 비교할 수 없다")
    void unassignedDomainIdsAreNotEqual() {
        // given
        final DomainId id1 = ReservationId.unassigned();
        final DomainId id2 = ReservationId.unassigned();

        // when
        // then
        assertThat(id1.equals(id2)).isFalse();
    }

    @Test
    @DisplayName("DomainId는 null로 설정할 수 없다")
    void cannotAssignNullToDomainId() {
        // given
        // when
        // then
        assertThatThrownBy(() -> ReservationId.from(null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Validation failed [while checking null]: DomainId.value");
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

    @Test
    @DisplayName("엔티티를 생성할 때, withId로 생성했다면 assigned된 값을 줘야 한다")
    void requireAssignedDomainId() {
        // given
        final ThemeId unassignedId = ThemeId.unassigned();
        final ThemeName name = ThemeName.from("테마 이름");
        final ThemeDescription description = ThemeDescription.from("재미있는 테마");
        final ThemeThumbnail thumbnail = ThemeThumbnail.from("https://example.com/image.png");

        // when
        // then
        assertThatThrownBy(() -> Theme.withId(unassignedId, name, description, thumbnail))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("식별자가 할당되지 않았습니다");
    }
}
