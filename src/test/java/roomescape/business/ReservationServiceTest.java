//package roomescape.business;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatCode;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import roomescape.business.dto.AvailableTimesResponseDto;
//import roomescape.business.dto.ReservationRequestDto;
//import roomescape.business.dto.ReservationThemeRequestDto;
//import roomescape.business.dto.ReservationThemeResponseDto;
//import roomescape.business.dto.ReservationTimeRequestDto;
//import roomescape.business.dto.ReservationTimeResponseDto;
//import roomescape.business.fakerepository.FakeReservationRepository;
//import roomescape.business.fakerepository.FakeReservationThemeRepository;
//import roomescape.business.fakerepository.FakeReservationTimeRepository;
//import roomescape.business.service.ReservationService;
//import roomescape.persistence.ReservationRepository;
//import roomescape.persistence.ReservationThemeRepository;
//import roomescape.persistence.ReservationTimeRepository;
//
//class ReservationServiceTest { // TODO: 테스트 분리 및 리팩터링
//
//    private ReservationTimeRepository reservationTimeRepository;
//    private ReservationThemeRepository reservationThemeRepository;
//    private ReservationService reservationService;
//    private Long timeId;
//    private Long themeId;
//
//    @BeforeEach
//    void setUp() {
//        ReservationRepository reservationRepository = new FakeReservationRepository();
//        reservationTimeRepository = new FakeReservationTimeRepository();
//        reservationThemeRepository = new FakeReservationThemeRepository();
//        reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
//                reservationThemeRepository);
//        timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now()));
//        themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "그림"));
//    }
//
//    @DisplayName("예약한다.")
//    @Test
//    void createReservation() {
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
//        reservationService.createReservation(new ReservationRequestDto("예약자", tomorrow, timeId, themeId));
//        assertThat(reservationService.readReservationAll()).isNotEmpty();
//    }
//
//    @DisplayName("과거 일시로 예약을 생성할 경우 예외가 발생한다.")
//    @Test
//    void createPastReservation() {
//        // given
//        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
//        ReservationRequestDto reservationRequestDto = new ReservationRequestDto("벨로", pastDateTime.toLocalDate(),
//                timeId, themeId);
//
//        // when
//        // then
//        assertThatCode(() -> reservationService.createReservation(reservationRequestDto))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("과거 일시로 예약을 생성할 수 없습니다.");
//    }
//
//    @DisplayName("이미 예약된 경우 예약할 수 없다.")
//    @Test
//    void failCreateReservation() {
//        // given
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
//        reservationService.createReservation(new ReservationRequestDto("예약자", tomorrow, timeId, themeId));
//
//        // when
//        // then
//        assertThatCode(() ->
//                reservationService.createReservation(new ReservationRequestDto("예약자", tomorrow, timeId, themeId))
//        )
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("이미 예약되었습니다.");
//    }
//
//    @DisplayName("예약을 취소한다.")
//    @Test
//    void deleteReservation() {
//        // given
//        Long id = reservationService.createReservation(
//                new ReservationRequestDto("예약자", LocalDate.now().plusDays(1), timeId, themeId));
//
//        // when
//        reservationService.deleteReservation(id);
//
//        // then
//        assertThat(reservationService.readReservationAll()).isEmpty();
//    }
//
//    @DisplayName("예약 목록을 불러온다")
//    @Test
//    void readReservationAll() {
//        // given
//        Long id = reservationService.createReservation(
//                new ReservationRequestDto("예약자", LocalDate.now().plusDays(1), timeId, themeId));
//
//        // when
//        int firstReadSize = reservationService.readReservationAll().size();
//        reservationService.deleteReservation(id);
//        int secondReadSize = reservationService.readReservationAll().size();
//
//        // then
//        assertThat(firstReadSize).isEqualTo(1);
//        assertThat(secondReadSize).isEqualTo(0);
//    }
//
//    @DisplayName("아이디로 예약 가능한 시간을 조회한다")
//    @Test
//    void readTimeOne() {
//        ReservationTimeResponseDto reservationTime = reservationService.readTimeOne(1L);
//        assertThat(reservationTime).isNotNull();
//    }
//
//    @DisplayName("모든 시간을 조회한다")
//    @Test
//    void readAvailableTimes() {
//        // given
//        Long two = reservationTimeRepository.add(new ReservationTime(2L, LocalTime.now()));
//
//        // when
//        List<AvailableTimesResponseDto> reservationTimes = reservationService.readAvailableTimes(LocalDate.now(), themeId);
//
//        // then
//        assertThat(reservationTimes).hasSize(2);
//    }
//
//    @DisplayName("예약 가능한 시간을 추가한다")
//    @Test
//    void createTime() {
//        Long timeId = reservationService.createTime(new ReservationTimeRequestDto(LocalTime.now()));
//        assertThat(timeId).isEqualTo(1L);
//    }
//
//    @DisplayName("예약 시간대 하나를 삭제한다")
//    @Test
//    void deleteTime() {
//        reservationService.deleteTime(1L);
//        assertThat(reservationService.readAvailableTimes(LocalDate.now(), themeId)).isEmpty();
//    }
//
//    @DisplayName("예약이 참조하는 시간대 하나를 삭제한다")
//    @Test
//    void deleteReferencedTime() {
//        // given
//        reservationService.createReservation(
//                new ReservationRequestDto("수양", LocalDate.now().plusDays(1), timeId, themeId));
//
//        // when
//        // then
//        assertThatCode(() -> reservationService.deleteTime(timeId))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("해당 시간의 예약이 존재하여 시간을 삭제할 수 없습니다.");
//    }
//
//    @DisplayName("동일한 이름의 테마를 추가할 경우 예외가 발생한다.")
//    @Test
//    void createSameNameTheme() {
//        // given
//        ReservationThemeRequestDto reservationThemeRequest = new ReservationThemeRequestDto("수양", "수양테마", "수양썸네일");
//        reservationService.createTheme(reservationThemeRequest);
//
//        // when
//        // then
//        assertThatCode(() -> reservationService.createTheme(reservationThemeRequest))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("동일한 이름의 테마를 추가할 수 없습니다.");
//    }
//
//    @DisplayName("테마를 삭제한다.")
//    @Test
//    void deleteTheme() {
//        // given
//        ReservationThemeRequestDto reservationThemeRequest = new ReservationThemeRequestDto("수양", "수양테마", "수양썸네일");
//        ReservationThemeResponseDto theme = reservationService.createTheme(reservationThemeRequest);
//
//        // when
//        // then
//        assertThatCode(() -> reservationService.deleteTheme(theme.id()))
//                .doesNotThrowAnyException();
//    }
//
//
//    @DisplayName("예약이 참조하고 있는 테마를 삭제한다.")
//    @Test
//    void deleteReferencedTheme() {
//        // given
//        reservationService.createReservation(
//                new ReservationRequestDto("벨로", LocalDate.now().plusDays(1), timeId, themeId));
//
//        // when
//        // then
//        assertThatCode(() -> reservationService.deleteTheme(themeId))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("해당 테마의 예약이 존재하여 삭제할 수 없습니다.");
//    }
//}