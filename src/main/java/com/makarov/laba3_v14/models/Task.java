package com.makarov.laba3_v14.models;

import com.makarov.laba3_v14.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private String header;

    private Status status;

    private LocalDate created;

    private LocalDate deadline;

    @ManyToOne
    private User owner;

    @ManyToOne
    private User assignTo;

    public Task () {
        this.created = LocalDate.now();
        this.deadline = LocalDate.now().plusDays(14);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getAssignTo () {
        return assignTo;
    }

    public void setAssignTo (User users) {
        this.assignTo = users;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String text) {
        this.description = text;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
