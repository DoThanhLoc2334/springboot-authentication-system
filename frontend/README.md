# Frontend (Authentication UI)

This folder is intended to house a standalone frontend app that talks to the backend API for authentication.

## Recommended stack

- **Bundler / dev server:** **Vite** (fast startup, modern build pipeline, great DX)
- **Language:** Plain **JavaScript** is fine for a small auth demo; you can add **TypeScript** later.
- **UI / theme:** "themeleft" is not a standard build tool; if you mean a UI theme/template, you can use any CSS/HTML theme (Tailwind, Bootstrap, or a paid dashboard theme) on top of Vite.

## Why Vite?

- Extremely fast dev server + HMR
- Works well with vanilla JS, React, Vue, Svelte, etc.
- Minimal config for a small auth dashboard

## Getting started

```bash
cd frontend
npm install
npm run dev
```

The app can call the backend (Spring Boot + JWT) at `http://localhost:8080` by default.

## Next steps

- Build login / register UI and call `/api/auth/login` and `/api/auth/register`
- Store JWT in `localStorage` (or `httpOnly` cookie if you want better security)
- Add protected routes (redirect to login when token is missing/expired)
