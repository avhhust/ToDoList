package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
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
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ToDoControllerTests {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ToDoService todoService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Transactional
    @Test
    public void createToDoTest() throws Exception {
        logger.info("run create ToDo Test");
        mockMvc.perform(MockMvcRequestBuilders.post("/todos/create/users/4")
                        .param("title", "write"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
    @Transactional
    @Test
    public void readToDoTest() throws Exception {
        logger.info("run read ToDo test");
        ToDo expected =todoService.readById(9L);
        List<Task> tasks = taskService.getByTodoId(9L);
        List<User> users = userService.getAll().stream()
                .filter(user -> user.getId() != expected.getOwner().getId()).collect(Collectors.toList());
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/9/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo"))
                .andExpect(MockMvcResultMatchers.model().attribute("todo",expected))
                .andExpect(MockMvcResultMatchers.model().attributeExists("tasks"))
                .andExpect(MockMvcResultMatchers.model().attribute("tasks",tasks))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.model().attribute("users",users));
    }

    @Transactional
    @Test
    public void updateToDoTest() throws Exception {
        logger.info("run update ToDo test");
        mockMvc.perform(MockMvcRequestBuilders.post("/todos/8/update/users/4")
                        .param("id", "8")
                        .param("title", "gggg"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/all/users/4"));
    }

    @Test
    public void deleteTest() throws Exception {
        logger.info("run delete  ToDo test");
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/9/delete/users/4"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/all/users/4"));
    }

    @Transactional
    @Test
    public void getAllTest() throws Exception {
        logger.info("run getAll test");
        List<ToDo> todos = todoService.getByUserId(6L);

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/all/users/6")
                .param("user_id", "6"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todos"))
                .andExpect(MockMvcResultMatchers.model().attribute("todos",todos))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user",userService.readById(6L)));
    }

    @Transactional
    @Test
    public void addCollaboratorTest() throws Exception {
        logger.info("run addCollaborator test");
        ToDo todo = todoService.readById(9L);
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/9/add")
                .param("id", "9")
                .param("user_id", "6"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/9/tasks"));
    }

    @Transactional
    @Test
    public void removeCollaboratorTest() throws Exception {
        logger.info("run removeCollaborator test");

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/9/remove")
                        .param("id", "9")
                        .param("user_id", "6"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/9/tasks"));
    }

}
