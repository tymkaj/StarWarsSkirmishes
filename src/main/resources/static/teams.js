import { apiJson } from "./api.js";

const params = new URLSearchParams(location.search);
const inBattleMode = params.has("battle");

if (!inBattleMode) {
    sessionStorage.removeItem("battle.opponentUserId");
    sessionStorage.removeItem("battle.opponentTeamId");
}

let currentUser = null;

if (window.location.pathname.includes("teams.html")) {
    localStorage.removeItem("selectedUser");
}

try {
    currentUser = await apiJson("/swApi/auth/session-info");
    await fetchTeams();
} catch {
    window.location.href = "main.html";
}

async function fetchTeams() {
    const selectedUser = JSON.parse(localStorage.getItem("selectedUser")); // przeglądany przeciwnik
    if (selectedUser) {
        // Pobierz drużyny przeciwnika
        const res = await apiJson("/swApi/teams/userTeams", {
            method: "POST",
            credentials: "include",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(selectedUser)
        })
        document.getElementById("opponent-title").textContent =
            `Teams of user: ${selectedUser.username}`;
        renderTeams(res, {mode: "opponent", selectedUser});
    } else {
        // Pobierz nasze drużyny
        const res = await apiJson("/swApi/teams")
        renderTeams(res, {mode: "mine"});
    }
}

function renderTeams(teams, opts = { mode: "mine" }) {

    const container = document.getElementById("teamsContainer");
    container.innerHTML = "";

    const opponentUserId = sessionStorage.getItem("battle.opponentUserId");
    const opponentTeamId = sessionStorage.getItem("battle.opponentTeamId");

    teams.forEach(team => {
        // 1) sekcja + tytuł
        const section = document.createElement('div');
        section.className = 'team-section';

        const title = document.createElement('div');
        title.className = 'team-title';
        title.textContent = team.name || 'Unknown';

        const teamRow = document.createElement('div');
        teamRow.className = 'team-row';

        // Renderowanie postaci
        team.characters.forEach(character => {
            const card = document.createElement("div");
            card.className = "character-card";
            card.style.backgroundImage = `url(${character.imgUrl})`;

            const name = document.createElement("div");
            name.className = "character-name";
            name.textContent = character.name;

            const level = document.createElement("div");
            level.className = "character-level";
            level.textContent = `lvl ${character.powerLvl}`;

            card.appendChild(name);
            card.appendChild(level);
            teamRow.appendChild(card);

            // Klik w kartę -> szczegóły postaci
            card.addEventListener("click", async () => {
                const res = await apiJson("/swApi/characters/details", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({id: character.id})
                })
                localStorage.setItem("selectedCharacter", JSON.stringify(res))
                window.location.href = "character.html";
            });

            teamRow.appendChild(card);
        });

        container.appendChild(teamRow);

        // Przyciski pod drużyną
        const actions = document.createElement("div");
        actions.style.display = "flex";
        actions.style.justifyContent = "center";
        actions.style.marginTop = "8px";
        actions.style.gap = "8px";

        const btn = document.createElement("button");
        btn.className = "btn";

        const delBtn = document.createElement("button");
        delBtn.className = "delBtn";
        delBtn.textContent = "Delete";
        delBtn.style.display = (opts.mode === "mine" && opponentTeamId === null && opponentUserId === null) ? "inline-block" : "none";

        delBtn.addEventListener("click", async () => {
            if (!confirm(`Delete team "${team.name}"? This cannot be undone.`)) return;

            try {
                await apiJson("/swApi/teams/delete", {
                    method: "DELETE",
                    credentials: "include",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ id: team.id, name: team.name }),
                })

                await fetchTeams();
            } catch (e) {
                alert("Failed to delete team. Please try again.");
            }
        });

        if (opts.mode === "opponent") {
            btn.textContent = "Fight";
            btn.addEventListener("click", () => {
                sessionStorage.setItem("battle.opponentUserId", String(opts.selectedUser.id));
                sessionStorage.setItem("battle.opponentTeamId", String(team.id));
                localStorage.removeItem("selectedUser"); // wracamy do naszych drużyn
                window.location.href = "teams.html?battle=1";
            });
        } else {
            if (opponentUserId && opponentTeamId) {
                btn.textContent = "Choose team";
                btn.addEventListener("click", () => {
                    commenceBattle({
                        myUserId: currentUser.userId,
                        myTeamId: team.id,
                        opponentUserId: Number(opponentUserId),
                        opponentTeamId: Number(opponentTeamId)
                    });
                });
            } else {
                btn.style.display = "none";
            }
        }

        actions.appendChild(btn);
        actions.appendChild(delBtn);
        section.appendChild(title);
        section.appendChild(teamRow);
        section.appendChild(actions);
        container.appendChild(section);
    });
}

async function commenceBattle({myUserId, myTeamId, opponentUserId, opponentTeamId}) {
    const payload = {
        userId: myUserId,
        teamId: myTeamId,
        opponentId: opponentUserId,
        opponentTeamId
    };

    try {
        const data = await apiJson("/swApi/battle/commence", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        })

        if (!data || data.error) {
            alert(`Failed to commence battle: ${data?.error ?? 'no data'}`);
            return;
        }

        localStorage.setItem("battleResult", JSON.stringify(data));
        window.location.href = "battleResult.html";
    } catch (err) {
        console.error(err);
        alert("Connection error while commencing battle.");
    }

}



