package roomescape.support;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class SimpleMockMvc {
    public static ResultActions post(MockMvc mockMvc, String uri, String content, Object... pathVariables)
            throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(uri, pathVariables)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
    }

    public static ResultActions get(MockMvc mockMvc, String uri, Object... pathVariables) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.get(uri, pathVariables)
        );
    }

    public static ResultActions delete(MockMvc mockMvc, String uri, Object... pathVariables) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.delete(uri, pathVariables)
        );
    }
}
