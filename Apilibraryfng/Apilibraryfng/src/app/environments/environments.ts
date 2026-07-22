const isLocal = typeof window !== 'undefined' && 
                (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1');

export const environment = {
    production: !isLocal,
    urlBase: isLocal ? 'http://localhost:8080' : 'https://library-university-production.up.railway.app'
};