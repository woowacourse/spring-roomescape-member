package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.LoginInfo;
import roomescape.dto.request.UserReservationRequest;
import roomescape.error.NotFoundException;
import roomescape.error.ReservationException;
import roomescape.stub.StubMemberRepository;
import roomescape.stub.StubReservationRepository;
import roomescape.stub.StubReservationTimeRepository;
import roomescape.stub.StubThemeRepository;

class ReservationServiceTest {

    private final ReservationTime savedTime = new ReservationTime(1L, LocalTime.of(14, 0));
    private final Theme savedTheme = new Theme(1L, "테마1", "설명1", "썸네일1");
    private final Member savedMember = new Member(1L, "홍길동", "hong@example.com", "pw123", Role.USER);
    private final LoginInfo loginInfo = new LoginInfo(savedMember.getId(), savedMember.getName(), savedMember.getRole());

    private final StubReservationRepository reservationRepository = new StubReservationRepository();
    private final StubReservationTimeRepository reservationTimeRepository = new StubReservationTimeRepository(savedTime);
    private final StubThemeRepository themeRepository = new StubThemeRepository(savedTheme);
    private final StubMemberRepository memberRepository = new StubMemberRepository(savedMember);

    private final ReservationService sut = new ReservationService(
            reservationRepository, memberRepository, reservationTimeRepository, themeRepository);

    @DisplayName("모든_예약을_조회한다")
    @Test
    void findAllReservation() {
        // given
        reservationRepository.save(new Reservation(1L, savedMember, LocalDate.of(2025, 5, 11), savedTime, savedTheme));

        // when
        var reservations = sut.findAllReservation();

        // then
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("새로운_예약은_정상_생성된다")
    @Test
    void saveReservation() {
        // given
        var request = new UserReservationRequest(LocalDate.of(2999, 4, 21), savedTime.getId(), savedTheme.getId());

        // when
        var result = sut.saveReservation(request, loginInfo);

        // then
        assertSoftly(soft -> {
            soft.assertThat(result.id()).isNotNull();
            soft.assertThat(result.member().id()).isEqualTo(savedMember.getId());
            soft.assertThat(result.date()).isEqualTo(LocalDate.of(2999, 4, 21));
            soft.assertThat(result.time().id()).isEqualTo(savedTime.getId());
            soft.assertThat(result.theme().id()).isEqualTo(savedTheme.getId());
        });
    }

    @DisplayName("중복된_날짜와_시간이면_예외가_발생한다")
    @Test
    void saveReservation_duplicatedDateTime() {
        // given
        var existingReservation = reservationRepository.save(
                new Reservation(1L, savedMember, LocalDate.of(2025, 5, 11), savedTime, savedTheme));
        var request = new UserReservationRequest(existingReservation.getDate(), savedTime.getId(), savedTheme.getId());

        // when // then
        assertThatThrownBy(() -> sut.saveReservation(request, loginInfo))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 시간은 이미 예약되어 있습니다.");
    }

    @DisplayName("지나간_날짜와_시간이면_예외가_발생한다")
    @Test
    void saveReservation_dateIsPast() {
        // given
        var request = new UserReservationRequest(LocalDate.now().minusYears(1), savedTime.getId(), savedTheme.getId());

        // when // then
        assertThatThrownBy(() -> sut.saveReservation(request, loginInfo))
                .isInstanceOf(ReservationException.class)
                .hasMessage("예약은 현재 시간 이후로 가능합니다.");
    }

    @DisplayName("예약을_삭제한다")
    @Test
    void deleteReservation() {
        // when
        var existingReservation = reservationRepository.save(
                new Reservation(1L, savedMember, LocalDate.of(2025, 5, 11), savedTime, savedTheme));
        sut.deleteReservation(existingReservation.getId());

        // then
        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @DisplayName("존재하지_않는_예약을_삭제하면_예외가_발생한다")
    @Test
    void deleteReservation_reservationNotFound() {
        // when // then
        assertThatThrownBy(() -> sut.deleteReservation(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }
}
