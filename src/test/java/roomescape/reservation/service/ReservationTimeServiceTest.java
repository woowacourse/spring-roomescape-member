package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.error.ConflictException;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;
import roomescape.reservation.controller.dto.ReservationTimeRequest;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.repository.ThemeRepository;
import roomescape.reservation.repository.fake.MemberFakeRepository;
import roomescape.reservation.repository.fake.ReservationFakeRepository;
import roomescape.reservation.repository.fake.ReservationTimeFakeRepository;
import roomescape.reservation.repository.fake.ThemeFakeRepository;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setup() {
        ReservationRepository reservationRepository = new ReservationFakeRepository();
        reservationTimeRepository = new ReservationTimeFakeRepository();
        ThemeRepository themeRepository = new ThemeFakeRepository(reservationRepository);
        MemberFakeRepository memberRepository = new MemberFakeRepository();

        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(3, 12)));
        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(11, 33)));
        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(16, 54)));
        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(23, 53)));

        themeRepository.saveAndReturnId(new Theme(null, "레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        themeRepository.saveAndReturnId(new Theme(null, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        themeRepository.saveAndReturnId(new Theme(null, "레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        memberRepository.save(new Member(null, "루키", "rookie123@woowa.com", "rookierookie123", Role.USER));
        memberRepository.save(new Member(null, "하루", "haru123@woowa.com", "haruharu123", Role.USER));
        memberRepository.save(new Member(null, "베루스", "verus@woowa.com", "verusverus123", Role.ADMIN));

        reservationRepository.saveAndReturnId(
                new Reservation(null, LocalDate.now().minusDays(3), reservationTimeRepository.findById(1L).get(),
                        themeRepository.findById(1L).get(), memberRepository.findById(1L).get()));
        reservationRepository.saveAndReturnId(
                new Reservation(null, LocalDate.now().minusDays(1), reservationTimeRepository.findById(2L).get(),
                        themeRepository.findById(2L).get(), memberRepository.findById(2L).get()));
        reservationRepository.saveAndReturnId(
                new Reservation(null, LocalDate.now().plusDays(3), reservationTimeRepository.findById(3L).get(),
                        themeRepository.findById(3L).get(), memberRepository.findById(3L).get()));

        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @DisplayName("예약 시간 정보를 추가한다")
    @Test
    void add_test() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(17, 25));

        // when
        reservationTimeService.add(request);

        // then
        ReservationTime savedTime = reservationTimeRepository.findById(5L).get();
        assertAll(
                () -> assertThat(savedTime.getId()).isEqualTo(5L),
                () -> assertThat(savedTime.getStartAt()).isEqualTo(LocalTime.of(17, 25))
        );
    }

    @DisplayName("예약 시간 정보를 삭제한다")
    @Test
    void remove_test() {
        // given
        Long removeId = 3L;

        // when
        reservationTimeRepository.deleteById(removeId);

        // then
        assertAll(
                () -> assertThat(reservationTimeRepository.findAll()).hasSize(3),
                () -> assertThat(reservationTimeRepository.findById(removeId).isEmpty()).isTrue()
        );

    }

    @DisplayName("예약 정보 시간을 모두 조회한다")
    @Test
    void get_times_test() {
        // when
        List<ReservationTimeResponse> reservationTimeResponse = reservationTimeService.getTimes();

        // then
        assertAll(
                () -> assertThat(reservationTimeResponse).hasSize(4),
                () -> assertThat(reservationTimeResponse).extracting(ReservationTimeResponse::startAt)
                        .containsExactlyInAnyOrder(
                                LocalTime.of(3, 12),
                                LocalTime.of(11, 33),
                                LocalTime.of(16, 54),
                                LocalTime.of(23, 53)
                        )
        );

    }

    @DisplayName("특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하면 예외가 발생한다")
    @Test
    void delete_exception() {
        // given
        Long deleteId = 1L;

        // when & then
        assertThatThrownBy(() -> reservationTimeService.remove(deleteId))
                .isInstanceOf(ConflictException.class)
                .hasMessage("해당 시간과 연관된 예약이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("동일한 시간을 생성하는 경우 예외가 발생한다")
    @Test
    void duplicate_time_test() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(3, 12));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.add(reservationTimeRequest))
                .isInstanceOf(ConflictException.class)
                .hasMessage("해당 시간은 이미 존재합니다.");
    }

}
