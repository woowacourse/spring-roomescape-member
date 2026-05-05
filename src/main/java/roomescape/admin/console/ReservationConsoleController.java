package roomescape.admin.console;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import roomescape.global.auth.Accessor;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.command.ReservationCommand;
import roomescape.service.command.ReservationTimeCommand;

@RequiredArgsConstructor
public class ReservationConsoleController implements CommandLineRunner {

    private static final Accessor ADMIN_ROLE = new Accessor("ADMIN");

    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            String menu = ConsoleView.readMenu();
            if (menu.equals("q")) break;

            try {
                handleMenu(menu);
            } catch (Exception e) {
                ConsoleView.printError("에러 발생: " + e.getMessage());
            }
        }
        ConsoleView.printMessage("프로그램을 종료합니다.");
        System.exit(0);
    }

    private void handleMenu(String menu) {
        switch (menu) {
            case "1" -> printAllReservations();
            case "2" -> reserve();
            case "3" -> cancelAllReservation();
            case "4" -> printAllReservationTimes();
            case "5" -> registerReservationTime();
            case "6" -> removeReservationTime();
            default -> printInvalidMenu();
        }
    }

    private void printAllReservations() {
        ConsoleView.printReservations(reservationService.getAllReservations(ADMIN_ROLE));
    }

    private void reserve() {
        String name = ConsoleView.readInput("이름: ");
        String date = ConsoleView.readInput("날짜(YYYY-MM-DD): ");
        Long timeId = Long.parseLong(ConsoleView.readInput("시간 ID: "));

        // 테마 선택은 콘솔에 반영하지 않을 것임.
        reservationService.reserve(ADMIN_ROLE, new ReservationCommand(name, LocalDate.parse(date), 1L, timeId));
        ConsoleView.printMessage("예약이 완료되었습니다.");
    }

    private void cancelAllReservation() {
        long id = Long.parseLong(ConsoleView.readInput("삭제할 예약 ID: "));
        reservationService.cancelReservation(ADMIN_ROLE, id);
        ConsoleView.printMessage("예약이 삭제되었습니다.");
    }

    private void printAllReservationTimes() {
        ConsoleView.printTimes(reservationTimeService.getAllReservationTimes());
    }

    private void registerReservationTime() {
        String startAt = ConsoleView.readInput("추가할 시간(HH:mm): ");
        reservationTimeService.register(new ReservationTimeCommand(LocalTime.parse(startAt)));
        ConsoleView.printMessage("시간이 등록되었습니다.");
    }

    private void removeReservationTime() {
        long id = Long.parseLong(ConsoleView.readInput("삭제할 시간 ID: "));
        reservationTimeService.remove(id);
        ConsoleView.printMessage("시간이 삭제되었습니다.");
    }

    private void printInvalidMenu() {
        ConsoleView.printError("잘못된 선택입니다.");
    }

}
