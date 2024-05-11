package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDAO;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.dao.ThemeDAO;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;
    private final ThemeDAO themeDAO;
    private final MemberDAO memberDAO;

    public ReservationService(final ReservationDAO reservationDAO, final ReservationTimeDAO reservationTimeDAO,
                              final ThemeDAO themeDAO, final MemberDAO memberDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
        this.themeDAO = themeDAO;
        this.memberDAO = memberDAO;
    }

    public Reservation save(final ReservationRequest reservationRequest) {
        final LocalDate requestReservationDate = reservationRequest.date();
        final ReservationTime requestReservationTime = reservationTimeDAO.findById(reservationRequest.timeId());

        validateReservation(requestReservationDate, requestReservationTime);

        final Theme theme = themeDAO.findById(reservationRequest.themeId());
        final Member member = memberDAO.findById(reservationRequest.memberId());
        final Reservation reservation = reservationRequest.toEntity(requestReservationTime, theme, member);

        return reservationDAO.insert(reservation);
    }

    private void validateReservation(final LocalDate requestReservationDate, final ReservationTime requestReservationTime) {
        validateDuplicatedReservation(requestReservationDate, requestReservationTime);
        validatePast(requestReservationDate, requestReservationTime);
    }

    private void validateDuplicatedReservation(final LocalDate requestReservationDate, final ReservationTime requestReservationTime) {
        if (reservationDAO.existReservationOf(requestReservationDate, requestReservationTime)) {
            throw new IllegalStateException("해당 날짜와 시간에 예약이 이미 존재합니다.");
        }
    }

    private void validatePast(final LocalDate requestReservationDate, final ReservationTime requestReservationTime) {
        final LocalDate today = LocalDate.now();
        final LocalTime reservationTime = requestReservationTime.getStartAt();

        if (requestReservationDate.isBefore(today)) {
            throw new IllegalArgumentException("지나간 날짜에 예약을 등록할 수 없습니다.");
        }
        if (requestReservationDate.isEqual(today) && reservationTime.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("지나간 시간에 예약을 등록할 수 없습니다.");
        }
    }

    public List<Reservation> findAll() {
        return reservationDAO.selectAll();
    }

    public void delete(final long id) {
        reservationDAO.deleteById(id);
    }
}
