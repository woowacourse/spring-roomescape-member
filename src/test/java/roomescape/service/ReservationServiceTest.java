package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DuplicateReservationException;
import roomescape.exception.exception.PastReservationTimeException;
import roomescape.repository.MemberRepository;
import roomescape.repository.RoomescapeRepository;
import roomescape.repository.RoomescapeThemeRepository;
import roomescape.repository.RoomescapeTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    RoomescapeRepository roomescapeRepository;
    @Mock
    RoomescapeTimeRepository roomescapeTimeRepository;
    @Mock
    RoomescapeThemeRepository roomescapeThemeRepository;
    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    ReservationService reservationService;

    @Test
    void addReservationWithNotExistedTimeId() {
        // given
        long notExistedTimeId = 999L;
        long memberId = 1L;
        long themeId = 1L;
        LocalDate date = LocalDate.now();
        ReservationRequest request = new ReservationRequest(date, themeId, notExistedTimeId);

        when(memberRepository.findById(memberId)).thenReturn(
                Optional.of(new Member("제프리", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER)));
        when(roomescapeTimeRepository.findById(notExistedTimeId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(request, memberId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약 시간 999번에 해당하는 시간이 없습니다.");
    }

    @Test
    void addReservationWithNotExistedThemeId() {
        // given
        long notExistedThemeId = 999L;
        long memberId = 1L;
        long timeId = 1L;
        LocalDate date = LocalDate.now();
        ReservationRequest request = new ReservationRequest(date, notExistedThemeId, timeId);

        when(memberRepository.findById(memberId)).thenReturn(
                Optional.of(new Member("제프리", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER)));
        when(roomescapeTimeRepository.findById(timeId)).thenReturn(
                Optional.of(new ReservationTime(timeId, LocalTime.parse("10:10"))));
        when(roomescapeThemeRepository.findById(notExistedThemeId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(request, memberId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약 테마 999번에 해당하는 테마가 없습니다.");
    }

    @Test
    void addReservationWithNotExistedMemberId() {
        // given
        long notExistedMemberId = 999L;
        long timeId = 1L;
        long themeId = 1L;
        LocalDate date = LocalDate.now();
        ReservationRequest request = new ReservationRequest(date, themeId, timeId);

        when(memberRepository.findById(notExistedMemberId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(request, notExistedMemberId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 999번에 해당하는 멤버가 없습니다.");
    }

    @DisplayName("현재 시점 이전의 예약을 생성할 시 예외를 던진다")
    @Test
    void addReservationBeforeCurrentDateTime() {
        // given
        long memberId = 1L;
        long timeId = 1L;
        long themeId = 1L;
        LocalDate date = LocalDate.now().minusDays(1);
        ReservationRequest request = new ReservationRequest(date, themeId, timeId);

        Member member = new Member("제프리", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        ReservationTime time = new ReservationTime(timeId, LocalTime.parse("10:10"));
        ReservationTheme theme = new ReservationTheme(themeId, "이름", "설명", "썸네일");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(roomescapeTimeRepository.findById(timeId)).thenReturn(Optional.of(time));
        when(roomescapeThemeRepository.findById(themeId)).thenReturn(Optional.of(theme));

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(request, memberId))
                .isInstanceOf(PastReservationTimeException.class)
                .hasMessage("[ERROR] 현재 시각 이후로 예약해 주세요.");
    }

    @DisplayName("같은 날짜 및 시간 예약이 존재하면 예외를 던진다")
    @Test
    void addReservationWithDuplicatedReservation() {
        // given
        long memberId = 1L;
        long timeId = 1L;
        long themeId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationRequest request = new ReservationRequest(date, themeId, timeId);

        Member member = new Member("제프리", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        ReservationTime time = new ReservationTime(timeId, LocalTime.parse("10:10"));
        ReservationTheme theme = new ReservationTheme(themeId, "이름", "설명", "썸네일");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(roomescapeTimeRepository.findById(timeId)).thenReturn(Optional.of(time));
        when(roomescapeThemeRepository.findById(themeId)).thenReturn(Optional.of(theme));
        when(roomescapeRepository.existsByDateAndTime(date, time)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(request, memberId))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage("[ERROR] 이미 존재하는 예약입니다. 다른 시간을 선택해 주세요.");
    }

    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 예외를 던진다")
    @Test
    void removeReservation() {
        // given
        long notExistId = 999;
        when(roomescapeRepository.deleteById(notExistId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> reservationService.removeReservation(notExistId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약번호 999번에 해당하는 예약이 없습니다.");
    }
}
