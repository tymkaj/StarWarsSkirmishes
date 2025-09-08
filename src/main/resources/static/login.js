import {api, clearCsrfCache} from "./api.js";

document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const msg = document.getElementById('message');

    try {
        const res = await api('/swApi/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        }, { redirectOn401: false });

        if (!res.ok) {
            let text = '';
            try {
                const data = await res.json();
                text = data?.error || '';
            } catch {
                text = await res.text().catch(()=>'');
            }
            msg.innerText = 'Error: ' + (text || 'invalid login or password');
            return;
        }

        msg.innerText = 'Logged in successfully!';
        clearCsrfCache();
        setTimeout(() => { window.location.href = '/mainLoggedIn.html'; }, 800);

    } catch (err) {
        console.error(err);
        msg.innerText = 'Error: ' + (err?.message || 'Login failed');
    }
});
