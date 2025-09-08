import { api, apiJson, apiCharacter } from "./api.js";

await api('/swApi/auth/session-info');

let q = "";          // tekst z inputa
let page = 0;        // 0-based
const size = 10;     // ile kart na stronę

const container = document.getElementById("charactersContainer");
const searchInput = document.getElementById("searchInput");
const pagerBox = document.querySelector(".pager");

// delegacja klików w karty – rejestrujemy raz
container.addEventListener("click", onCharacterClick);

// debounce 300 ms
let timer = null;
searchInput.addEventListener("input", () => {
    clearTimeout(timer);
    timer = setTimeout(async () => {
        q = searchInput.value.trim();
        page = 0;              // nowy tekst → wracamy na pierwszą stronę
        await loadCharacters();
    }, 300);
});

await loadCharacters();

async function loadCharacters() {
    // zawsze używamy endpointu search – pusta fraza zwróci wszystko
    const url = `/swApi/characters/search?q=${encodeURIComponent(q)}&page=${page}&size=${size}`;
    const data = await apiJson(url);

    if (!data) {
        container.innerHTML = "<p>No character data found.</p>";
        pagerBox.innerHTML = "";
        return;
    }

    renderCharacters(data.items);
    renderPage(data);
}

function renderCharacters(characters = []) {
    container.innerHTML = "";

    if (!Array.isArray(characters) || characters.length === 0) {
        container.innerHTML = "<p>No characters match your search.</p>";
        return;
    }

    characters.forEach(character => {
        const card = document.createElement("div");
        card.className = "character-card";
        card.dataset.id = character.id;

        card.style.backgroundImage = `url(${character.imgUrl || ""})`;

        const name = document.createElement("div");
        name.className = "character-name";
        name.textContent = character.name ?? "";

        const level = document.createElement("div");
        level.className = "character-level";
        level.textContent = `Lvl ${character.powerLvl ?? ""}`;

        card.appendChild(name);
        card.appendChild(level);

        container.appendChild(card);
    });
}

function renderPage(p) {
    // p: PageDTO z backendu
    pagerBox.innerHTML = `
    <br>
    <button class="btn" id="prevBtn" ${p.hasPrev ? "" : "disabled"}>Prev</button>
    <span>Page ${p.page + 1} / ${Math.max(p.totalPages, 1)}</span>
    <button class="btn" id="nextBtn" ${p.hasNext ? "" : "disabled"}>Next</button>
    <br>
    <br>
  `;

    pagerBox.querySelector("#prevBtn")?.addEventListener("click", async () => {
        if (!p.hasPrev) return;
        page = Math.max(0, page - 1);
        await loadCharacters();
    });

    pagerBox.querySelector("#nextBtn")?.addEventListener("click", async () => {
        if (!p.hasNext) return;
        page = page + 1;
        await loadCharacters();
    });
}

async function onCharacterClick(e) {
    const card = e.target.closest(".character-card");
    if (!card) return;
    const id = Number(card.dataset.id);
    if (!id) return;
    await apiCharacter(id); // Twoja istniejąca nawigacja do szczegółów
}


