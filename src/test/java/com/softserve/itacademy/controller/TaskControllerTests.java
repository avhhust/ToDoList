package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import org.junit.jupiter.api.BeforeEach;
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
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private ToDoService toDoService;

    @MockBean
    private StateService stateService;

    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    private ToDo toDo = new ToDo();

    @BeforeEach
    public void init() {
        Mockito.when(toDoService.readById(1L)).thenReturn(toDo);
    }

    @Test
    public void createGet() throws Exception {
        logger.info("TEST: \"Create valid task. HTTPMethod: GET.\"");
        ToDo expected = toDo;

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET,"/tasks/create/todos/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("task"))
                .andExpect(MockMvcResultMatchers.model().attribute("todo",expected))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities", Priority.values()));
        Mockito.verify(toDoService,Mockito.times(1)).readById(1L);
    }

    @Test
    public void createPostValid() throws Exception {
        logger.info("TEST: \"Create valid task. HTTPMethod: POST.\"");
        TaskDto taskDto = new TaskDto();
        taskDto.setTodoId(1L);
        taskDto.setName("Task #1");
        taskDto.setPriority(Priority.LOW.toString().toUpperCase());
        State state = new State();
        state.setName("New");
        state.setId(1L);

        Mockito.when(toDoService.readById(taskDto.getId())).thenReturn(toDo);
        Mockito.when(stateService.getByName("New")).thenReturn(state);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST,"/tasks/create/todos/1")
                        .param("name", taskDto.getName())
                        .param("priority",taskDto.getPriority())
                        .param("todo_id",String.valueOf(taskDto.getTodoId())))
                        .andExpect(MockMvcResultMatchers.status().isFound())
                        .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                        .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/1/tasks"));
        Mockito.verify(toDoService,Mockito.times(1)).readById(taskDto.getId());
        Mockito.verify(stateService,Mockito.times(1)).getByName("New");
    }

    @Test
    public void createPostInvalid() throws Exception {
        logger.info("TEST: \"Create invalid task. HTTPMethod: POST.\"");
        TaskDto taskDto = new TaskDto();
        taskDto.setTodoId(1L);
        taskDto.setName("");
        taskDto.setPriority(Priority.LOW.toString().toUpperCase());
        State state = new State();
        state.setName("New");
        state.setId(1L);

        Mockito.when(toDoService.readById(1L)).thenReturn(toDo);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST,"/tasks/create/todos/1")
                        .param("name", taskDto.getName())
                        .param("priority",taskDto.getPriority())
                        .param("todo_id",String.valueOf(taskDto.getTodoId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors());
        Mockito.verify(toDoService,Mockito.times(1)).readById(1L);
    }

    @Test
    public void updateGet() throws Exception {
        logger.info("TEST: \"Update valid task. HTTPMethod: GET.\"");
        State state = new State(),
                state2 = new State();
        state.setId(1L);
        state.setName("New");
        state2.setId(2L);
        state2.setName("Done");
        List<State> states = new ArrayList<>();
        states.add(state);
        states.add(state2);

        Task task = new Task();
        task.setId(1L);
        task.setName("Task #1");
        task.setPriority(Priority.LOW);
        task.setTodo(toDo);
        task.setState(state);

        Mockito.when(taskService.readById(1L)).thenReturn(task);
        Mockito.when(stateService.getAll()).thenReturn(states);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET,"/tasks/1/update/todos/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("task"))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities",Priority.values()))
                .andExpect(MockMvcResultMatchers.model().attribute("states",states));

        Mockito.verify(taskService,Mockito.times(1)).readById(1L);
        Mockito.verify(stateService,Mockito.times(1)).getAll();
    }

    @Test
    public void updatePostValid() throws Exception {
        logger.info("TEST: \"Update valid task. HTTPMethod: POST.\"");
        State state = new State();
        state.setId(1L);
        state.setName("New");

        Task task = new Task();
        task.setId(1L);
        task.setName("Task #1");
        task.setPriority(Priority.LOW);
        task.setTodo(toDo);
        task.setState(state);
        toDo.setId(1);

        Mockito.when(toDoService.readById(1L)).thenReturn(toDo);
        Mockito.when(stateService.readById(1L)).thenReturn(state);
        Mockito.when(taskService.update(task)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST,"/tasks/1/update/todos/1")
                .param("id",String.valueOf(task.getId()))
                .param("name",task.getName())
                .param("priority",task.getPriority().toString())
                .param("todoId",String.valueOf(task.getTodo().getId()))
                .param("stateId",String.valueOf(task.getState().getId())))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/1/tasks"));

        Mockito.verify(stateService,Mockito.times(1)).readById(1L);
        Mockito.verify(toDoService,Mockito.times(1)).readById(1L);
    }

    @Test
    public void updatePostInvalid() throws Exception {
        logger.info("TEST: \"Update invalid task. HTTPMethod: POST.\"");
        State state = new State(),
                state2 = new State();
        state.setId(1L);
        state.setName("New");
        state2.setId(2L);
        state2.setName("Done");
        List<State> states = new ArrayList<>();
        states.add(state);
        states.add(state2);

        Task task = new Task();
        task.setId(1L);
        task.setName("");
        task.setPriority(Priority.LOW);
        task.setTodo(toDo);
        task.setState(state);

        Mockito.when(stateService.getAll()).thenReturn(states);

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST,"/tasks/1/update/todos/1")
                        .param("id",String.valueOf(task.getId()))
                        .param("name",task.getName())
                        .param("priority",task.getPriority().toString())
                        .param("todoId",String.valueOf(task.getTodo().getId()))
                        .param("stateId",String.valueOf(task.getState().getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.model().attribute("priorities",Priority.values()))
                .andExpect(MockMvcResultMatchers.model().attribute("states",states));

        Mockito.verify(stateService,Mockito.times(1)).getAll();
    }


    @Test
    public void deleteGet() throws Exception {
        logger.info("TEST: \"Delete valid task. HTTPMethod: GET.\"");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET,"/tasks/1/delete/todos/1"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/1/tasks"));
        Mockito.verify(taskService,Mockito.times(1)).delete(1L);
    }

    @Test
    public void deleteGetInvalid() throws Exception {
        logger.info("TEST: \"Delete invalid task. HTTPMethod: GET.\"");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET,"/tasks/255/delete/todos/1"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/1/tasks"));
        Mockito.verify(taskService,Mockito.times(1)).delete(255L);
    }
}
