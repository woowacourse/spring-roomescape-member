package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.constant.TestData.RESERVATION_TIME_COUNT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import roomescape.DataBasedTest;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.CreateReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.DatabaseForeignKeyException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;

class ReservationTimeServiceTest extends DataBasedTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @Autowired
    ReservationThemeRepository reservationThemeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void create() {
        // given
        CreateReservationTimeRequest request = new CreateReservationTimeRequest(LocalTime.now());

        // when
        reservationTimeService.create(request);

        // then
        assertThat(reservationTimeRepository.getAll()).hasSize(RESERVATION_TIME_COUNT + 1);
    }

    @Test
    void getAll() {
        // when
        List<ReservationTimeResponse> responses = reservationTimeService.getAll();

        // then
        assertThat(reservationTimeRepository.getAll()).hasSize(responses.size());
    }

    @Test
    void getAllByThemeIdAndDate() {
        // given
        Member member = memberRepository.save(new Member("pobi", "pobi@example.com", "password"));
        ReservationTheme theme = reservationThemeRepository.save(new ReservationTheme("theme", "desc", "thumb"));
        LocalDate date = LocalDate.of(2024, 5, 1);
        ReservationTime time1 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(5, 10)));
        ReservationTime time2 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(6, 10)));
        reservationRepository.save(new Reservation(member, date, time1, theme));

        // when
        List<ReservationTimeResponse> all = reservationTimeService.getAllByThemeIdAndDate(theme.id(), date);

        // then
        assertThat(all).hasSize(RESERVATION_TIME_COUNT + 2);
        assertThat(findById(all, time1.id()).isBooked()).isTrue();
        assertThat(findById(all, time2.id()).isBooked()).isFalse();
    }

    private ReservationTimeResponse findById(List<ReservationTimeResponse> all, Long id) {
        return all.stream()
                .filter(response -> Objects.equals(response.id(), id))
                .findAny()
                .orElse(null);
    }

    @Test
    void delete() {
        // given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));

        // when
        reservationTimeService.delete(time.id());

        // then
        assertThat(reservationTimeRepository.getAll()).hasSize(RESERVATION_TIME_COUNT);
    }


    @Test
    @DisplayName("삭제 대상이 없더라도 정상 처리된다.")
    void delete2() {
        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> reservationTimeService.delete(1000L);

        // then
        assertThatCode(throwingCallable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제 대상 시간에 대한 예약이 존재하면 예외를 반환한다.")
    void deleteException() {
        // given
        Member member = memberRepository.save(new Member("pobi", "pobi@example.com", "password"));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        ReservationTheme theme = reservationThemeRepository.save(new ReservationTheme("theme", "desc", "thumb"));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(1), time, theme));

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> reservationTimeService.delete(time.id());

        // then
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(DatabaseForeignKeyException.class)
                .hasMessage("삭제하려는 시간을 사용중인 예약이 있습니다.");
    }
}
