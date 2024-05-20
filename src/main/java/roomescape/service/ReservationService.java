package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.exception.CustomBadRequest;
import roomescape.service.dto.input.ReservationInput;
import roomescape.service.dto.output.ReservationOutput;
import roomescape.service.util.DateTimeFormatter;

@Service
public class ReservationService {

    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final MemberService memberService;
    private final ReservationDao reservationDao;
    private final DateTimeFormatter dateTimeFormatter;

    public ReservationService(final ReservationTimeService reservationTimeService,
                              final ThemeService themeService,
                              final MemberService memberService,
                              final ReservationDao reservationDao,
                              final DateTimeFormatter dateTimeFormatter) {
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.memberService = memberService;
        this.reservationDao = reservationDao;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public ReservationOutput createReservation(final ReservationInput input) {
        final var member = memberService.getMemberById(input.memberId());
        final var time = reservationTimeService.getReservationTimeById(input.timeId());
        final var theme = themeService.getThemeById(input.themeId());

        final var reservation = new Reservation(member, input.date(), time, theme);

        validatePastDateAndTime(reservation);

        final var savedReservation = reservationDao.create(reservation);
        return ReservationOutput.from(savedReservation);
    }

    private void validatePastDateAndTime(final Reservation reservation) {
        if (reservation.isBefore(dateTimeFormatter.getDate(), dateTimeFormatter.getTime())) {
            throw new CustomBadRequest("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    private Reservation getReservationById(final Long reservationId) {
        return reservationDao.findById(reservationId)
                .orElseThrow(() -> new CustomBadRequest(String.format("reservationId(%s)가 존재하지 않습니다.", reservationId)));
    }

    public List<ReservationOutput> filterReservations(final Long themeId,
                                                      final Long memberId,
                                                      final String dateFrom,
                                                      final String dateTo) {
        final var reservations = reservationDao.filter(themeId, memberId, dateFrom, dateTo);
        return ReservationOutput.list(reservations);
    }

    public void deleteReservation(final long id) {
        final var reservation = getReservationById(id);
        reservationDao.delete(reservation.getId());
    }
}
