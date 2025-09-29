const apiBase = "/api/tasks";

// Load tasks from backend
async function loadTasks() {
    const res = await fetch(apiBase);
    const tasks = await res.json();
    const list = document.getElementById("taskList");
    list.innerHTML = "";

    tasks.forEach(task => {
        const li = document.createElement("li");
        li.innerHTML = `
            <span>${task.title} - ${task.description}</span>
            <span>
                <button onclick="editTask(${task.id}, '${task.title}', '${task.description}')">Edit</button>
                <button onclick="deleteTask(${task.id})">Delete</button>
            </span>
        `;
        list.appendChild(li);
    });
}

// Add a new task
async function addTask() {
    const title = document.getElementById("taskTitle").value;
    const description = document.getElementById("taskDesc").value;

    if (!title) {
        alert("Title is required");
        return;
    }

    const res = await fetch(apiBase, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({title, description})
    });

    if (res.ok) {
        document.getElementById("taskTitle").value = "";
        document.getElementById("taskDesc").value = "";
        loadTasks();
    } else {
        const error = await res.text();
        alert(error);
    }
}

// Edit a task
function editTask(id, title, description) {
    const newTitle = prompt("Edit Task Title:", title);
    if (!newTitle) return;

    const newDesc = prompt("Edit Task Description:", description);
    fetch(`${apiBase}/${id}`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({title: newTitle, description: newDesc})
    }).then(res => {
        if(res.ok) loadTasks();
        else res.text().then(err => alert(err));
    });
}

// Delete a task
function deleteTask(id) {
    if(!confirm("Are you sure you want to delete this task?")) return;

    fetch(`${apiBase}/${id}`, {method: "DELETE"})
        .then(res => {
            if(res.ok) loadTasks();
            else res.text().then(err => alert(err));
        });
}

// Load tasks when page loads
window.onload = loadTasks;
