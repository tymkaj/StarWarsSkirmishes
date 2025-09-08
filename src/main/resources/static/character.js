import { api } from "./api.js";
await api('/swApi/auth/session-info')

const data = JSON.parse(localStorage.getItem('selectedCharacter'));

if (!data) {
    document.getElementById('characterDetail').textContent = 'Nie znaleziono danych postaci.';
} else {
    const container = document.getElementById('characterDetail');
    container.innerHTML = `
  <h1>${data.name}</h1>

  <div class="character-card character-card--details"
       style="background-image:url('${data.imgUrl || ""}')">
  </div>

  <p><strong>Power level:</strong> ${data.powerLvl}</p>
  <p><strong>Role:</strong> ${data.role}</p>
  <p><strong>Affiliation:</strong> ${data.affiliation}</p>
`;
}


