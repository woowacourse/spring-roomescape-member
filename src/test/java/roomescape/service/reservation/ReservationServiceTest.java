package roomescape.service.reservation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.Role;
import roomescape.domain.reservation.ReservationSearchFilter;
import roomescape.dto.member.UserReservationRequest;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.exceptions.reservation.ReservationDuplicateException;
import roomescape.infrastructure.member.MemberInfo;

@SpringBootTest
@Transactional
@Sql({"/fixtures/schema-test.sql", "/fixtures/data-test.sql"})
class ReservationServiceTest {

    private static final ReservationSearchFilter RESERVATION_SEARCH_FILTER = new ReservationSearchFilter(null, null,
            null, null);
    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("조회된 엔티티를 DTO로 매핑해 반환한다.")
    void readReservation() {
        //given & when
        List<ReservationResponse> actual = reservationService.readReservation(RESERVATION_SEARCH_FILTER);
        //then
        assertThat(actual.size()).isEqualTo(56);
        assertThat(actual.getFirst().id()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 생성 시, 저장한 엔티티를 DTO로 반환한다.")
    void postReservation() {
        //given
        List<ReservationResponse> given = reservationService.readReservation(RESERVATION_SEARCH_FILTER);
        assertThat(given.size()).isEqualTo(56);
        LocalDate givenDate = LocalDate.of(2028, 1, 10);
        long timeId = 1L;
        long themeId = 1L;
        long memberId = 1L;
        //when
        ReservationRequest request = new ReservationRequest(givenDate, timeId, themeId, memberId);
        ReservationResponse actual = reservationService.postReservation(request);
        //then
        assertThat(actual.id()).isEqualTo(57);
    }

    @Test
    @DisplayName("사용자가 예약을 생성할 시, 저장한 엔티티를 DTO로 반환한다.")
    void postReservationWithMemberInfo() {
        //given
        List<ReservationResponse> given = reservationService.readReservation(RESERVATION_SEARCH_FILTER);
        assertThat(given.size()).isEqualTo(56);
        LocalDate givenDate = LocalDate.of(2028, 1, 10);
        long timeId = 1L;
        long themeId = 1L;
        MemberInfo memberInfo = new MemberInfo(1L, "", "", Role.USER);
        //when
        UserReservationRequest request = new UserReservationRequest(givenDate, timeId, themeId);
        ReservationResponse actual = reservationService.postReservation(request, memberInfo);
        //then
        assertThat(actual.id()).isEqualTo(57);
    }

    @Test
    @DisplayName("예약 생성 시, 저장할 날짜가 과거일 경우 예외를 발생한다.")
    void postReservationIfBeforeDate() {
        //given
        LocalDate givenDate = LocalDate.MIN;
        long timeId = 1L;
        long themeId = 1L;
        long memberId = 1L;

        ReservationRequest request = new ReservationRequest(givenDate, timeId, themeId, memberId);

        //when&then
        assertThatThrownBy(() -> reservationService.postReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약이 불가능한 시간");
    }

    @Test
    @DisplayName("예약 생성 시, 날짜, 시간, 테마가 중복될 경우 예외가 발생한다.")
    void postReservationIfDuplication() {
        //given
        LocalDate daysAgo = LocalDate.now().plusDays(10);
        long timeId = 1L;
        long themeId = 10L;
        long memberId = 1L;
        ReservationRequest request = new ReservationRequest(daysAgo, timeId, themeId, memberId);

        //when&then
        assertThatThrownBy(() -> reservationService.postReservation(request))
                .isInstanceOf(ReservationDuplicateException.class)
                .hasMessageContaining("해당 시각의 중복된 예약이 존재합니다");
    }

    @Test
    @DisplayName("저장소에 없는 값을 삭제하려할 경우, 예외가 발생한다.")
    void deleteReservation() {
        assertThatThrownBy(() -> reservationService.deleteReservation(Long.MAX_VALUE))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
