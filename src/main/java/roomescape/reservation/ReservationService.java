package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.globalexception.BadRequestException;
import roomescape.globalexception.ConflictException;
import roomescape.globalexception.NotFoundException;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.ReservationTimeRepository;
import roomescape.theme.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse create(final ReservationRequest request) {
        validateDuplicateDateTime(request.timeId(), request.date());
        validatePastDateTime(request);
        validateExistsReservationTime(request);
        validateExistsTheme(request);

        final Reservation reservation = new Reservation(request.name(), request.date());
        final long id = reservationRepository.save(reservation, request.timeId(), request.themeId());
        final Reservation savedReservation = reservationRepository.findById(id);
        return ReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> readAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(final Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NotFoundException("존재하지 않는 예약입니다.");
        }
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
        if (reservationTime.isBefore(LocalTime.now())) {
            throw new BadRequestException("지난 시간으로 예약할 수 없습니다.");
        }
    }

    private void validateDuplicateDateTime(final Long reservationTimeId, final LocalDate date) {
        if (reservationRepository.existsByReservationTimeIdAndDate(reservationTimeId, date)) {
            throw new ConflictException("이미 예약이 존재합니다.");
        }
    }

    private void validateExistsReservationTime(final ReservationRequest request) {
        if (!reservationTimeRepository.existsById(request.themeId())) {
            throw new BadRequestException("예약시간이 존재하지 않습니다.");
        }
    }

    private void validateExistsTheme(final ReservationRequest request) {
        if (!themeRepository.existsById(request.themeId())) {
            throw new BadRequestException("테마가 존재하지 않습니다.");
        }
    }
}
