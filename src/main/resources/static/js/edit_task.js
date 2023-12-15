$(document).ready(function () {
    M.AutoInit();
    M.updateTextFields();
});

function updateTask() {
    const taskId = $('#taskId').val();
    const header = $('#header').val();
    const description = $('#description').val();
    const status = $('#status').val();
    const assignTo = $('#assign_to').val();
    const deadline = $('#deadline').val();

    const form = new FormData();

    form.append('header', header);
    form.append('description', description);
    form.append('status', status);
    form.append('deadline', deadline);
    form.append('assign_to', assignTo);

    $.ajax({
        url: '/api/tasks/' + taskId,
        type: 'PUT',
        contentType: false,
        processData: false,
        data: form,
        success: function (response) {
            if (response.id) {
                M.toast({html: 'Task updated'});

                return setTimeout(function () {
                    return window.location.href = '/viewTask/' + response.id;
                }, 1000)
            } else {
                return M.toast({html: 'Task update failed.'});
            }
        },
        error: function () {
            return M.toast({html: 'Task update failed.'});
        }
    });
}