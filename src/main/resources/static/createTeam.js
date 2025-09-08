import { api, apiJson } from "./api.js";

await api('/swApi/auth/session-info');

let characters = [];
const selects = document.querySelectorAll('.character-select');

const characterList = await apiJson('/swApi/characters');
characters = characterList;
populateAllSelects();
addListeners();


function populateAllSelects() {
    selects.forEach(select => populateSelect(select));
}

function populateSelect(select, exclude = []) {
    const currentValue = select.value;
    select.innerHTML = '';
    characters
        .filter(c => !exclude.includes(String(c.id)) || String(c.id) === currentValue)
        .forEach(c => {
            const option = document.createElement('option');
            option.value = c.id;
            option.textContent = c.name;
            select.appendChild(option);
        });
    select.value = currentValue;
}

function addListeners() {
    selects.forEach(select => {
        select.addEventListener('change', () => {
            const selectedIds = [...selects].map(s => s.value);
            selects.forEach(s => populateSelect(s, selectedIds));
        });
    });
}

document.getElementById('teamForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('teamName').value;
    const charactersId = [...selects].map(s => Number(s.value));

    const data = await apiJson('/swApi/teams/create', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({name, characters: charactersId})
    })

    if (!data) {
        alert('Error during team creation');
        return;
    }

    alert('Team has been created!');
    window.location.href = '/teams.html';
});
