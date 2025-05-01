package roomescape.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.util.DateTime;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

@Service
public class ReservationService {

    private final DateTime dateTime;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            DateTime dateTime,
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.dateTime = dateTime;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse createReservation(final ReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId());
        Theme theme = themeRepository.findById(request.themeId());

        LocalDateTime now = dateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(request.date(), time.getStartAt());

        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("예약할 수 없는 날짜와 시간입니다.");
        }

        if (reservationRepository.existBy(request.themeId(), request.date(), time.getStartAt())) {
            throw new IllegalArgumentException("이미 예약이 존재합니다.");
        }

        // TODO: save -> findById 흐름의 아이디 부여 로직 변경
        Long id = reservationRepository.save(new Reservation(null, request.name(), request.date(), time, theme));
        Reservation findReservation = new Reservation(id, request.name(), request.date(), time, theme);

        return ReservationResponse.from(findReservation);
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteReservationById(final Long id) {
        // TODO: 메서드명 변경 (why? count 인것을 특정할 수 없다.)
        int count = reservationRepository.deleteById(id);
        validateExistIdToDelete(count);
    }

    private void validateExistIdToDelete(int count) {
        if (count == 0) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 예약입니다.");
        }
    }
}
