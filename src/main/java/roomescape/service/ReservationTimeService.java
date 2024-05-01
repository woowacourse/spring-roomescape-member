package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.exception.time.DuplicatedTimeException;
import roomescape.exception.time.NotFoundTimeException;
import roomescape.exception.time.ReservationReferencedTimeException;
import roomescape.web.dto.ReservationTimeRequest;
import roomescape.web.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> findAllReservationTime() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<ReservationTimeResponse> findAllAvailableTime(LocalDate date, Long themeId) {
        List<Long> unavailableTimeIds = reservationRepository.findTimeIdByDateAndThemeId(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeRepository.hasNotId(unavailableTimeIds); // TODO: 이름 수정
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse saveReservationTime(ReservationTimeRequest request) {
        if (reservationTimeRepository.existsByStartAt(request.getStartAt())) {
            throw new DuplicatedTimeException();
        }
        ReservationTime reservationTime = request.toReservationTime();
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public void deleteReservationTime(Long id) {
        ReservationTime reservationTime = findReservationTimeById(id);
        try {
            reservationTimeRepository.delete(reservationTime);
        } catch (DataIntegrityViolationException e) { // TODO: 조회 쿼리로 대체하는 것도 좋을듯?
            throw new ReservationReferencedTimeException();
        }
    }

    private ReservationTime findReservationTimeById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(NotFoundTimeException::new);
    }
}
