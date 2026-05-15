package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.TestClockConfig;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.CreateReservationRequest;
import roomescape.dto.reservation.ReservationResponses;
import roomescape.dto.reservation.UpdateReservationRequest;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.InvalidReservationDateTimeException;
import roomescape.exception.ReservationOwnerMismatchException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.repository.fake.FakeReservationRepository;
import roomescape.repository.fake.FakeReservationTimeRepository;
import roomescape.repository.fake.FakeThemeRepository;

class ReservationServiceTest {

    private FakeReservationRepository reservationRepository;
    private FakeReservationTimeRepository reservationTimeRepository;
    private FakeThemeRepository themeRepository;
    private ReservationService service;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository(reservationRepository);
        service = new ReservationService(reservationRepository, themeRepository, reservationTimeRepository,
                new TestClockConfig().timeProvider());
    }

    @Test
    void createReservation_id가_채워진_도메인을_반환한다() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "https://thumbnail.url"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        CreateReservationRequest request = new CreateReservationRequest(
                "브라운", themeId, LocalDate.of(2026, 5, 8), timeId);

        Reservation created = service.createReservation(request);

        assertThat(created.getId()).isPositive();
        assertThat(created.getName()).isEqualTo("브라운");
        assertThat(created.getDate()).isEqualTo(LocalDate.of(2026, 5, 8));
        assertThat(created.getTheme().getId()).isEqualTo(themeId);
        assertThat(created.getTime().getId()).isEqualTo(timeId);
    }

    @Test
    void createReservation_과거의_날짜_시간이면_예외() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "https://thumbnail.url"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(11, 0)));

        assertThatThrownBy(() -> service.createReservation(new CreateReservationRequest(
                "브라운", themeId, LocalDate.of(2026, 5, 5), timeId)))
                .isInstanceOf(InvalidReservationDateTimeException.class)
                .hasMessage("예약 일정이 유효하지 않습니다. 예약 날짜와 시간은 현시간 이후여야 합니다.");
    }

    @Test
    void createReservation_같은_날짜시간테마_중복이면_예외() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "https://thumbnail.url"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        service.createReservation(new CreateReservationRequest(
                "브라운", themeId, LocalDate.of(2026, 5, 8), timeId));

        assertThatThrownBy(() -> service.createReservation(new CreateReservationRequest(
                "다른사람", themeId, LocalDate.of(2026, 5, 8), timeId)))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage("해당 날짜·시간·테마에 이미 예약이 존재합니다. 다른 날짜·시간·테마를 선택해주세요.");
    }

    @Test
    void createReservation_존재하지_않는_themeId이면_ResourceNotFoundException() {
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));

        assertThatThrownBy(() -> service.createReservation(new CreateReservationRequest(
                "브라운", 9999L, LocalDate.of(2026, 5, 8), timeId)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("테마")
                .hasMessageContaining("9999");
    }

    @Test
    void createReservation_존재하지_않는_timeId이면_ResourceNotFoundException() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "https://thumbnail.url"));

        assertThatThrownBy(() -> service.createReservation(new CreateReservationRequest(
                "브라운", themeId, LocalDate.of(2026, 5, 8), 9999L)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("예약 시간")
                .hasMessageContaining("9999");
    }

    @Test
    void getReservations_다음_페이지가_있으면_hasNext가_true() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        reservationRepository.save(buildReservation("A", themeId, timeId, LocalDate.of(2026, 5, 1)));
        reservationRepository.save(buildReservation("B", themeId, timeId, LocalDate.of(2026, 5, 2)));
        reservationRepository.save(buildReservation("C", themeId, timeId, LocalDate.of(2026, 5, 3)));

        ReservationResponses responses = service.getReservations(0, 2, null);

        assertThat(responses.reservations()).hasSize(2);
        assertThat(responses.hasNext()).isTrue();
    }

    @Test
    void getReservations_다음_페이지가_없으면_hasNext가_false() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        reservationRepository.save(buildReservation("A", themeId, timeId, LocalDate.of(2026, 5, 1)));
        reservationRepository.save(buildReservation("B", themeId, timeId, LocalDate.of(2026, 5, 2)));

        ReservationResponses responses = service.getReservations(0, 2, null);

        assertThat(responses.reservations()).hasSize(2);
        assertThat(responses.hasNext()).isFalse();
    }

    @Test
    void getReservations_name이_주어지면_해당_이름의_예약만_반환한다() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        reservationRepository.save(buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 5, 1)));
        reservationRepository.save(buildReservation("다른사람", themeId, timeId, LocalDate.of(2026, 5, 2)));
        reservationRepository.save(buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 5, 3)));

        ReservationResponses responses = service.getReservations(0, 10, "브라운");

        assertThat(responses.reservations()).hasSize(2);
        assertThat(responses.reservations()).extracting("name").containsOnly("브라운");
    }

    @Test
    void getReservation_id로_단건을_조회한다() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 5, 6)));

        Reservation found = service.getReservation(reservationId);

        assertThat(found.getId()).isEqualTo(reservationId);
        assertThat(found.getName()).isEqualTo("브라운");
    }

    @Test
    void getReservation_없는_id이면_ResourceNotFoundException() {
        assertThatThrownBy(() -> service.getReservation(9999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("예약")
                .hasMessageContaining("9999");
    }

    @Test
    void deleteReservation_없는_id이면_ResourceNotFoundException() {
        assertThatThrownBy(() -> service.deleteReservation(9999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("예약")
                .hasMessageContaining("9999");
    }

    @Test
    void deleteReservation_삭제후_조회되지_않는다() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 5, 6)));

        service.deleteReservation(reservationId);

        ReservationResponses responses = service.getReservations(0, 10, null);
        assertThat(responses.reservations()).extracting("id").doesNotContain(reservationId);
    }

    @Test
    void cancelOwnReservation_이름_불일치면_예외() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 5, 8)));

        assertThatThrownBy(() -> service.cancelOwnReservation(reservationId, "다른사람"))
                .isInstanceOf(ReservationOwnerMismatchException.class);
        assertThat(reservationRepository.findById(reservationId)).isPresent();
    }

    @Test
    void cancelOwnReservation_없는_id이면_ResourceNotFoundException() {
        assertThatThrownBy(() -> service.cancelOwnReservation(9999L, "브라운"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateOwnReservation_변경된_도메인을_반환한다() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long themeId2 = themeRepository.save(new Theme(null, "추리", "재밌음", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long timeId2 = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(11, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 6, 1)));

        Reservation updated = service.updateOwnReservation(reservationId,
                new UpdateReservationRequest("브라운", themeId2, LocalDate.of(2026, 6, 2), timeId2));

        assertThat(updated.getDate()).isEqualTo(LocalDate.of(2026, 6, 2));
        assertThat(updated.getTheme().getId()).isEqualTo(themeId2);
        assertThat(updated.getTime().getId()).isEqualTo(timeId2);
    }

    @Test
    void updateOwnReservation_이름_불일치면_예외() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 6, 1)));

        assertThatThrownBy(() -> service.updateOwnReservation(reservationId,
                new UpdateReservationRequest("다른사람", themeId, LocalDate.of(2026, 6, 2), timeId)))
                .isInstanceOf(ReservationOwnerMismatchException.class);
    }

    @Test
    void updateOwnReservation_기존_예약이_과거이면_예외() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 5, 1)));

        assertThatThrownBy(() -> service.updateOwnReservation(reservationId,
                new UpdateReservationRequest("브라운", themeId, LocalDate.of(2026, 6, 2), timeId)))
                .isInstanceOf(InvalidReservationDateTimeException.class);
    }

    @Test
    void updateOwnReservation_새_일정이_과거이면_예외() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 6, 1)));

        assertThatThrownBy(() -> service.updateOwnReservation(reservationId,
                new UpdateReservationRequest("브라운", themeId, LocalDate.of(2026, 5, 1), timeId)))
                .isInstanceOf(InvalidReservationDateTimeException.class);
    }

    @Test
    void updateOwnReservation_새_일정이_다른_예약과_충돌하면_예외() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long timeId2 = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(11, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 6, 1)));
        reservationRepository.save(buildReservation("다른사람", themeId, timeId2, LocalDate.of(2026, 6, 2)));

        assertThatThrownBy(() -> service.updateOwnReservation(reservationId,
                new UpdateReservationRequest("브라운", themeId, LocalDate.of(2026, 6, 2), timeId2)))
                .isInstanceOf(DuplicateReservationException.class);
    }

    @Test
    void updateOwnReservation_기존_슬롯과_동일하면_예외없이_통과() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Long reservationId = reservationRepository.save(
                buildReservation("브라운", themeId, timeId, LocalDate.of(2026, 6, 1)));

        Reservation updated = service.updateOwnReservation(reservationId,
                new UpdateReservationRequest("브라운", themeId, LocalDate.of(2026, 6, 1), timeId));

        assertThat(updated.getId()).isEqualTo(reservationId);
    }

    @Test
    void updateOwnReservation_없는_id이면_ResourceNotFoundException() {
        Long themeId = themeRepository.save(new Theme(null, "공포", "무서움", "u"));
        Long timeId = reservationTimeRepository.save(new ReservationTime(null, LocalTime.of(10, 0)));

        assertThatThrownBy(() -> service.updateOwnReservation(9999L,
                new UpdateReservationRequest("브라운", themeId, LocalDate.of(2026, 6, 2), timeId)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Reservation buildReservation(String name, Long themeId, Long timeId, LocalDate date) {
        Theme theme = themeRepository.findById(themeId).orElseThrow();
        ReservationTime time = reservationTimeRepository.findById(timeId).orElseThrow();
        return new Reservation(null, name, theme, date, time);
    }
}