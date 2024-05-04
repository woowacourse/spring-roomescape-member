package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.DuplicateSaveException;
import roomescape.exception.IllegalReservationDateException;
import roomescape.exception.NoSuchRecordException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> findAllReservation() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse saveReservation(ReservationAddRequest request) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(request.date(), request.timeId(),
                request.themeId())) {
            throw new DuplicateSaveException("중복되는 예약이 존재합니다");
        }

        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        Reservation reservation = request.toReservation(reservationTime, theme);
        if (reservation.isDateBefore(LocalDate.now())) {
            throw new IllegalReservationDateException(reservation.getDate() + ": 예약 날짜는 현재 보다 이전일 수 없습니다");
        }
        Reservation saved = reservationRepository.save(reservation);
        return new ReservationResponse(saved);
    }

    private Theme getTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new NoSuchRecordException("해당하는 테마가 존재하지 않습니다 ID: " + themeId));
    }

    private ReservationTime getReservationTime(long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NoSuchRecordException("해당하는 예약시간이 존재하지 않습니다 ID: " + timeId));
    }

    public void removeReservation(long id) {
        reservationRepository.deleteById(id);
    }
}
