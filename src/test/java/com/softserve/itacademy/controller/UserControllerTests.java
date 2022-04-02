package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerTests {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Test
    public void createTest() throws Exception {
        logger.info("run create test");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .param("firstName", "Bogdan")
                        .param("lastName", "Ivanov")
                        .param("email", "iii@or.com")
                        .param("password", "32yyWas"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Transactional
    @Test
    public void updateTest() throws Exception {
        logger.info("run update test2");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/5/update")
                        .param("id", "6")
                        .param("firstName", "Nora")
                        .param("lastName", "White")
                        .param("email", "nora@mail.com")
                        .param("password", "32yyWas")
                        .param("roleId", "2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Transactional
    @Test
    public void getAllTest() throws Exception {
        logger.info("run getAll test");
        List<User> expected = userService.getAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/users/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.model().attribute("users",expected));
    }

    @Transactional
    @Test
    public void readTest() throws Exception {
        logger.info("run read test");
        User expected = userService.readById(6L);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/6/read"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user",expected));
    }

    @Test
    public void deleteTest() throws Exception {
        logger.info("run delete test");
        mockMvc.perform(MockMvcRequestBuilders.get("/users/5/delete"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

}
