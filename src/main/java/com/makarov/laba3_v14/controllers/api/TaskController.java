package com.makarov.laba3_v14.controllers.api;

import com.makarov.laba3_v14.enums.Status;
import com.makarov.laba3_v14.models.Task;
import com.makarov.laba3_v14.models.User;
import com.makarov.laba3_v14.repositories.TaskRepository;
import com.makarov.laba3_v14.repositories.UserRepository;
import com.makarov.laba3_v14.responses.error.TaskNotFoundException;
import com.makarov.laba3_v14.responses.success.Response;
import com.makarov.laba3_v14.services.UserDetailsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TaskController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public TaskController(
        TaskRepository taskRepository,
        UserRepository userRepository,
        UserDetailsService userDetailsService
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<?> getAllTasks() {
        User currentUser = userDetailsService.getCurrentUser();
        List<Task> tasks = taskRepository.findAllByAssignToOrOwner(currentUser, currentUser);

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/api/tasks/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        try {
            User currentUser = userDetailsService.getCurrentUser();

            Task task = taskRepository.findByAssignToAndIdOrOwnerAndId(currentUser, id, currentUser, id);

            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Task not found"));
        }
    }

    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Requested task not found"));

            taskRepository.delete(task);

            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Task not found"));
        }
    }

    @PutMapping("/api/tasks/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @RequestParam(value = "header", required = false) String header,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "assign_to", required = false) Long assignTo,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "deadline", required = false) String deadline
    ) {
        try {
            Task taskToUpdate = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Requested task not found"));

            User currentUser = userDetailsService.getCurrentUser();
            if (!currentUser.equals(taskToUpdate.getOwner())) {
                return new ResponseEntity<>(new Response("You do not have permission to update this task"), HttpStatus.FORBIDDEN);
            }

            if (header != null) {
                taskToUpdate.setHeader(header);
            }
            if (description != null) {
                taskToUpdate.setDescription(description);
            }
            if (deadline != null) {
                taskToUpdate.setDeadline(LocalDate.parse(deadline));
            }

            if (assignTo != null) {
                User assignee = userRepository.findById(assignTo).orElseThrow(() -> new EntityNotFoundException("User not found"));
                taskToUpdate.setAssignTo(assignee);
            }

            if (status != null) {
                taskToUpdate.setStatus(status);
            }

            Task updatedTask = taskRepository.save(taskToUpdate);

            return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Task not found"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("User not found"));
        }
    }

    @PostMapping("/api/tasks/create")
    public ResponseEntity<?> createTask (
        @RequestParam("header") String header,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "assign_to", required = false) Long assignTo
    ) {
        if (assignTo == null) {
            assignTo = this.userDetailsService.getCurrentUser().getId();
        }

        try {
            Task task = new Task();
            task.setHeader(header);
            task.setDescription(description);
            task.setOwner(this.userDetailsService.getCurrentUser());
            task.setAssignTo(this.userRepository.findById(assignTo).orElseThrow(() -> new EntityNotFoundException("User not found")));
            task.setStatus(Status.BACKLOG);

            return ResponseEntity.status(HttpStatus.OK).body(this.taskRepository.save(task));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("User not found"));
        }
    }
}
