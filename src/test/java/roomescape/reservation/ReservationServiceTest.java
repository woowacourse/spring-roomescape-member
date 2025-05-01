package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.globalexception.BadRequestException;
import roomescape.globalexception.ConflictException;
import roomescape.globalexception.NotFoundException;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationtime.FakeReservationTimeRepository;

public class ReservationServiceTest {

    private final ReservationService reservationService;
    private final FakeReservationRepository fakeReservationRepository;
    private final FakeReservationTimeRepository fakeReservationTimeRepository;

    public ReservationServiceTest() {
        fakeReservationRepository = new FakeReservationRepository();
        fakeReservationTimeRepository = new FakeReservationTimeRepository();
        reservationService = new ReservationService(fakeReservationRepository, fakeReservationTimeRepository);
    }

    @BeforeEach
    void setUp() {
        fakeReservationRepository.clear();
    }

    @Nested
    @DisplayName("예약 생성")
    class Create {

        @DisplayName("reservation request를 생성하면 response 값을 반환한다.")
        @Test
        void create() {
            // given
            final ReservationRequest request = new ReservationRequest(
                    "로키",
                    LocalDate.now().plusDays(1),
                    1L, 1L);
            fakeReservationRepository.addTheme(1L);
            fakeReservationRepository.addTimes(1L);

            // when
            final ReservationResponse response = reservationService.create(request);

            // then
            assertSoftly(s -> {
                s.assertThat(response.id()).isNotNull();
                s.assertThat(response.name()).isEqualTo(request.name());
                s.assertThat(response.date()).isEqualTo(request.date());
                s.assertThat(response.time().id()).isEqualTo(1L);
                s.assertThat(response.theme().id()).isEqualTo(1L);
            });
        }

        @DisplayName("테마가 존재하지 않으면 예외가 발생한다.")
        @Test
        void create1() {
            // given
            final ReservationRequest request = new ReservationRequest(
                    "로키",
                    LocalDate.now().plusDays(1),
                    1L, 1L);
            fakeReservationRepository.addTimes(1L);

            // when & then
            assertThatThrownBy(() -> {
                reservationService.create(request);
            }).isInstanceOf(BadRequestException.class);
        }

        @DisplayName("시간이 존재하지 않으면 예외가 발생한다.")
        @Test
        void create2() {
            // given
            final ReservationRequest request = new ReservationRequest(
                    "로키",
                    LocalDate.now().plusDays(1),
                    1L, 1L);
            fakeReservationRepository.addTheme(1L);

            // when & then
            assertThatThrownBy(() -> {
                reservationService.create(request);
            }).isInstanceOf(BadRequestException.class);
        }

        @DisplayName("이미 해당 시간, 날짜에 예약이 존재한다면 예외가 발생한다.")
        @Test
        void create3() {
            // given
            // dummy 시간이 12시 40분
            final ReservationRequest request = new ReservationRequest(
                    "로키",
                    LocalDate.now().plusDays(1),
                    1L, 1L);
            fakeReservationRepository.addTheme(1L);
            fakeReservationRepository.addTimes(1L);
            fakeReservationRepository.save(
                    new Reservation("", LocalDate.now().plusDays(1)),
                    1L, 1L
            );

            // when & then
            assertThatThrownBy(() -> {
                reservationService.create(request);
            }).isInstanceOf(ConflictException.class);
        }
    }

    @Nested
    @DisplayName("예약 모두 조회")
    class ReadAll {

        @DisplayName("reservation이 없다면 빈 컬렉션을 조회한다.")
        @Test
        void readAll1() {
            // given & when
            final List<ReservationResponse> allReservation = reservationService.readAll();

            // then
            assertThat(allReservation).hasSize(0);
        }

        @DisplayName("존재하는 reservation들을 모두 조회한다.")
        @Test
        void readAll2() {
            // given
            fakeReservationRepository.addTheme(1L);
            fakeReservationRepository.addTimes(1L);
            fakeReservationRepository.save(
                    new Reservation("", LocalDate.of(2026, 12, 1)),
                    1L, 1L
            );

            // when
            final List<ReservationResponse> actual = reservationService.readAll();

            // then
            assertThat(actual).hasSize(1);
        }

    }

    @Nested
    @DisplayName("예약 삭제")
    class Delete {

        @DisplayName("주어진 id에 해당하는 reservation 삭제한다.")
        @Test
        void delete1() {
            // given
            final Long id = 1L;
            fakeReservationRepository.addTimes(1L);
            fakeReservationRepository.addTheme(1L);
            fakeReservationRepository.save(new Reservation("", LocalDate.of(2026, 12, 1)), 1L, 1L);

            // when
            reservationService.deleteById(id);

            // then
            assertThat(fakeReservationRepository.isInvokeDeleteById(id)).isTrue();
        }

        @DisplayName("주어진 id에 해당하는 reservation이 없다면 예외가 밠애한다.")
        @Test
        void delete2() {
            // given & when & then
            assertThatThrownBy(() -> {
                reservationService.deleteById(1L);
            }).isInstanceOf(NotFoundException.class);
        }

    }


}
