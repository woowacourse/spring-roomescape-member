package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.global.exception.custom.BusinessException;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDto;
import roomescape.domain.reservation.dto.request.ReservationUpdateRequestDto;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDto;
import roomescape.domain.reservation.dto.response.ReservationResponseDto;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.FakeReservationRepository;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.FakeThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.dto.response.TimeResponseDto;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.FakeTimeRepository;
import roomescape.domain.time.repository.TimeRepository;

class ReservationServiceTest {

    private final Clock fixedClock;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    ReservationServiceTest() {
        this.fixedClock = Clock.fixed(
            Instant.parse("2026-01-01T10:00:00Z"),
            ZoneId.of("UTC")
        );
        this.reservationRepository = new FakeReservationRepository();
        this.timeRepository = new FakeTimeRepository();
        this.themeRepository = new FakeThemeRepository();
        this.reservationService = new ReservationService(fixedClock, reservationRepository,
            timeRepository,
            themeRepository);
    }

    @Nested
    @DisplayName("getReservation 테스트")
    class GetReservationsTest {

        @Test
        @DisplayName("모든 예약을 조회한다.")
        void 성공() {

            // given
            LocalDate date = LocalDate.of(2026, 4, 30);
            Time time = Time.reconstruct(1L, LocalTime.of(10, 0));
            Theme theme = Theme.reconstruct(1L, "테마 이름", "테마 설명",
                "https://roomescape.com/images/themes/ring-banner.png");

            reservationRepository.save(Reservation.create("제이콥", date, time, theme, fixedClock));
            reservationRepository.save(
                Reservation.create("라이", date.plusDays(1),
                    Time.reconstruct(2L, LocalTime.of(11, 0)), theme, fixedClock));
            reservationRepository.save(
                Reservation.create("티모", date.plusDays(2),
                    Time.reconstruct(3L, LocalTime.of(12, 0)), theme, fixedClock));

            // when
            List<ReservationResponseDto> actual = reservationService.getReservations();

            // then
            assertAll(
                () -> assertEquals(3, actual.size()),
                () -> assertEquals(
                    new ReservationResponseDto(1L, "제이콥", date, TimeResponseDto.from(time),
                        ThemeResponseDto.from(theme)),
                    actual.get(0)),
                () -> assertEquals(
                    new ReservationResponseDto(2L, "라이", date.plusDays(1),
                        TimeResponseDto.from(Time.reconstruct(2L, LocalTime.of(11, 0))),
                        ThemeResponseDto.from(theme)),
                    actual.get(1)
                ),
                () -> assertEquals(
                    new ReservationResponseDto(3L, "티모", date.plusDays(2),
                        TimeResponseDto.from(Time.reconstruct(3L, LocalTime.of(12, 0))),
                        ThemeResponseDto.from(theme)),
                    actual.get(2)
                )
            );
        }
    }

    @Nested
    @DisplayName("getReservationByName 테스트")
    class GetReservationByNameTest {

        @Test
        @DisplayName("사용자의 모든 예약을 조회한다.")
        void 성공() {
            LocalDate date = LocalDate.of(2026, 4, 30);
            Time time = Time.reconstruct(1L, LocalTime.of(10, 0));
            Theme theme = Theme.reconstruct(1L, "테마 이름", "테마 설명",
                "https://roomescape.com/images/themes/ring-banner.png");

            reservationRepository.save(Reservation.create("제이콥", date, time, theme, fixedClock));
            reservationRepository.save(
                Reservation.create("라이", date.plusDays(1),
                    Time.reconstruct(2L, LocalTime.of(11, 0)), theme, fixedClock));
            reservationRepository.save(
                Reservation.create("티모", date.plusDays(2),
                    Time.reconstruct(3L, LocalTime.of(12, 0)), theme, fixedClock));
            String name = "제이콥";

            List<ReservationResponseDto> actual = reservationService.getReservationsByName(name);

            assertAll(
                () -> assertEquals(1, actual.size()),
                () -> assertEquals(
                    new ReservationResponseDto(1L, "제이콥", date, TimeResponseDto.from(time),
                        ThemeResponseDto.from(theme)),
                    actual.get(0))
            );
        }
    }

    @Nested
    @DisplayName("saveReservation 테스트")
    class SaveReservationTest {

