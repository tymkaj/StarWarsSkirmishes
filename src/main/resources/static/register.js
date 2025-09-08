import { api } from "./api.js";

document.getElementById('register-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const email    = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const msg = document.getElementById('message');

    try {
        const res = await api('/swApi/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        }, { redirectOn401: false });

        if (!res.ok) {
            let errMsg = 'Registration failed';
            try {
                const data = await res.json();
                errMsg = data?.error || errMsg;
            } catch {
                errMsg = await res.text().catch(()=>'Registration failed');
            }
            msg.innerText = 'Error: ' + errMsg;
            return;
        }

        msg.innerText = 'Successful registration! You may now log in.';
        setTimeout(() => { window.location.href = '/login.html'; }, 2000);

    } catch (err) {
        msg.innerText = 'Error: ' + (err?.message || 'Registration failed');
    }
});
