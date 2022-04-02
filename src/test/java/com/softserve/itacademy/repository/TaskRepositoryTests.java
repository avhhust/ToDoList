package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTests {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager manager;

    private ToDo toDo = new ToDo();

    @Test
    public void getByValidTodoId() {
        toDo.setTitle("Title");
        toDo.setCreatedAt(LocalDateTime.now());
        manager.persist(toDo);

        State state = new State();
        state.setName("Some_state");
        manager.persist(state);

        Task task = new Task(),
                task2 = new Task();
        task.setName("Task #1");
        task.setState(state);
        task.setTodo(toDo);
        task.setPriority(Priority.MEDIUM);
        task2.setName("Task #2");
        task2.setState(state);
        task2.setTodo(toDo);
        task2.setPriority(Priority.MEDIUM);
        manager.persist(task);
        manager.persist(task2);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(task2);

        List<Task> getByTodoId = taskRepository.getByTodoId(toDo.getId());
        Assertions.assertEquals(tasks.size(), getByTodoId.size());
        Assertions.assertTrue(getByTodoId.contains(task));
        Assertions.assertTrue(getByTodoId.contains(task2));
    }

    @Test
    public void getByInvalidTodoId() {
        Assertions.assertTrue(taskRepository.getByTodoId(255L).isEmpty());
    }
}
