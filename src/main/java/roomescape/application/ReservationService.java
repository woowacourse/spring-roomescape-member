package roomescape.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.dto.request.ReservationRequest;
import roomescape.application.dto.response.ReservationResponse;
import roomescape.application.exception.DuplicatedEntityException;
import roomescape.application.exception.EntityNotFoundException;
import roomescape.application.exception.ReserveOnPastException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              Clock clock) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 테마 입니다."));
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 예약 시간 입니다."));

        if (reservationRepository.existsBy(request.parsedDate(), request.timeId(), request.themeId())) {
            throw new DuplicatedEntityException("이미 존재하는 예약입니다."); 
        }
        Reservation reservation = request.toReservation(reservationTime, theme);
        if (reservation.isBefore(LocalDateTime.now(clock))) {
            throw new ReserveOnPastException("현재 시간보다 과거로 예약할 수 없습니다.");
        }
        return ReservationResponse.from(reservationRepository.create(reservation));
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 예약 입니다."));
        reservationRepository.deleteById(reservation.getId());
    }
}
