package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationThemeRequest;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.SignUpRequest;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DuplicateReservationException;
import roomescape.exception.exception.PastReservationTimeException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class ReservationServiceTest {

    @Autowired
    ReservationService service;
    @Autowired
    MemberService memberService;
    @Autowired
    ReservationTimeService timeService;
    @Autowired
    ReservationThemeService themeService;

    @Test
    void addReservationWithNotExistedTimeId() {
        // given
        long notExistedTimeId = 999L;
        memberService.signUpMember(new SignUpRequest("제프리", "jeffrey@gmail.com", "1234!@#$"));
        timeService.addReservationTime(new ReservationTimeRequest(LocalTime.parse("10:10")));
        themeService.addReservationTheme(new ReservationThemeRequest("이름", "설명", "썸네일"));
        LocalDate date = LocalDate.now();
        ReservationRequest request = new ReservationRequest(date, 1L, notExistedTimeId);

        // then & when
        assertThatThrownBy(() -> service.addReservation(request, 1L))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약 시간 999번에 해당하는 시간이 없습니다.");
    }

    @Test
    void addReservationWithNotExistedThemeId() {
        // given
        long notExistedThemeId = 999L;
        memberService.signUpMember(new SignUpRequest("제프리", "jeffrey@gmail.com", "1234!@#$"));
        timeService.addReservationTime(new ReservationTimeRequest(LocalTime.parse("10:10")));
        themeService.addReservationTheme(new ReservationThemeRequest("이름", "설명", "썸네일"));
        LocalDate date = LocalDate.now();
        ReservationRequest request = new ReservationRequest(date, notExistedThemeId, 1L);

        // then & when
        assertThatThrownBy(() -> service.addReservation(request, 1L))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약 테마 999번에 해당하는 테마가 없습니다.");
    }

    @Test
    void addReservationWithNotExistedMemberId() {
        // given
        long notExistedMemberId = 999L;
        memberService.signUpMember(new SignUpRequest("제프리", "jeffrey@gmail.com", "1234!@#$"));
        timeService.addReservationTime(new ReservationTimeRequest(LocalTime.parse("10:10")));
        themeService.addReservationTheme(new ReservationThemeRequest("이름", "설명", "썸네일"));
        LocalDate date = LocalDate.now();
        ReservationRequest request = new ReservationRequest(date, 1L, 1L);

        // then & when
        assertThatThrownBy(() -> service.addReservation(request, notExistedMemberId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 999번에 해당하는 멤버가 없습니다.");
    }

    @DisplayName("현재 시점 이전의 예약을 생성할 시 예외를 던진다")
    @Test
    void addReservationBeforeCurrentDateTime() {
        // given
        memberService.signUpMember(new SignUpRequest("제프리", "jeffrey@gmail.com", "1234!@#$"));
        timeService.addReservationTime(new ReservationTimeRequest(LocalTime.parse("10:10")));
        themeService.addReservationTheme(new ReservationThemeRequest("이름", "설명", "썸네일"));
        LocalDate date = LocalDate.now().minusDays(1);
        ReservationRequest request = new ReservationRequest(date, 1L, 1L);

        // then & when
        assertThatThrownBy(() -> service.addReservation(request, 1L))
                .isInstanceOf(PastReservationTimeException.class)
                .hasMessage("[ERROR] 현재 시각 이후로 예약해 주세요.");
    }

    @DisplayName("같은 날짜 및 시간 예약이 존재하면 예외를 던진다")
    @Test
    void addReservationWithDuplicatedReservation() {
        //given
        memberService.signUpMember(new SignUpRequest("제프리", "jeffrey@gmail.com", "1234!@#$"));
        timeService.addReservationTime(new ReservationTimeRequest(LocalTime.parse("10:10")));
        themeService.addReservationTheme(new ReservationThemeRequest("이름", "설명", "썸네일"));
        LocalDate date = LocalDate.now().plusDays(1);

        ReservationRequest request = new ReservationRequest(date, 1L, 1L);

        service.addReservation(request, 1L);
        ReservationRequest duplicated = new ReservationRequest(date, 1L, 1L);

        //when & then
        assertThatThrownBy(() -> service.addReservation(duplicated, 1L))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage("[ERROR] 이미 존재하는 예약입니다. 다른 시간을 선택해 주세요.");
    }

    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 예외를 던진다")
    @Test
    void removeReservation() {
        //given
        long notExistId = 999;

        //when & then
        assertThatThrownBy(() -> service.removeReservation(notExistId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약번호 999번에 해당하는 예약이 없습니다.");
    }

}
