package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.Member;
import roomescape.business.MemberRole;
import roomescape.business.Reservation;
import roomescape.business.ReservationTime;
import roomescape.persistence.FakeMemberRepository;
import roomescape.persistence.FakeReservationRepository;
import roomescape.persistence.FakeReservationTimeRepository;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.dto.request.ReservationTimeRequestDto;
import roomescape.presentation.dto.response.ReservationTimeResponseDto;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        memberRepository = new FakeMemberRepository();
        reservationTimeService = new ReservationTimeService(
                reservationRepository,
                reservationTimeRepository
        );
    }

    @DisplayName("아이디로 예약 가능한 시간을 조회한다")
    @Test
    void readTimeOne() {
        // given
        LocalTime time = LocalTime.now(ZoneId.of("Asia/Seoul"));
        Long timeId = reservationTimeRepository.add(new ReservationTime(time));
        ReservationTimeResponseDto expected = new ReservationTimeResponseDto(timeId, time);

        // when
        ReservationTimeResponseDto actual = reservationTimeService.readTimeOne(timeId);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @DisplayName("예약 가능한 시간을 추가한다")
    @Test
    void createTime() {
        // given
        LocalTime time = LocalTime.now(ZoneId.of("Asia/Seoul"));
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(time);

        // when
        Long timeId = reservationTimeService.createTime(reservationTimeRequestDto);

        // then
        ReservationTime saved = reservationTimeRepository.findById(timeId).get();
        assertAll(
                () -> assertThat(saved.getId())
                        .isEqualTo(timeId),
                () -> assertThat(saved.getStartAt())
                        .isEqualTo(time)
        );
    }

    @DisplayName("예약 시간대 하나를 삭제한다")
    @Test
    void deleteTime() {
        // given
        LocalTime time = LocalTime.now(ZoneId.of("Asia/Seoul"));
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(time);
        Long timeId = reservationTimeService.createTime(reservationTimeRequestDto);

        // when
        reservationTimeService.deleteTime(timeId);

        // then
        assertThat(reservationTimeRepository.findById(timeId))
                .isNotPresent();
    }

    @DisplayName("예약이 참조하는 시간대 하나를 삭제한다")
    @Test
    void deleteReferencedTime() {
        // given
        LocalDateTime tomorrow = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                .plusDays(1);
        LocalDate date = tomorrow.toLocalDate();
        LocalTime time = tomorrow.toLocalTime();
        Member member = new Member("사용자", "user@user.com", "1234", MemberRole.ADMIN);
        Long timeId = reservationTimeRepository.add(new ReservationTime(time));
        Long memberId = memberRepository.add(member);
        reservationRepository.add(new Reservation(
                new Member(
                        memberId,
                        member.getName(),
                        member.getEmail(),
                        member.getPassword(),
                        member.getRole()
                ), date,
                new ReservationTime(
                        timeId,
                        time
                ),
                null)
        );

        // when
        // then
        assertThatCode(() -> reservationTimeService.deleteTime(timeId))
                .isInstanceOf(ReservationTimeException.class)
                .hasMessage("해당 시간의 예약이 존재하여 시간을 삭제할 수 없습니다.");
    }
}
