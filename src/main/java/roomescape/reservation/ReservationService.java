package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.globalexception.BadRequestException;
import roomescape.globalexception.ConflictException;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(
            @Autowired final ReservationRepository reservationRepository,
            @Autowired final ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse create(final ReservationRequest request) {
        validateDuplicateDateTime(request.timeId(), request.date());
        validatePastDateTime(request);

        final Reservation reservation = new Reservation(request.name(), request.date());
        try {
            final long id = reservationRepository.save(reservation, request.timeId(), request.themeId());
            final Reservation savedReservation = reservationRepository.findById(id);
            return ReservationResponse.from(savedReservation);
        } catch (final DataIntegrityViolationException e){
            throw new BadRequestException("시간 또는 테마가 존재하지 않습니다.");
        }
    }

    public List<ReservationResponse> readAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(final Long id) {
        reservationRepository.delete(id);
    }

    private void validatePastDateTime(final ReservationRequest request) {
        final LocalDate today = LocalDate.now();
        final LocalDate reservationDate = request.date();
        if (reservationDate.isBefore(today)) {
            throw new BadRequestException("지난 날짜로 예약할 수 없습니다.");
        }
        if (reservationDate.isEqual(today)) {
            validatePastTime(request);
        }
    }

    private void validatePastTime(final ReservationRequest request) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId());
        if (reservationTime.getStartAt().isBefore(LocalTime.now())) {
            throw new BadRequestException("지난 시간으로 예약할 수 없습니다.");
        }
    }

    private void validateDuplicateDateTime(final Long reservationTimeId, final LocalDate date) {
        if (reservationRepository.existsByReservationTimeAndDate(reservationTimeId, date)) {
            throw new ConflictException("이미 예약이 존재합니다.");
        }
    }
}
