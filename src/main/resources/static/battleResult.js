import { apiCharacter } from "./api.js";

    (function () {
    const summary = document.getElementById("battle-summary");
    const againBtn = document.getElementById("againBtn");
    const homeBtn  = document.getElementById("homeBtn");

    againBtn.addEventListener("click", () => {
    localStorage.removeItem("battleResult");
    // wyczyść ewentualne resztki z poprzedniej selekcji
    sessionStorage.clear();
    // idź od razu do wyboru przeciwnika
    window.location.href = "users.html";
});

    homeBtn.addEventListener("click", () => {
    localStorage.removeItem("battleResult");
    sessionStorage.clear();
    window.location.href = "mainLoggedIn.html";
});

    // 1) Pobierz wynik z localStorage
    const raw = localStorage.getItem("battleResult");
    let battle = null;
    try { battle = raw ? JSON.parse(raw) : null; } catch { battle = null; }

    // 2) Jeśli brak danych lub backend zwrócił błąd – pokaż komunikat
    if (!battle || typeof battle !== "object" || (typeof battle.status === "number" && battle.status >= 400)) {
    summary.innerHTML = `
          <p>No valid battle data.</p>
          ${battle?.error ? `<p class="note">${battle.error} (status ${battle.status})</p>` : `<p class="note">Begin battle to see result.</p>`}
        `;
    return;
}

    // 3) Pola zgodnie z Twoim BattleDTO
    const winnerUser = battle.winner || {};
    const loserUser  = battle.loser  || {};
    const winnerName = winnerUser.username || "Nieznany";
    const loserName  = loserUser.username || "Nieznany";

    const teamA = battle.teamA || {};
    const teamB = battle.teamB || {};
    const winnerTeam = battle.winnerTeam || {};

    // Wyznacz przegraną drużynę: jeśli id pasuje do teamA → przegrany to teamB; w przeciwnym razie teamA
    let loserTeam = {};
    if (winnerTeam && teamA && teamB) {
    const aId = teamA.id
    const wId = winnerTeam.id
    loserTeam = (aId != null && wId != null && aId === wId) ? teamB : teamA;
}

    const renderTeam = (team) => {
        const list = Array.isArray(team?.characters) ? team.characters : [];
        if (!list.length) return `<p class="note">(No team data)</p>`;
        return `
    <div class="team">
      ${list.map(c => `
        <div class="character-card"
             data-id="${c.id}"
             title="Check details ${c.name}"
             style="background-image:url('${c.imgUrl || ""}')">
          <div class="character-name">${c.name || ""}</div>
          <div class="character-level">lvl ${c.powerLvl || ""}</div>
        </div>
      `).join("")}
    </div>
  `;
};

    summary.addEventListener('click', async (e) => {
        const card = e.target.closest('.character-card');
        if (!card) return;

        const id = Number(card.dataset.id);
        if (!id) return;

        await apiCharacter(id);
    });

        const winTeamName  = winnerTeam?.name || 'Bez nazwy';
        const loseTeamName = loserTeam?.name  || 'Bez nazwy';

        summary.innerHTML = `
          <h2>Winner: <span class="winner">${winnerName}</span></h2>
          <h3>Winning team:</h3>
          <div class="team-name">${winTeamName}</div>
          ${renderTeam(winnerTeam)}
        
          <h2>Loser: <span class="loser">${loserName}</span></h2>
          <h3>Losing team:</h3>
          <div class="team-name">${loseTeamName}</div>
          ${renderTeam(loserTeam)}
        `;

})();