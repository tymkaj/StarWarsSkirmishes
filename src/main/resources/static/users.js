import { api } from "./api.js";

try {
    const sessionRes = await api("/swApi/auth/session-info");
    if (!sessionRes.ok) {
        window.location.href = "main.html";
    }

    const currentUser = await sessionRes.json();

    // get a list of users
    const usersRes = await api("/swApi/users");
    if (!usersRes.ok) {
        throw new Error("Cannot load users");
    }
    const users = await usersRes.json();

    // filtering and rendering, returns every user except us
    const filteredUsers = users.filter(u => u.username !== currentUser.username);
    renderUsers(filteredUsers);

} catch (err) {
    console.warn("Error:", err);
    window.location.href = "main.html";
}

function renderUsers(users) {
    const container = document.getElementById("usersContainer");

    users.forEach(user => {
        const card = document.createElement("div");
        card.className = "user-card";

        const username = document.createElement("p");
        username.textContent = user.username;
        card.appendChild(username);

        card.addEventListener("click", () => {
            localStorage.setItem("selectedUser", JSON.stringify(user));
            window.location.href = "opponentTeams.html";
        });

        container.appendChild(card);
    });
}

