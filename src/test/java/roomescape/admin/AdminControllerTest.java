package roomescape.admin;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import roomescape.admin.controller.AdminController;
import roomescape.auth.service.AuthService;


@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;

    @Test
    void admin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    void reservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/reservation"))
                .andExpect(status().isOk());
    }

    @Test
    void time() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/time"))
                .andExpect(status().isOk());
    }

    @Test
    void theme() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/theme"))
                .andExpect(status().isOk());
    }
}
