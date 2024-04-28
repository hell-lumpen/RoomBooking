//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mai.roombooking.dtos.UserDTO;
//import org.mai.roombooking.entities.User;
//import org.mai.roombooking.entities.UserInfo;
//import org.mai.roombooking.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    UserService userService;
//
//    private User getAdmin() {
//        var adminInfo = UserInfo.builder()
//                .role(User.UserRole.ADMINISTRATOR)
//                .phoneNumber("+79297016628")
//                .isAccountLocked(false)
//                .password("p")
//                .username("u")
//                .build();
//        return userService.updateUser(User.builder().fullName("Artem").info(adminInfo).build());
//    }
//    private User getAuthorised() {
//        var authorisedInfo = UserInfo.builder()
//                .role(User.UserRole.AUTHORISED)
//                .phoneNumber("+79297016629")
//                .isAccountLocked(false)
//                .password("p1")
//                .username("u1")
//                .build();
//        return userService.updateUser(User.builder().fullName("Artem2").info(authorisedInfo).build());
//    }
//
//    private User getTeacher() {
//        var teacherInfo = UserInfo.builder()
//                .role(User.UserRole.TEACHER)
//                .phoneNumber("+79297016621")
//                .isAccountLocked(false)
//                .password("p2")
//                .username("u2")
//                .build();
//        return userService.updateUser(User.builder().fullName("Artem3").info(teacherInfo).build());
//    }
//
//    @Test
//    public void testStatus1() throws Exception {
//        User user = getAdmin();
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/bookings/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(request -> {
//                    request.setRemoteUser("rgre"); // Simulate authentication
//                    return request;
//                }))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Expected Response"));
//    }
//
//    @Test
//    public void testPostEndpoint() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/endpoint")
//                        .contentType("application/json")
//                        .content("{\"key\":\"value\"}"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.key").value("value"));
//    }
//
//
//}
