package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.AvailableReservationTimeResponse;
import roomescape.controller.dto.response.ReservationTimeResponse;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.ReservationTime;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.exception.custom.PharmaceuticalViolationException;
import roomescape.service.dto.ReservationTimeCreation;

@Service
public class ReservationTimeService {

    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationTimeService(final ReservationDAO reservationDAO,
                                  final ReservationTimeDAO reservationTimeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public ReservationTimeResponse addReservationTime(final ReservationTimeCreation creation) {
        if (reservationTimeDAO.existsByStartAt(creation.startAt())) {
            throw new ExistedDuplicateValueException("이미 존재하는 예약 가능 시간입니다: %s".formatted(creation.startAt()));
        }
        final ReservationTime reservationTime = new ReservationTime(creation.startAt());
        final long id = reservationTimeDAO.insert(reservationTime);

        final ReservationTime savedReservationTime = findById(id);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    private ReservationTime findById(final long id) {
        return reservationTimeDAO.findById(id)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 예약 시간입니다"));
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeDAO.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAllAvailableTime(final LocalDate date, final long themeId) {
        List<ReservationTime> totalReservationTime = reservationTimeDAO.findAll();
        List<ReservationTime> bookedTime = reservationTimeDAO.findAllBookedTime(date, themeId);
        List<AvailableReservationTimeResponse> responses = new ArrayList<>();

        for (ReservationTime reservationTime : totalReservationTime) {
            if (bookedTime.contains(reservationTime)) {
                responses.add(AvailableReservationTimeResponse.from(reservationTime, true));
                continue;
            }
            responses.add(AvailableReservationTimeResponse.from(reservationTime, false));
        }
        return responses;
    }

    public void deleteById(final long id) {
        if (reservationDAO.existsByTimeId(id)) {
            throw new PharmaceuticalViolationException("사용 중인 예약 시간입니다");
        }

        boolean deleted = reservationTimeDAO.deleteById(id);

        if (!deleted) {
            throw new NotExistedValueException("존재하지 않는 예약 시간입니다");
        }
    }
}
