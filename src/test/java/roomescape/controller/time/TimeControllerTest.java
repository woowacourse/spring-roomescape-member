package roomescape.controller.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.controller.ControllerTest;
import roomescape.service.TimeService;
import roomescape.service.exception.TimeDuplicatedException;
import roomescape.service.exception.TimeUsedException;

import java.net.URI;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TimeController.class)
class TimeControllerTest extends ControllerTest {

    @Autowired
    TimeController timeController;

    @MockBean
    TimeService timeService;

    @Test
    @DisplayName("예약 시간 목록을 요청하면 200 과 예약 시간 리스트를 응답한다.")
    void getTimes200AndTimes() throws Exception {
        // given
        final List<TimeResponse> expectResponses = List.of(
                new TimeResponse(1L, "08:00", false),
                new TimeResponse(2L, "09:10", false)
        );
        final String expectJson = objectMapper.writeValueAsString(expectResponses);

        Mockito.when(timeService.getTimes())
                .thenReturn(expectResponses);

        // when & then
        mvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectJson));
    }

    @Test
    @DisplayName("해당 날짜와 테마에 예약 가능한 시간 리스트를 요청하면 200 과 예약 시간 리스트를 응답한다.")
    void getTimes200AndTimesWithBooked() throws Exception {
        // given
        final List<TimeResponse> expectResponses = List.of(
                new TimeResponse(1L, "08:00", true),
                new TimeResponse(2L, "09:10", false),
                new TimeResponse(3L, "10:20", true)
        );
        final String expectJson = objectMapper.writeValueAsString(expectResponses);

        final String date = "2024-05-05";
        final long themeId = 1L;
        final URI uri = UriComponentsBuilder.fromPath("/times")
                .queryParam("date", date)
                .queryParam("themeId", themeId)
                .build().toUri();

        Mockito.when(timeService.getTimesWithBooked(date, themeId))
                .thenReturn(expectResponses);

        // when & then
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(expectJson));
    }

    @Test
    @DisplayName("예약 시간 추가하면 201 을 응답한다.")
    void addTime201AndLocation() throws Exception {
        // given
        final String expectLocation = "/reservations/1";
        final TimeResponse response = new TimeResponse(1L, "08:00", false);
        final TimeRequest request = new TimeRequest(response.startAt());

        final String requestJson = objectMapper.writeValueAsString(request);
        final String responseJson = objectMapper.writeValueAsString(response);

        Mockito.when(timeService.addTime(request))
                .thenReturn(response);

        // when & then
        mvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", expectLocation))
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("존재하는 예약 시간을 추가 하면 409 를 응답한다.")
    void addTime409() throws Exception {
        // given
        final TimeRequest request = new TimeRequest("08:00");
        final String requestJson = objectMapper.writeValueAsString(request);

        final String message = "이미 존재하는 예약 시간 입니다.";
        Mockito.when(timeService.addTime(request))
                .thenThrow(new TimeDuplicatedException(message));

        // when & then
        mvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString(message)));
    }

    @Test
    @DisplayName("존재하는 예약 시간을 삭제하면 204 를 응답한다.")
    void deleteTime204() throws Exception {
        // given
        Mockito.when(timeService.deleteTime(1L))
                .thenReturn(1);

        // when & then
        mvc.perform(delete("/times/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제하면 404 를 응답한다.")
    void deleteTime404() throws Exception {
        // given
        Mockito.when(timeService.deleteTime(1L))
                .thenReturn(0);

        // when & then
        mvc.perform(delete("/times/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("예약된 시간을 삭제 요청하면 400 을 응답한다.")
    void methodName() throws Exception {
        // given
        final String message = "예약된 시간은 삭제할 수 없습니다.";
        Mockito.when(timeService.deleteTime(1L))
                .thenThrow(new TimeUsedException(message));

        // when & then
        mvc.perform(delete("/times/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(message)));
    }
}
