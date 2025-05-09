package roomescape.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.common.exception.NotAbleReservationException;
import roomescape.common.exception.NotFoundReservationTimeException;
import roomescape.common.exception.NotFoundThemeException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    // TODO: Repository 의존이 너무 많다. 개선할 방법이 없을까?
    // todo: 왜 Repository에 의존성이 너무 많다고 생각하면 안되는지
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse createReservation(ReservationCreateRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new NotFoundReservationTimeException("올바른 예약 시간을 찾을 수 없습니다. 나중에 다시 시도해주세요."));
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NotFoundThemeException("올바른 방탈출 테마가 없습니다."));

        if (LocalDateTime.of(request.date(), reservationTime.getStartAt()).isBefore(LocalDateTime.now())) {
            throw new NotAbleReservationException("과거 시점의 예약을 할 수 없습니다.");
        }

        List<ReservationTime> availableTimes = reservationTimeRepository.findByReservationDateAndThemeId(request.date(),
                request.themeId());

        if (!availableTimes.contains(reservationTime)) {
            throw new NotAbleReservationException("이미 해당 시간과 테마에 예약이 존재하여 예약할 수 없습니다.");
        }

        Reservation reservation = reservationRepository.save(request.toReservation(reservationTime, theme));

        return ReservationResponse.from(reservation);
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }
}
