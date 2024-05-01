package roomescape.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.app.ReservationAppRequest;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public Reservation save(ReservationAppRequest request) {
        try {
            // TODO : method 분리
            // TODO : 예약 커스터마이즈 -> 구체화

            // 날짜 검증 -- 포맷
            LocalDate date = LocalDate.parse(request.date());
            // 예약 데이터 검증 -- 포맷
            ReservationTime time = reservationTimeRepository.findById(request.timeId());
            Reservation newReservation = new Reservation(request.name(), date, time);
            // 과거 예약 검증
            if (date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException();
            }
            if (date.isEqual(LocalDate.now()) && time.isBeforeNow()) {
                throw new IllegalArgumentException();
            }
            // 중복 데이터 검증
            long dataCount = reservationRepository.countByDateAndTimeId(date, request.timeId());
            if (dataCount > 0) {
                throw new IllegalArgumentException("예약이 이미 존재합니다.");
            }

            return reservationRepository.save(newReservation);
        } catch (EmptyResultDataAccessException | DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("잘못된 예약 포맷을 입력했습니다.");
        }
    }

    public int delete(Long id) {
        return reservationRepository.deleteById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
}
