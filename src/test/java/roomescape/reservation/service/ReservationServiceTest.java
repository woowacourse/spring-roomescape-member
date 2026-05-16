package roomescape.reservation.service;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnauthorizedActionException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("성공: 모든 정보가 유효하면 예약을 정상적으로 저장한다.")
    void 성공적으로_예약을_저장한다() {
        // given
        String name = "밍구";
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(1L, LocalTime.of(14, 0));
        // Theme 레코드 구조 반영: id, name, description, thumbnail
        Theme theme = new Theme(1L, "우테코 탈출", "레벨 2 미션 테마입니다.", "https://example.com/image.png");
        Reservation reservation = new Reservation(1L, name, date, time, theme);

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(time));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));
        given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);

        // when
        Reservation saved = reservationService.save(name, date, 1L, 1L);

        // then
        assertThat(saved.getName()).isEqualTo(name);
        assertThat(saved.getTheme().name()).isEqualTo("우테코 탈출");
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예외: 존재하지 않는 시간 ID로 예약 시 NotFoundException이 발생한다.")
    void 존재하지_않는_시간으로_예약_시_예외가_발생한다() {
        // given
        given(reservationTimeRepository.findById(99L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.save("밍구", LocalDate.now(), 99L, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("예약 시간을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("예외: 존재하지 않는 테마 ID로 예약 시 NotFoundException이 발생한다.")
    void 존재하지_않는_테마로_예약_시_예외가_발생한다() {
        // given
        given(reservationTimeRepository.findById(1L)).willReturn(
                Optional.of(new ReservationTime(1L, LocalTime.of(10, 0))));
        given(themeRepository.findById(99L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.save("밍구", LocalDate.now(), 1L, 99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("해당 테마를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("예외: 이미 예약된 날짜와 시간인 경우 DuplicateException이 발생한다.")
    void 중복된_날짜와_시간으로_예약_시_예외가_발생한다() {
        // given
        given(reservationTimeRepository.findById(1L)).willReturn(
                Optional.of(new ReservationTime(1L, LocalTime.of(10, 0))));
        given(themeRepository.findById(1L)).willReturn(Optional.of(new Theme(1L, "테마", "설명", "이미지")));
        given(reservationRepository.save(any(Reservation.class))).willThrow(new DuplicateKeyException("중복 예약"));

        // when & then
        assertThatThrownBy(() -> reservationService.save("밍구", LocalDate.now(), 1L, 1L))
                .isInstanceOf(DuplicateException.class)
                .hasMessageContaining("이미 예약되어 있습니다.");
    }

    @Test
    @DisplayName("성공: 관리자는 어떤 예약이든 ID 기반으로 강제 삭제할 수 있다.")
    void 관리자는_예약을_강제로_삭제할_수_있다() {
        // given
        Reservation reservation = new Reservation(1L, "사용자", LocalDate.now(), null, null);
        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        // when
        reservationService.deleteByAdmin(1L);

        // then
        verify(reservationRepository).delete(1L);
    }

    @Test
    @DisplayName("성공: 사용자는 본인 이름이 일치하는 경우 예약을 삭제할 수 있다.")
    void 사용자는_본인의_예약인_경우_삭제할_수_있다() {
        // given
        String userName = "밍구";
        Reservation reservation = new Reservation(1L, userName, LocalDate.now(), null, null);
        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        // when
        reservationService.deleteByUser(1L, userName);

        // then
        verify(reservationRepository).delete(1L);
    }

    @Test
    @DisplayName("예외: 사용자가 타인의 예약을 삭제하려고 하면 UnauthorizedActionException이 발생한다.")
    void 사용자가_타인의_예약을_삭제하려고_하면_예외가_발생한다() {
        // given
        String ownerName = "진짜 주인";
        String intruderName = "가짜 주인";
        Reservation reservation = new Reservation(1L, ownerName, LocalDate.now(), null, null);
        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.deleteByUser(1L, intruderName))
                .isInstanceOf(UnauthorizedActionException.class)
                .hasMessageContaining("예약자 이름이 일치하지 않아");
    }

    @Test
    @DisplayName("예외: 존재하지 않는 예약 ID를 삭제하려고 하면 NotFoundException이 발생한다.")
    void 존재하지_않는_예약을_삭제하려고_하면_예외가_발생한다() {
        // given
        given(reservationRepository.findById(99L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.deleteByAdmin(99L))
                .isInstanceOf(NotFoundException.class);
    }
}
