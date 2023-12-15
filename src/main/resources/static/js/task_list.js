$(document).ready(function () {
    M.AutoInit();
    M.updateTextFields();

    loadTasks();
});

function loadTasks () {
    const loadCircle = $('#loading');
    const tasksTable = $('#tasksDiv');
    const loadErrorContainer = $('#upload-error');
    const tasksRowsTable = $('#tasksList');
    const noTasksMessage = $('#no-tasks');

    tasksTable.css('display', 'none');
    loadCircle.css('display', '');

    $.get({
        url: '/api/tasks',
        contentType: 'application/json',
        success: function (response) {
            tasksRowsTable.html('');

            if (response.length <= 0) {
                noTasksMessage.css('display', '');
                loadErrorContainer.css('display', 'none');
                tasksTable.css('display', 'none');
                loadCircle.css('display', 'none');
                return;
            }

            response.forEach(function (item) {
                const task = $(`
                    <tr>
                        <td>${item.id}</td>
                        <td>${item.header}</td>
                        <td>${item.owner.userName}</td>
                        <td>${item.assignTo.userName}</td>
                        <td>${item.status.replace('_', ' ')}</td>
                        <td>${item.created}</td>
                        <td>${item.deadline}</td>
                        <td>
                            <a href="/viewTask/${item.id}">View</a>
                            <br>
                            <a href="/editTask/${item.id}">Edit</a>
                            <br>
                            <a style="cursor: pointer" onclick="deleteTask(${item.id})">Delete</a>
                        </td>
                    </tr>
                `);

                tasksRowsTable.append(task);
            });

            loadCircle.css('display', 'none');
            tasksTable.css('display', '');
            loadErrorContainer.css('display', 'none');
        },
        error: function () {
            tasksTable.css('display', 'none');
            loadCircle.css('display', 'none');
            loadErrorContainer.css('display', 'unset');

            return M.toast({html: 'Load error'});
        }
    });
}

function openAddTaskWindow () {
    M.Modal.getInstance(document.getElementById('upload-modal')).open();
}

function createTask (button) {
    const header = $('#title').val();
    const description = $('#description').val();
    const assignTo = $('#assign_to').val();

    if (!header.trim()) {
        M.toast({html: 'Please enter a title.'});
        return;
    }

    button.setAttribute('disabled', 'true');
    button.innerText = 'Creating...';

    const form = new FormData();

    form.append('header', header);
    form.append('description', description);
    form.append('assign_to', assignTo);

    $.post({
        url: '/api/tasks/create',
        method: 'POST',
        data: form,
        processData: false,
        contentType: false,
        success: function (response) {
            button.innerText = 'Create';

            if (response.id) {
                M.toast({html: 'Task created.'});

                return setTimeout(function () {
                    return window.location.href = '/viewTask/' + response.id;
                }, 2000);
            } else {
                button.removeAttribute('disabled');

                return M.toast({html: 'Task not created.'})
            }
        },
        error: function () {
            button.removeAttribute('disabled');
            button.innerText = 'Create';

            return M.toast({html: 'Connection failed.'});
        }
    });
}

function deleteTask (taskId) {
    if (confirm('Are you sure want to delete this task?')) {
        $.ajax({
            url: '/api/tasks/' + taskId,
            method: 'DELETE',
            success: function () {
                return window.location.reload();
            },
            error: function () {
                return M.toast({html: 'Task deletion failed.'});
            }
        });
    }
}