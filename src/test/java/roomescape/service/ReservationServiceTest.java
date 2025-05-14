package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.controller.dto.request.CreateReservationRequest;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.MemberRoleType;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.RoomThemeRepository;
import roomescape.service.dto.request.ReservationCreation;
import roomescape.service.dto.request.ReservationCriteriaCreation;
import roomescape.service.dto.response.ReservationResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReservationTimeRepository reservationTimeRepository;

    @Mock
    RoomThemeRepository roomThemeRepository;

    @InjectMocks
    ReservationService reservationService;

    @Test
    @DisplayName("예약 정보를 조회한다")
    void saveReservation() {
        //given
        LocalDate date = LocalDate.of(2100, 6, 30);
        Member member = new Member(1L, "test", "test@email.com", "1234", MemberRoleType.MEMBER);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        RoomTheme roomTheme = new RoomTheme(1L, "test", "test", "test");
        when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(member));
        when(reservationTimeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(reservationTime));
        when(roomThemeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(roomTheme));
        when(reservationRepository.findById(any(Long.class)))
                .thenReturn(
                        Optional.of(new Reservation(1L, member, LocalDate.of(2100, 1, 1), reservationTime, roomTheme)));
        when(reservationRepository.insert(any(Reservation.class)))
                .thenReturn(1L);
        when(reservationRepository.existSameReservation(any(LocalDate.class), any(Long.class), any(Long.class)))
                .thenReturn(false);

        //when
        ReservationCreation creation = new ReservationCreation(1L, date, 1L, 1L);
        ReservationResult actual = reservationService.addReservation(creation);

        //then
        assertThat(actual.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("테마, 날짜, 시간이 같은 예약이 존재하면 예외를 던진다")
    void exceptionWhenSameDateTime() {
        //given
        when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Member(1L, "test", "test@email.com", "1234", MemberRoleType.MEMBER)));
        when(reservationTimeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new ReservationTime(1L, LocalTime.of(10, 0))));
        when(roomThemeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new RoomTheme(1L, "test", "test", "test")));
        when(reservationRepository.existSameReservation(any(LocalDate.class), any(Long.class), any(Long.class)))
                .thenReturn(true);

        LocalDate date = LocalDate.of(2100, 1, 1);
        ReservationCreation creation = ReservationCreation.of(
                new LoginMember(1, "test"),
                new CreateReservationRequest(date, 1, 1));

        //when & then
        assertThatThrownBy(() -> reservationService.addReservation(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessageContaining("이미 예약이 존재하는 시간입니다");
    }

    @Test
    @DisplayName("과거 시점으로 예약하는 경우 예외를 던진다")
    void exceptionWhenPastDate() {
        // given
        when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Member(1L, "test", "test@email.com", "1234", MemberRoleType.MEMBER)));
        when(reservationTimeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new ReservationTime(1L, LocalTime.of(10, 0))));
        when(roomThemeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new RoomTheme(1L, "test", "test", "test")));

        LocalDate date = LocalDate.of(2000, 1, 1);

        //when & then
        ReservationCreation past = ReservationCreation.of(
                new LoginMember(1, "test"),
                new CreateReservationRequest(date, 1, 1));

        assertThatThrownBy(() -> reservationService.addReservation(past))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("과거 시점은 예약할 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 timeId인 경우 예외를 던진다")
    void throwExceptionWhenNotExistTimeId() {
        //given
        when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Member(1L, "test", "test@email.com", "1234", MemberRoleType.MEMBER)));
        when(reservationTimeRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        ReservationCreation notValidTimeId = ReservationCreation.of(
                new LoginMember(1, "test"),
                new CreateReservationRequest(LocalDate.of(3000, 1, 1), 1000, 1));

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(notValidTimeId))
                .isInstanceOf(NotFoundValueException.class)
                .hasMessageContaining("존재하지 않는 예약 가능 시간입니다");
    }

    @Test
    @DisplayName("존재하지 않는 themeId 경우 예외를 던진다")
    void throwExceptionWhenNotExistThemeId() {
        //given
        when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Member(1L, "test", "test@email.com", "1234", MemberRoleType.MEMBER)));
        when(reservationTimeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new ReservationTime(1L, LocalTime.of(10, 0))));
        when(roomThemeRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        ReservationCreation notValidThemeId = ReservationCreation.of(
                new LoginMember(1, "test"),
                new CreateReservationRequest(LocalDate.of(3000, 1, 1), 1, 1000));

        //when & then
        assertThatThrownBy(() -> reservationService.addReservation(notValidThemeId))
                .isInstanceOf(NotFoundValueException.class)
                .hasMessageContaining("존재하지 않는 테마 입니다");
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void removeReservationById() {
        //given
        when(reservationRepository.deleteById(any(Long.class)))
                .thenReturn(true);

        // when //then
        assertDoesNotThrow(() -> reservationService.removeReservationById(1L));
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 예외를 던진다")
    void removeNotExistReservationById() {
        //given
        long notExistId = 1000L;
        when(reservationRepository.deleteById(any(Long.class)))
                .thenReturn(false);

        //when & then
        assertThatThrownBy(() -> reservationService.removeReservationById(notExistId))
                .isInstanceOf(NotFoundValueException.class)
                .hasMessageContaining("존재하지 않는 예약입니다");
    }

    private static Stream<Arguments> provideCriteria() {
        return Stream.of(
                Arguments.of(new ReservationCriteriaCreation(1L, 1L,
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1)), 1),
                Arguments.of(new ReservationCriteriaCreation(null, 1L,
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1)), 1),
                Arguments.of(new ReservationCriteriaCreation(1L, null,
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1)), 1),
                Arguments.of(new ReservationCriteriaCreation(1L, 1L, null, LocalDate.of(2025, 1, 1)), 1),
                Arguments.of(new ReservationCriteriaCreation(1L, 1L, LocalDate.of(2025, 1, 1), null), 1),
                Arguments.of(new ReservationCriteriaCreation(null, null, null, null), 1),
                Arguments.of(new ReservationCriteriaCreation(null, null,
                        LocalDate.of(2025, 1, 2), LocalDate.of(2025, 1, 2)), 0),
                Arguments.of(new ReservationCriteriaCreation(2L, 1L,
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1)), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCriteria")
    @DisplayName("필터 기준을 기반으로 예약을 조회한다")
    void findReservationByCriteria(ReservationCriteriaCreation criteriaCreation, int expectedSize) {
        //given
        Member member = new Member(1L, "test", "test@email.com", "1234", MemberRoleType.MEMBER);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        RoomTheme roomTheme = new RoomTheme(1L, "test", "test", "test");
        Reservation reservation = new Reservation(1L, member, LocalDate.of(2025, 1, 1), reservationTime, roomTheme);

        when(reservationRepository.findAllByCriteria(any()))
                .thenReturn(expectedSize > 0 ? List.of(reservation) : List.of());

        //when
        List<ReservationResult> actual = reservationService.getAllReservationByCriteria(criteriaCreation);

        //then
        assertThat(actual).hasSize(expectedSize);
    }

    @Test
    @DisplayName("모든 예약 정보를 조회한다")
    void getAllReservations() {
        //given
        Member member = new Member(1L, "test", "test@email.com", "1234", MemberRoleType.MEMBER);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        RoomTheme roomTheme = new RoomTheme(1L, "test", "test", "test");
        Reservation reservation = new Reservation(1L, member, LocalDate.of(2100, 1, 1), reservationTime, roomTheme);

        when(reservationRepository.findAll())
                .thenReturn(List.of(reservation));

        //when
        List<ReservationResult> actual = reservationService.getAllReservations();

        //then
        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst().id()).isEqualTo(1L);
        assertThat(actual.getFirst().member().name()).isEqualTo("test");
        assertThat(actual.getFirst().date()).isEqualTo(LocalDate.of(2100, 1, 1));
    }
}
