package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.*;
import roomescape.model.*;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("예약 시간 목록 조회")
    @Test
    void getTimes() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        final List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.getTimes();

        assertThat(reservationTimeResponses).hasSize(2)
                .containsExactly(
                        new ReservationTimeResponse(reservationTimeRepository.findById(1L).get()),
                        new ReservationTimeResponse(reservationTimeRepository.findById(2L).get())
                );
    }

    @DisplayName("중복된 시간 저장 시 예외 발생")
    @Test
    void saveExistTime() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));

        assertThatCode(() ->
                reservationTimeService.saveTime(new ReservationTimeSaveRequest(LocalTime.parse("09:00")))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("이미 저장된 예약 시간입니다.");
    }

    @DisplayName("예약 시간 저장")
    @Test
    void saveTime() {
        final ReservationTimeSaveRequest reservationTimeSaveRequest = new ReservationTimeSaveRequest(LocalTime.parse("13:00"));
        final ReservationTimeResponse reservationTimeResponse = reservationTimeService.saveTime(reservationTimeSaveRequest);

        assertThat(reservationTimeResponse).isEqualTo(new ReservationTimeResponse(reservationTimeRepository.findById(reservationTimeResponse.id()).get()));
    }

    @DisplayName("예약 시간 삭제")
    @Test
    void deleteTime() {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        reservationTimeService.deleteTime(reservationTime.getId());

        assertThat(reservationTimeRepository.findById(reservationTime.getId())).isEmpty();
    }

    @DisplayName("존재하지 않는 예약 시간 삭제 시 예외 발생")
    @Test
    void deleteTimeNotFound() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @DisplayName("예약이 존재하는 시간 삭제 시 예외 발생")
    @Test
    void deleteTimeExistReservation() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름", "설명", "썸네일"));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusMonths(1), reservationTime, theme));

        assertThatThrownBy(() -> reservationTimeService.deleteTime(reservationTime.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약이 존재하는 시간은 삭제할 수 없습니다.");
    }

    @DisplayName("특정 날짜의 테마에 대한 전체 시간 예약 여부 오름차순 조회")
    @Test
    void getTimesWithBooked() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final List<ReservationTime> reservationTimes = Stream.of("10:00", "09:00", "11:00")
                .map((time) -> reservationTimeRepository.save(new ReservationTime(LocalTime.parse(time))))
                .toList();
        final Theme theme = themeRepository.save(new Theme("이름", "설명", "썸네일"));
        final LocalDate localDate = LocalDate.now().plusWeeks(1);
        reservationRepository.save(new Reservation(member, localDate, reservationTimes.get(0), theme));
        reservationRepository.save(new Reservation(member, localDate, reservationTimes.get(1), theme));
        final List<ReservationTimeBookedResponse> reservationTimeBookedResponses = reservationTimeService.getTimesWithBooked(
                new ReservationTimeBookedRequest(localDate, theme.getId()));

        assertThat(reservationTimeBookedResponses)
                .hasSize(3)
                .containsExactly(
                        new ReservationTimeBookedResponse(reservationTimes.get(1), true),
                        new ReservationTimeBookedResponse(reservationTimes.get(0), true),
                        new ReservationTimeBookedResponse(reservationTimes.get(2), false)
                );
    }
}