        @Test
        @DisplayName("예약을 생성하고, 생성된 예약을 반환한다.")
        void 성공() {

            // given
            ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                "보예",
                LocalDate.of(2026, 5, 1),
                1L,
                1L
            );

            themeRepository.save(Theme.create("피온", "테마 설명",
                "https://roomescape.com/images/themes/ring-banner.png"));
            timeRepository.save(Time.create(LocalTime.of(15, 30)));

            // when
            ReservationCreateResponseDto actual = reservationService.saveReservation(request);

            // then
            assertAll(
                () -> assertEquals(1L, actual.id()),
                () -> assertEquals("보예", actual.name()),
                () -> assertEquals(LocalDate.of(2026, 5, 1), actual.date()),
                () -> assertEquals(1L, actual.timeId()),
                () -> assertEquals(1L, actual.themeId()),
                () -> assertEquals(1, reservationRepository.findAllReservations().size())
            );
        }

        @Test
        @DisplayName("날짜, 시간과 테마가 모두 같은 예약이 존재하는 경우 예외가 발생한다.")
        void 실패1() {
            Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
            Theme theme = themeRepository.save(Theme.create("테마명", "테마 설명",
                "https://roomescape.com/images/themes/ring-banner.png"));
            ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                "보예",
                LocalDate.of(2026, 5, 1),
                time.getId(),
                theme.getId()
            );
            reservationRepository.save(
                Reservation.create(request.name(), request.date(), time, theme, fixedClock));

            assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BusinessException.class);
        }

        @Test
        @DisplayName("요청한 시간 id가 존재하지 않으면 예외가 발생한다.")
        void 실패2() {
            Theme theme = themeRepository.save(Theme.create("테마명", "테마 설명",
                "https://roomescape.com/images/themes/ring-banner.png"));
            Long wrongTimeId = 99999L;
            ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                "보예",
                LocalDate.of(2026, 5, 1),
                wrongTimeId,
                theme.getId()
            );

            assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.TIME_NOT_FOUND);
        }

        @Test
        @DisplayName("요청한 테마 id가 존재하지 않으면 예외가 발생한다.")
        void 실패3() {
            Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
            Long wrongThemeId = 99999L;
            ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                "보예",
                LocalDate.of(2026, 5, 1),
                time.getId(),
                wrongThemeId
            );

            assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.THEME_NOT_FOUND);
        }

        @Test
        @DisplayName("지난 날짜와 시간으로 예약을 생성하려고 하면 예외가 발생한다.")
        void 실패4() {
            Time time = timeRepository.save(Time.create(LocalTime.of(15, 30)));
            Theme theme = themeRepository.save(Theme.create("테마명", "테마 설명",
                "https://roomescape.com/images/themes/ring-banner.png"));
            ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                "보예",
                LocalDate.of(2025, 12, 31),
                time.getId(),
                theme.getId()
            );

            assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_ALREADY_PASSED);
        }
    }

    @Nested
    @DisplayName("updateReservation 테스트")
    class UpdateReservationTest {

        @Test
        @DisplayName("주어진 예약의 날짜와 시간을 변경한다.")
        void 성공1() {
            String name = "시오";
            Reservation savedReservation = reservationRepository.save(
                Reservation.create(name, LocalDate.of(2026, 5, 3),
                    Time.reconstruct(2L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png"), fixedClock));
            Long id = savedReservation.getId();
            LocalDate changeDate = LocalDate.of(2026, 5, 2);
            Long changeTimeId = timeRepository.save(Time.create(LocalTime.of(20, 30))).getId();
            ReservationUpdateRequestDto request = new ReservationUpdateRequestDto(
                changeDate, changeTimeId);

            reservationService.updateReservation(name, id, request);
            Optional<Reservation> updatedReservation = reservationRepository.findReservationById(
                id);

            assertThat(updatedReservation).isPresent();
            assertAll(
                () -> assertThat(updatedReservation.get().getDate()).isEqualTo(changeDate),
                () -> assertThat(updatedReservation.get().getTime().getId()).isEqualTo(changeTimeId)
            );
        }

        @Test
        @DisplayName("기존 날짜와 시간 그대로 변경 요청하면 자기 자신을 중복 예약으로 보지 않는다.")
        void 성공2() {
            String name = "시오";
            Time time = timeRepository.save(Time.create(LocalTime.of(13, 0)));
            Reservation savedReservation = reservationRepository.save(
                Reservation.create(name, LocalDate.of(2026, 5, 3), time,
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png"), fixedClock));
            ReservationUpdateRequestDto request = new ReservationUpdateRequestDto(
                savedReservation.getDate(), time.getId());

            reservationService.updateReservation(name, savedReservation.getId(), request);

            Optional<Reservation> actual = reservationRepository.findReservationById(
                savedReservation.getId());

            assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getDate()).isEqualTo(savedReservation.getDate()),
                () -> assertThat(actual.get().getTime().getId()).isEqualTo(time.getId())
            );
        }

        @Test
        @DisplayName("요청한 시간 id가 존재하지 않으면 예외가 발생한다.")
        void 실패1() {
            String name = "시오";
            Reservation savedReservation = reservationRepository.save(
                Reservation.create(name, LocalDate.of(2026, 5, 3),
                    Time.reconstruct(2L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png"), fixedClock));
            Long id = savedReservation.getId();
            LocalDate changeDate = LocalDate.of(2026, 5, 2);
            Long wrongId = 99999L;
            ReservationUpdateRequestDto request = new ReservationUpdateRequestDto(
                changeDate, wrongId);
            List<ErrorDetail> expectedErrors = List.of(
                ErrorDetail.of("timeId", wrongId, "요청한 시간 id가 존재하지 않습니다."));

            assertThatThrownBy(() -> reservationService.updateReservation(name, id, request))
                .isInstanceOfSatisfying(BusinessException.class, exception -> assertAll(
                    () -> assertThat(exception.getErrorCode())
                        .isEqualTo(ErrorCode.COMMON_INVALID_REQUEST_BODY),
                    () -> assertThat(exception.getErrors()).isEqualTo(expectedErrors)
                ));
        }

        @Test
        @DisplayName("요청한 예약에 권한이 없는 경우 예외가 발생한다.")
        void 실패2() {
            Reservation savedReservation = reservationRepository.save(
                Reservation.create("시오", LocalDate.of(2026, 5, 3),
                    Time.reconstruct(2L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png"), fixedClock));
            String wrongName = "잘못된 이름";
            Long id = savedReservation.getId();
            LocalDate changeDate = LocalDate.of(2026, 5, 2);
            Long wrongId = 99999L;
            ReservationUpdateRequestDto request = new ReservationUpdateRequestDto(
                changeDate, wrongId);

            assertThatThrownBy(() -> reservationService.updateReservation(wrongName, id, request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_FORBIDDEN);
        }

        @Test
        @DisplayName("변경하려는 날짜와 시간에 이미 예약이 있으면 예외가 발생한다.")
        void 실패3() {
            String name = "시오";
            Theme theme = Theme.reconstruct(1L, "테마 이름", "테마 설명",
                "https://roomescape.com/images/themes/ring-banner.png");
            Time originalTime = timeRepository.save(Time.create(LocalTime.of(13, 0)));
            Time duplicatedTime = timeRepository.save(Time.create(LocalTime.of(20, 30)));
            Reservation savedReservation = reservationRepository.save(
                Reservation.create(name, LocalDate.of(2026, 5, 3), originalTime, theme,
                    fixedClock));
            reservationRepository.save(
                Reservation.create("다른 이름", LocalDate.of(2026, 5, 4), duplicatedTime, theme,
                    fixedClock));
            Long id = savedReservation.getId();
            ReservationUpdateRequestDto request = new ReservationUpdateRequestDto(
                LocalDate.of(2026, 5, 4), duplicatedTime.getId());

            assertThatThrownBy(() -> reservationService.updateReservation(name, id, request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_DUPLICATE);
        }

        @Test
        @DisplayName("지난 예약을 변경하려고 하면 예외가 발생한다.")
        void 실패4() {
            String name = "시오";
            Reservation savedReservation = reservationRepository.save(
                Reservation.reconstruct(1L, name, LocalDate.of(2025, 12, 31),
                    Time.reconstruct(2L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png")));
            Long id = savedReservation.getId();
            LocalDate changeDate = LocalDate.of(2026, 5, 2);
            Long changeTimeId = timeRepository.save(Time.create(LocalTime.of(20, 30))).getId();
            ReservationUpdateRequestDto request = new ReservationUpdateRequestDto(
                changeDate, changeTimeId);

            assertThatThrownBy(() -> reservationService.updateReservation(name, id, request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_ALREADY_PASSED);
        }

        @Test
        @DisplayName("지난 시점으로 변경하려고 하면 예외가 발생한다.")
        void 실패5() {
            String name = "시오";
            Reservation savedReservation = reservationRepository.save(
                Reservation.create(name, LocalDate.of(2026, 5, 3),
                    Time.reconstruct(2L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png"), fixedClock));
            Long id = savedReservation.getId();
            LocalDate changeDate = LocalDate.of(2025, 12, 31);
            Long changeTimeId = timeRepository.save(Time.create(LocalTime.of(20, 30))).getId();
            ReservationUpdateRequestDto request = new ReservationUpdateRequestDto(
                changeDate, changeTimeId);

            assertThatThrownBy(() -> reservationService.updateReservation(name, id, request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_TIME_ALREADY_PASSED);
        }

        @Test
        @DisplayName("존재하지 않는 예약을 변경하려고 하면 예외가 발생한다.")
        void 실패6() {
            Long notFoundId = 99999L;
            Long timeId = timeRepository.save(Time.create(LocalTime.of(20, 30))).getId();
            ReservationUpdateRequestDto request = new ReservationUpdateRequestDto(
                LocalDate.of(2026, 5, 2), timeId);

            assertThatThrownBy(() -> reservationService.updateReservation("시오", notFoundId, request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
        }

    }

    @Nested
    @DisplayName("deleteReservationById 테스트")
    class DeleteReservationByIdTest {

        @Test
        @DisplayName("주어진 아이디를 가진 예약을 삭제한다.")
        void 성공() {

            // given
            Reservation savedReservation = reservationRepository.save(
                Reservation.create("제이슨", LocalDate.of(2026, 5, 2),
                    Time.reconstruct(1L, LocalTime.of(12, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png"), fixedClock));
            reservationRepository.save(
                Reservation.create("시오", LocalDate.of(2026, 5, 3),
                    Time.reconstruct(2L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png"), fixedClock));

            // when
            reservationService.deleteReservationById(savedReservation.getId());

            // then
            List<ReservationResponseDto> actual = reservationService.getReservations();
            assertAll(
                () -> assertEquals(1, actual.size()),
                () -> assertEquals("시오", actual.getFirst().name())
            );

        }
    }

    @Nested
    @DisplayName("deleteMemberReservationById 테스트")
    class DeleteMemberReservationByIdTest {

        @Test
        @DisplayName("본인의 예약을 삭제한다.")
        void 성공() {
            String name = "시오";
            Reservation savedReservation = reservationRepository.save(
                Reservation.create(name, LocalDate.of(2026, 5, 3),
                    Time.reconstruct(1L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png"), fixedClock));
            reservationRepository.save(
                Reservation.create("다른 이름", LocalDate.of(2026, 5, 4),
                    Time.reconstruct(2L, LocalTime.of(14, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png"), fixedClock));

            reservationService.deleteMemberReservationById(name, savedReservation.getId());

            List<ReservationResponseDto> actual = reservationService.getReservations();
            assertAll(
                () -> assertEquals(1, actual.size()),
                () -> assertEquals("다른 이름", actual.getFirst().name())
            );
        }

        @Test
        @DisplayName("다른 사용자의 예약을 삭제하려고 하면 예외가 발생한다.")
        void 실패1() {
            Reservation savedReservation = reservationRepository.save(
                Reservation.create("시오", LocalDate.of(2026, 5, 3),
                    Time.reconstruct(1L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png"), fixedClock));

            assertThatThrownBy(() -> reservationService.deleteMemberReservationById("다른 이름",
                    savedReservation.getId()))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_FORBIDDEN);
        }

        @Test
        @DisplayName("존재하지 않는 예약을 삭제하려고 하면 예외가 발생한다.")
        void 실패2() {
            Long notFoundId = 99999L;

            assertThatThrownBy(() -> reservationService.deleteMemberReservationById("시오", notFoundId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
        }

        @Test
        @DisplayName("지난 예약을 삭제하려고 하면 예외가 발생한다.")
        void 실패3() {
            String name = "시오";
            Reservation savedReservation = reservationRepository.save(
                Reservation.reconstruct(1L, name, LocalDate.of(2025, 12, 31),
                    Time.reconstruct(1L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png")));

            assertThatThrownBy(() -> reservationService.deleteMemberReservationById(name,
                    savedReservation.getId()))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_ALREADY_PASSED);
        }
    }
}
