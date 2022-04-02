package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Test
    public void homepageGet() throws Exception {
        Role role = new Role();
        role.setName("Role");

        User user = new User(),
                user2 = new User();
        user.setId(1L);
        user.setFirstName("Name");
        user.setLastName("Surname");
        user.setEmail("user@mail.com");
        user.setPassword("1234Ab");
        user.setRole(role);

        user2.setId(2L);
        user2.setFirstName("Name");
        user2.setLastName("Surname");
        user2.setEmail("usertwo@mail.com");
        user2.setPassword("1234Ab");
        user2.setRole(role);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        Mockito.when(userService.getAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET,"/home"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("users",users));
        Mockito.verify(userService,Mockito.times(1)).getAll();
    }
}
