package com.makarov.laba3_v14.repositories;

import com.makarov.laba3_v14.models.Task;
import com.makarov.laba3_v14.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    ArrayList<Task> findAllByOwner (User user);

    List<Task> findAllByAssignTo (User assignTo);

    List<Task> findAllByAssignToOrOwner (User assignee, User owner);

    Task findByAssignToAndIdOrOwnerAndId (User assignee, Long assigneeId, User owner, Long ownerId);
}
