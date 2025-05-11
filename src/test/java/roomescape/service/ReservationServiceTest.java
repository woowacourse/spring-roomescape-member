package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static roomescape.constant.TestData.RESERVATION_COUNT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import roomescape.DataBasedTest;
import roomescape.domain.Member;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.request.SearchReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;

class ReservationServiceTest extends DataBasedTest {

    @Autowired
    ReservationService reservationService;

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
        var member = memberRepository.save(new Member("pobi", "pobi@example.com", "password"));
        var time = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        var theme = reservationThemeRepository.save(new ReservationTheme("theme", "desc", "thumb"));
        var request = new CreateReservationRequest(LocalDate.now().plusDays(1), time.id(), theme.id());

        // when
        reservationService.create(member.id(), LocalDate.now().plusDays(1), time.id(), theme.id());

        // then
        assertThat(reservationRepository.getAll()).hasSize(RESERVATION_COUNT + 1);
    }

    @Test
    void getAll() {
        // when
        List<ReservationResponse> responses = reservationService.getAll();

        // then
        assertThat(reservationRepository.getAll()).hasSize(responses.size());
    }

    @Test
    void delete() {
        // when
        reservationService.delete(1L);

        // then
        assertThat(reservationRepository.getAll()).hasSize(RESERVATION_COUNT - 1);
    }

    @Test
    @DisplayName("삭제 대상이 없더라도 정상 처리된다.")
    void delete2() {
        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> reservationService.delete(1000L);

        // then
        assertThatCode(throwingCallable).doesNotThrowAnyException();
    }

    @Test
    void search() {
        // given
        var themeSearch = new SearchReservationRequest(1L, null, null, null);
        var memberSearch = new SearchReservationRequest(null, 1L, null, null);
        var dateFromSearch = new SearchReservationRequest(null, null, LocalDate.of(2025, 1, 1), null);
        var dateToSearch = new SearchReservationRequest(null, null, null, LocalDate.of(2025, 1, 1));

        // when
        List<ReservationResponse> theme = reservationService.search(themeSearch);
        List<ReservationResponse> member = reservationService.search(memberSearch);
        List<ReservationResponse> dateFrom = reservationService.search(dateFromSearch);
        List<ReservationResponse> dateTo = reservationService.search(dateToSearch);

        // then
        assertThat(theme).hasSize(1);
        assertThat(member).hasSize(1);
        assertThat(dateFrom).hasSize(RESERVATION_COUNT);
        assertThat(dateTo).hasSize(0);
    }
}
