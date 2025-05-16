package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.test.fixture.ReservationTimeFixture.addReservationTimeInRepository;
import static roomescape.test.fixture.ThemeFixture.addThemeInRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.AdminReservationCreationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.test.fake.FakeH2MemberRepository;
import roomescape.test.fake.FakeH2ReservationRepository;
import roomescape.test.fake.FakeH2ReservationTimeRepository;
import roomescape.test.fake.FakeH2ThemeRepository;

class ReservationServiceTest {

    private static final LocalDate NEXT_DATE = LocalDate.now().plusDays(1);
    private static final LocalTime PAST_TIME = LocalTime.now().minusSeconds(1);
    private final ReservationRepository reservationRepository = new FakeH2ReservationRepository();
    private final ReservationTimeRepository timeRepository = new FakeH2ReservationTimeRepository();
    private final ThemeRepository themeRepository = new FakeH2ThemeRepository();
    private final MemberRepository memberRepository = new FakeH2MemberRepository();
    private final ReservationService reservationService = new ReservationService(
            reservationRepository,
            timeRepository,
            themeRepository,
            memberRepository
    );
    Member member = new Member(1L, "사람", "email", "비번", "member");

    @DisplayName("저장된 예약들을 조회할 수 있다")
    @Test
    void getReservations() {
        ReservationTime time = addReservationTimeInRepository(timeRepository, LocalTime.now());
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");
        reservationRepository.add(Reservation.createWithoutId(member, NEXT_DATE, time, theme));
        reservationRepository.add(Reservation.createWithoutId(member, NEXT_DATE, time, theme));
        reservationRepository.add(Reservation.createWithoutId(member, NEXT_DATE, time, theme));

        List<ReservationResponse> allReservations = reservationService.getAllReservations();

        assertThat(allReservations).hasSize(3);
    }

    @Disabled
    @DisplayName("예약을 추가할 수 있다.")
    @Test
    void createReservation() {
        ReservationTime time = addReservationTimeInRepository(timeRepository, LocalTime.now());
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");
        Member member = new Member(1L, "이름", "이메일", "비번", "역할");
        AdminReservationCreationRequest request =
                new AdminReservationCreationRequest(NEXT_DATE, time.getId(), theme.getId(), member.getId());

        long savedId = reservationService.saveReservation(request);

        Reservation savedReservation = reservationRepository.findAll().getFirst();
        Reservation expectedReservation = new Reservation(1L, member, request.date(), time, theme);
        assertAll(
                () -> assertThat(savedId).isEqualTo(1L),
                () -> assertThat(savedReservation).isEqualTo(expectedReservation)
        );
    }

    @Disabled
    @DisplayName("과거 날짜와 시간으로는 예약을 추가할 수 없다.")
    @Test
    void canNotCreateReservationWithPastDateTime() {
        ReservationTime pastTime = addReservationTimeInRepository(timeRepository, PAST_TIME);
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");
        AdminReservationCreationRequest request =
                new AdminReservationCreationRequest(LocalDate.now(), pastTime.getId(), theme.getId(), member.getId());

        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 과거의 날짜와 시간입니다.");
    }

    @Disabled
    @DisplayName("예약 중복을 허용하지 않는다.")
    @Test
    void canNotCreateReservationWithSameDateTime() {
        LocalDate sameDate = NEXT_DATE;
        ReservationTime sameTime = addReservationTimeInRepository(timeRepository, LocalTime.of(10, 0));
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");

        reservationRepository.add(Reservation.createWithoutId(member, sameDate, sameTime, theme));

        AdminReservationCreationRequest request =
                new AdminReservationCreationRequest(sameDate, sameTime.getId(), theme.getId(), member.getId());

        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 존재하는 예약입니다.");
    }

    @DisplayName("특정 ID의 예약을 삭제할 수 있다.")
    @Test
    void deleteReservation() {
        ReservationTime time = addReservationTimeInRepository(timeRepository, LocalTime.now());
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");

        reservationRepository.add(Reservation.createWithoutId(member, NEXT_DATE, time, theme));
        reservationRepository.add(Reservation.createWithoutId(member, NEXT_DATE, time, theme));
        reservationRepository.add(Reservation.createWithoutId(member, NEXT_DATE, time, theme));
        long deletedId = reservationRepository.findAll().getFirst().getId();

        reservationService.deleteReservation(deletedId);

        assertThat(reservationRepository.findAll())
                .extracting(Reservation::getId)
                .doesNotContain(deletedId);
    }

    @DisplayName("존재하지 않는 예약을 삭제하려고 할 경우 예외를 발생시킨다")
    @Test
    void deleteNoneExistentReservation() {
        long noneExistentId = 1L;
        assertThatThrownBy(() -> reservationService.deleteReservation(noneExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약이 존재하지 않습니다.");
    }
}
