package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteReservation(Long id) {
        reservationRepository.removeById(id);
    }

    public ReservationResponse createReservation(ReservationRequest createDto) {
        ReservationTime reservationTime = getReservationTime(createDto.timeId());
        Reservation reservation = createDto.toDomain(reservationTime);
        validateDateAndTime(reservation, reservationTime);

        Reservation createdReservation = reservationRepository.create(reservation);
        return ReservationResponse.from(createdReservation);
    }

    private ReservationTime getReservationTime(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당되는 예약 시간이 없습니다."));
    }

    private void validateDateAndTime(Reservation reservation, ReservationTime reservationTime) {
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDate reservationDate = reservation.getDate();
        LocalTime reservationStartAt = reservationTime.getStartAt();

        if (reservationDate.isBefore(nowDate)) {
            throw new IllegalStateException("예약 날짜는 오늘보다 이전일 수 없습니다.");
        }
        if (reservationDate.isEqual(nowDate) && reservationStartAt.isBefore(nowTime)) {
            throw new IllegalStateException("예약 시간은 현재 시간보다 이전일 수 없습니다.");
        }
        if (reservationRepository.hasDuplicateDateTimeReservation(reservation)) {
            throw new IllegalStateException("해당 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
