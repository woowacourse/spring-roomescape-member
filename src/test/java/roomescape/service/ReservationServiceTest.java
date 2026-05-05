package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.DuplicateEntityException;
import roomescape.domain.EntityNotFoundException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.global.auth.Accessor;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.collection.MemoryReservationRepository;
import roomescape.repository.collection.MemoryReservationTimeRepository;
import roomescape.service.command.ReservationCommand;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        this.reservationRepository = new MemoryReservationRepository();
        this.reservationTimeRepository = new MemoryReservationTimeRepository();
        this.reservationService = new ReservationService(reservationRepository, reservationTimeRepository);
    }

    @Test
    void 새로운_예약을_정상적으로_등록한다() {
        // given: 예약 시간이 먼저 등록되어 있음
        Accessor admin = new Accessor("ADMIN");
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        LocalDate reservationDate = LocalDate.now().plusDays(1);
        ReservationCommand command = new ReservationCommand("이프", reservationDate, time.getId());

        // when: 예약 진행
        ReservationResult result = reservationService.reserve(admin, command);

        // then: 등록된 예약 정보가 입력값과 일치함
        ReservationTimeResult timeResult = ReservationTimeResult.from(time);
        assertThat(result)
                .extracting(
                        ReservationResult::id,
                        ReservationResult::name,
                        ReservationResult::date,
                        ReservationResult::time
                )
                .containsExactly(1L, "이프", reservationDate, timeResult);
    }

    @Test
    void 시간_정보가_등록되지_않았을_떄_예약하면_예외가_발생한다() {
        // given: 시간 ID가 등록되지 않음
        Accessor admin = new Accessor("ADMIN");
        ReservationCommand command = new ReservationCommand("이프", LocalDate.now().plusDays(1), 1L);

        // when & then: EntityNotFoundException 발생 확인
        assertThatThrownBy(() -> reservationService.reserve(admin, command))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("존재하지 않는 시간 정보입니다.");
    }

    @Test
    void 같은_날짜와_같은_시간에_중복_예약을_시도하면_예외가_발생한다() {
        // given: 이미 10시 예약이 하나 존재함
        Accessor admin = new Accessor("ADMIN");
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        reservationRepository.save(new Reservation("기존예약자", date, time));

        ReservationCommand command = new ReservationCommand("새예약자", date, time.getId());

        // when & then: DuplicateEntityException 발생 확인
        assertThatThrownBy(() -> reservationService.reserve(admin, command))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("이미 예약 된 날짜입니다.");
    }

    @Test
    void 모든_예약_목록을_조회한다() {
        // given: 2개의 예약이 저장되어 있음
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        reservationRepository.save(new Reservation("이프", LocalDate.now().plusDays(1), time));
        reservationRepository.save(new Reservation("아루", LocalDate.now().plusDays(2), time));

        // when: 전체 조회를 요청함
        List<ReservationResult> results = reservationService.getAllReservations();

        // then: 2개의 결과가 반환됨
        assertThat(results).hasSize(2);
    }

    @Test
    void 식별자를_이용해_예약을_취소한다() {
        // given: 취소할 예약이 저장되어 있음
        Accessor admin = new Accessor("ADMIN");
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        Reservation saved = reservationRepository.save(new Reservation("이프", LocalDate.now().plusDays(1), time));

        // when: 삭제 요청
        reservationService.cancelReservation(admin, saved.getId());

        // then: 조회 시 목록이 비어있음
        assertThat(reservationService.getAllReservations()).isEmpty();
    }
}
