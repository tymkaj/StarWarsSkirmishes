// api.js (module)

// POST/PUT/PATCH/DELETE wymagają CSRF
function needsCsrf(method = 'GET') {
    const m = (method || '').toUpperCase();
    return m !== 'GET' && m !== 'HEAD' && m !== 'OPTIONS';
}

let cachedCsrf = null;

async function fetchCsrf() {
    // GET, który tworzy token w sesji i wystawia go w nagłówku oraz w JSON
    const res = await fetch('/swApi/auth/csrf', { credentials: 'include' });
    cachedCsrf = res.headers.get('X-CSRF-TOKEN');
    if (!cachedCsrf) {
        try {
            const data = await res.json();
            cachedCsrf = data?.token || null;
        } catch (_) { /* ignore */ }
    }
    return cachedCsrf;
}

async function ensureCsrf() {
    if (!cachedCsrf) await fetchCsrf();
}

export async function api(path, options = {}, behaviour = { redirectOn401: true }) {
    const headers = new Headers(options.headers || {});

    if (needsCsrf(options.method)) {
        await ensureCsrf();                           // PRIMING
        if (cachedCsrf) {
            headers.set('X-CSRF-TOKEN', cachedCsrf);   // Spring 6 domyślna nazwa
            headers.set('X-XSRF-TOKEN', cachedCsrf);   // opcjonalnie (kompat.)
        }
    }

    let res = await fetch(path, { credentials: 'include', ...options, headers });

    // „miękki” retry: jeśli 403 i brak/expired token – pobierz świeży i spróbuj raz jeszcze
    if (res.status === 403 && needsCsrf(options.method)) {
        await fetchCsrf();
        if (cachedCsrf) {
            headers.set('X-CSRF-TOKEN', cachedCsrf);
            headers.set('X-XSRF-TOKEN', cachedCsrf);
            res = await fetch(path, { credentials: 'include', ...options, headers });
        }
    }

    if (res.status === 401) {
        if (behaviour.redirectOn401) window.location.href = '/main.html';
        return res;
    }
    if (res.status === 403) throw new Error('403');
    return res;
}



export async function apiJson(path, options) {
    const r = await api(path, options);
    return r.status === 204 ? null : r.json();
}

export function clearCsrfCache() {
    cachedCsrf = null;
}

export async function apiLogout() {
    await api('/swApi/auth/logout', { method: 'POST' });
    clearCsrfCache();
    sessionStorage.clear();
    localStorage.clear();
}

export async function apiCharacter(id) {
    try {
        const data = await apiJson('/swApi/characters/details', {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id })
        });

        if (!data) {
            alert('No character data found (204).');
            return;
        }

        localStorage.setItem('selectedCharacter', JSON.stringify(data));
        window.location.href = 'character.html';
    } catch (err) {
        console.error(err);
        alert('Failed to retrieve character data.');
    }
}
