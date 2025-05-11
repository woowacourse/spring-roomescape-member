package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.constant.TestData.RESERVATION_THEME_COUNT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import roomescape.DataBasedTest;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.CreateReservationThemeRequest;
import roomescape.dto.response.ReservationThemeResponse;
import roomescape.exception.DatabaseForeignKeyException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;

class ReservationThemeServiceTest extends DataBasedTest {

    @Autowired
    ReservationThemeService reservationThemeService;

    @Autowired
    ReservationThemeRepository reservationThemeRepository;

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void create() {
        // given
        CreateReservationThemeRequest request = new CreateReservationThemeRequest("theme", "desc",
                "thumb");

        // when
        reservationThemeService.create(request);

        // then
        assertThat(reservationThemeRepository.getAll()).hasSize(RESERVATION_THEME_COUNT + 1);
    }

    @Test
    void getAll() {
        // when
        List<ReservationThemeResponse> responses = reservationThemeService.getAll();

        // then
        assertThat(reservationThemeRepository.getAll()).hasSize(responses.size());
    }

    @Test
    void delete() {
        // given
        ReservationTheme theme = reservationThemeRepository.save(new ReservationTheme("theme", "desc", "thumb"));

        // when
        reservationThemeService.delete(theme.id());

        // then
        assertThat(reservationThemeRepository.getAll()).hasSize(RESERVATION_THEME_COUNT);
    }

    @Test
    @DisplayName("삭제 대상이 없더라도 정상 처리된다.")
    void delete2() {
        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> reservationThemeService.delete(1000L);

        // then
        assertThatCode(throwingCallable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제 대상 테마에 대한 예약이 존재하면 예외를 반환한다.")
    void deleteException() {
        // given
        Member member = memberRepository.save(new Member("pobi", "pobi@example.com", "password"));
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        ReservationTheme theme = reservationThemeRepository.save(new ReservationTheme("theme", "desc", "thumb"));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(1), time, theme));

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> reservationThemeService.delete(theme.id());

        // then
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(DatabaseForeignKeyException.class)
                .hasMessage("삭제하려는 테마에 예약이 존재합니다.");
    }

    @Test
    void getPopularThemes() {
        // given
        Member member = memberRepository.save(new Member("pobi", "pobi@example.com", "password"));
        ReservationTheme theme1 = reservationThemeRepository.getById(1L);
        ReservationTheme theme2 = reservationThemeRepository.getById(2L);
        ReservationTheme theme3 = reservationThemeRepository.getById(3L);
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(1), time, theme1));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(2), time, theme2));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(3), time, theme2));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(4), time, theme3));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(5), time, theme3));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(6), time, theme3));

        // when
        List<ReservationThemeResponse> popularThemes = reservationThemeService.getPopularThemes(3);

        // then
        assertThat(popularThemes.get(0).id()).isEqualTo(theme3.id());
        assertThat(popularThemes.get(1).id()).isEqualTo(theme2.id());
        assertThat(popularThemes.get(2).id()).isEqualTo(theme1.id());
    }
}
