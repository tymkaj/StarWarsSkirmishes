import { api, apiJson, apiCharacter } from "./api.js";

let page = 0;
const size = 10;

let filter = 'ALL';
let dir = 'DESC';

await api('/swApi/auth/session-info')

await loadBattles();

const container = document.getElementById("battlesContainer");
container.addEventListener("click", onCharacterClick);

async function loadBattles() {
    const data = await apiJson(`/swApi/battle?page=${page}&size=${size}&filter=${filter}&dir=${dir}`);

    if (!data) {
        const c = document.getElementById("battlesContainer");
        c.innerHTML = `<p>Failed to retrieve battles' data.</p>`;
    }
    renderBattles(data.items);
    renderPage(data);
}

function renderBattles(battles) {
    const container = document.getElementById("battlesContainer");
    container.innerHTML = "";

    if (!Array.isArray(battles) || battles.length === 0) {
        container.innerHTML = `<p>No completed battles found.</p>`;
        return;
    }

    battles.forEach(b => {
        // BattleDTO
        // { id, winner:UserDTO, loser:UserDTO, teamA:TeamDTO, teamB:TeamDTO, winnerTeam:TeamDTO }

        const winnerName = b.winner?.username ?? "Unknown";
        const loserName  = b.loser?.username  ?? "Unknown";

        const teamA = b.teamA || null;
        const teamB = b.teamB || null;
        const winnerTeam = b.winnerTeam || null;

        const loserTeam = (winnerTeam?.id === teamA?.id) ? teamB : teamA;

        const card = document.createElement("div");
        card.className = "battle-card";

        const header = document.createElement("div");
        header.className = "battle-header";
        header.innerHTML = `
      <div class="battle-title">
        Battle id: ${b.id ?? "?"}
      </div>
      <div class="badge">
        <span class="winner">Winner: ${winnerName}</span> &nbsp;|&nbsp;
        <span class="loser">Loser: ${loserName}</span>
      </div>
    `;
        card.appendChild(header);

        const teamsWrap = document.createElement("div");
        teamsWrap.className = "teams";

        const winName  = winnerTeam?.name || '(deleted team)';
        const loseName = loserTeam?.name  || '(deleted team)';

        const boxWin = document.createElement("div");
        boxWin.className = "team-box";
        boxWin.innerHTML = `
          <h3 class="winner">Winning team</h3>
          <div class="team-name">${winName}</div>
          ${renderTeam(winnerTeam)}
        `;

        const boxLose = document.createElement("div");
        boxLose.className = "team-box";
        boxLose.innerHTML = `
          <h3 class="loser">Losing team</h3>
          <div class="team-name">${loseName}</div>
          ${renderTeam(loserTeam)}
        `;


        teamsWrap.appendChild(boxWin);
        teamsWrap.appendChild(boxLose);
        card.appendChild(teamsWrap);

        container.appendChild(card);
    });
}

function renderPage(p) {
    const actions = document.querySelector('.actions');
    actions.innerHTML = `
    <div class="bar">
      <label>Filter:
        <select id="fFilter">
          <option value="ALL">All</option>
          <option value="WON">Won</option>
          <option value="LOST">Lost</option>
        </select>
      </label>
      <label>Order:
        <select id="fDir">
          <option value="DESC">Desc</option>
          <option value="ASC">Asc</option>
        </select>
      </label>

      <button class="btn" id="prevBtn" ${p.hasPrev ? '' : 'disabled'}>Prev</button>
      <span>Page ${p.page + 1} / ${Math.max(p.totalPages, 1)}</span>
      <button class="btn" id="nextBtn" ${p.hasNext ? '' : 'disabled'}>Next</button>
      <br>
      <br>
      <button class="btn" onclick="window.location.href='mainLoggedIn.html'">Menu</button>
    </div>
  `;

    // set current select values
    actions.querySelector('#fFilter').value = filter;
    actions.querySelector('#fDir').value = dir;

    // changing filtering/sorting sets the page to 0 and reloads
    actions.querySelector('#fFilter').addEventListener('change', async e => {
        filter = e.target.value; page = 0; await loadBattles();
    });
    actions.querySelector('#fDir').addEventListener('change', async e => {
        dir = e.target.value; page = 0; await loadBattles();
    });

    const prev = document.getElementById('prevBtn');
    const next = document.getElementById('nextBtn');

    prev.addEventListener('click', async () => {
        if (!p.hasPrev) return;
        page = Math.max(0, page - 1);
        await loadBattles();
    });

    next.addEventListener('click', async () => {
        if (!p.hasNext) return;
        page = page + 1;
        await loadBattles();
    });
}

function renderTeam(team) {
    const list = Array.isArray(team?.characters) ? team.characters : [];
    if (!list.length) return `<p>(No team data found)</p>`;
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
}

async function onCharacterClick(e) {
    const card = e.target.closest(".character-card");
    if (!card) return;
    const id = Number(card.dataset.id);
    if (!id) return;

    await apiCharacter(id);
}
