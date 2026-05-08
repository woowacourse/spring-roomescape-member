package roomescape.view;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationTime.ReservationTime;

import java.util.List;

public class OutputView {
    private static final String RESERVATION_LIST_TITLE = "전체 예약 조회";
    private static final String RESERVATION_ADDED_MSG = "다음 예약 데이터가 추가되었습니다.";
    private static final String RESERVATION_DELETED_MSG = "해당 예약 데이터가 삭제되었습니다.";

    private static final String TIME_LIST_TITLE = "전체 예약 시간 조회";
    private static final String TIME_ADDED_MSG = "다음 예약 시간 데이터가 추가되었습니다.";
    private static final String TIME_DELETED_MSG = "해당 예약 시간 데이터가 삭제되었습니다.";

    private static final String RESERVATION_FORMAT = "id: %d, name: %s, date: %s, timeId: %d, time: %s";
    private static final String TIME_FORMAT = "id: %d, startAt: %s";

    public static void printReservations(List<Reservation> reservations) {
        printList(RESERVATION_LIST_TITLE, reservations, OutputView::formatReservation);
    }

    public static void printAddedReservation(Reservation reservation) {
        printSingle(RESERVATION_ADDED_MSG, formatReservation(reservation));
    }

    public static void printDeleteReservationComplete() {
        printMessageWithSpacing(RESERVATION_DELETED_MSG);
    }

    public static void printReservationTimes(List<ReservationTime> reservationTimes) {
        printList(TIME_LIST_TITLE, reservationTimes, OutputView::formatTime);
    }

    public static void printAddedReservationTime(ReservationTime reservationTime) {
        printSingle(TIME_ADDED_MSG, formatTime(reservationTime));
    }

    public static void printDeleteReservationTimeComplete() {
        printMessageWithSpacing(TIME_DELETED_MSG);
    }

    private static <T> void printList(String title, List<T> items, java.util.function.Function<T, String> formatter) {
        System.out.println(title);
        System.out.println();
        if(items.isEmpty()) {
            System.out.println("값이 없습니다.");
            System.out.println();
            return;
        }
        items.forEach(item -> System.out.println(formatter.apply(item)));
        System.out.println();
    }

    private static void printSingle(String message, String formattedContent) {
        System.out.println(message);
        System.out.println();
        System.out.println(formattedContent);
        System.out.println();
    }

    private static void printMessageWithSpacing(String message) {
        System.out.println(message);
        System.out.println();
    }

    private static String formatReservation(Reservation res) {
        return String.format(RESERVATION_FORMAT,
                res.id(), res.name(), res.date(), res.time().id(), res.time().startAt());
    }

    private static String formatTime(ReservationTime time) {
        return String.format(TIME_FORMAT, time.id(), time.startAt());
    }
}
