package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SecurityAccessTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // 1. Kiểm thử truy cập công khai (Khách vãng lai gọi trang chủ)
    @Test
    public void givenUnauthenticatedUser_whenGetHome_thenSuccess() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    // 2. Kiểm thử khách vãng lai gọi trang Admin bị chuyển hướng (Redirect -> Login)
    @Test
    public void givenUnauthenticatedUser_whenGetAdmin_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection());
    }

    // 3. Kiểm thử phân quyền: Người dùng role USER truy cập trang Admin sẽ bị tước quyền (403)
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void givenUserRole_whenGetAdmin_thenForbidden() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    // 4. Kiểm thử phân quyền: Người dùng role ADMIN truy cập trang Admin sẽ thành công (200)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenAdminRole_whenGetAdmin_thenSuccess() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    // 5. Kiểm thử trang Products công khai
    @Test
    public void givenUnauthenticatedUser_whenGetProducts_thenSuccess() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    // 6. Kiểm thử chức năng Search hoạt động (trả về 200)
    @Test
    public void givenSearchQuery_whenGetProducts_thenSuccess() throws Exception {
        mockMvc.perform(get("/products").param("search", "Laptop"))
                .andExpect(status().isOk());
    }

    // 7. Kiểm thử XSS payload qua search — ứng dụng vẫn trả 200 (lỗ hổng cố ý)
    @Test
    public void givenXssPayload_whenSearch_thenStillReturns200() throws Exception {
        mockMvc.perform(get("/products").param("search", "<script>alert(1)</script>"))
                .andExpect(status().isOk());
    }
}
