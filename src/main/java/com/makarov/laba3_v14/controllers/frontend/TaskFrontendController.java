package com.makarov.laba3_v14.controllers.frontend;

import com.makarov.laba3_v14.models.Task;
import com.makarov.laba3_v14.repositories.TaskRepository;
import com.makarov.laba3_v14.responses.error.TaskNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskFrontendController {
    private final TaskRepository taskRepository;

    public TaskFrontendController(
            TaskRepository taskRepository
    ) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/viewTask/{id}")
    public ModelAndView viewTask (@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("viewTask");

        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Requested task not found"));

            modelAndView.addObject("task", task);

            return modelAndView;
        } catch (TaskNotFoundException e) {
            return new ModelAndView("redirect:/error");
        }
    }

    @GetMapping("/editTask/{id}")
    public ModelAndView editTask (@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("editTask");

        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Requested task not found"));

            modelAndView.addObject("task", task);

            return modelAndView;
        } catch (TaskNotFoundException e) {
            return new ModelAndView("redirect:/error");
        }
    }
}
