package com.softserve.itacademy.service;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import com.softserve.itacademy.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;


import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@SpringBootTest
public class TaskServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private State state = new State();

    private Task task = new Task();

    @BeforeEach
    public void init() {
        state.setName("New");
        task.setId(1L);
        task.setName("Task #1");
        task.setPriority(Priority.LOW);
        task.setState(state);

        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    }

    @Test
    public void createValidUser() {
        Mockito.when(taskRepository.save(task)).thenReturn(task);

        Assertions.assertEquals(task,taskService.create(task));
        Mockito.verify(taskRepository,Mockito.times(1)).save(task);
    }

    @Test
    public void createInvalidUser() {
        Mockito.when(taskRepository.save(task)).thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(NullEntityReferenceException.class, () -> { taskService.create(task); });
        Mockito.verify(taskRepository,Mockito.times(1)).save(task);
    }

    @Test
    public void readValidId() {
        Assertions.assertEquals(task,taskService.readById(1L));
        Mockito.verify(taskRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    public void readInvalidId() {
        Mockito.when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> { taskService.readById(2L); });
        Mockito.verify(taskRepository,Mockito.times(1)).findById(2L);
    }

    @Test
    public void updateValidTask() {
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setName("Task #2");
        updatedTask.setPriority(Priority.MEDIUM);
        updatedTask.setState(state);

        Mockito.when(taskRepository.save(updatedTask)).thenReturn(updatedTask);
        Assertions.assertEquals(updatedTask,taskService.update(updatedTask));
        Mockito.verify(taskRepository,Mockito.times(1)).save(updatedTask);
    }

    @Test
    public void updateInvalidTask() {
        Mockito.when(taskRepository.findById(2L)).thenReturn(Optional.empty());
        Task updatedTask = new Task();
        updatedTask.setId(2L);
        updatedTask.setName("Task #2");
        updatedTask.setPriority(Priority.MEDIUM);
        updatedTask.setState(state);

        Assertions.assertThrows(EntityNotFoundException.class, () -> { taskService.update(updatedTask); });
        Mockito.verify(taskRepository,Mockito.times(1)).findById(2L);
    }

    @Test
    public void updateNullTask() {
        Assertions.assertThrows(NullEntityReferenceException.class, () -> { taskService.update(null); });
    }

    @Test
    public void deleteValidTask() {
        Assertions.assertDoesNotThrow(() -> { taskService.delete(1L); });
        Mockito.verify(taskRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    public void deleteInvalidTask() {
        Mockito.when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> { taskService.delete(2L); });
        Mockito.verify(taskRepository,Mockito.times(1)).findById(2L);
    }

    @Test
    public void getAll() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        Mockito.when(taskRepository.findAll()).thenReturn(tasks);

        Assertions.assertEquals(tasks,taskService.getAll());
        Mockito.verify(taskRepository,Mockito.times(1)).findAll();
    }

    @Test
    public void getAllEmptyList() {
        Mockito.when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        Assertions.assertTrue(taskService.getAll().isEmpty());
        Mockito.verify(taskRepository,Mockito.times(1)).findAll();
    }


    @Test
    public void getByTodoId() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        Mockito.when(taskRepository.getByTodoId(1L)).thenReturn(tasks);

        Assertions.assertEquals(tasks,taskService.getByTodoId(1L));
        Mockito.verify(taskRepository,Mockito.times(1)).getByTodoId(1L);
    }

    @Test
    public void getByTodoIdEmptyList() {
        Mockito.when(taskRepository.getByTodoId(1L)).thenReturn(new ArrayList<>());

        Assertions.assertTrue(taskService.getByTodoId(1L).isEmpty());
        Mockito.verify(taskRepository,Mockito.times(1)).getByTodoId(1L);
    }
}
