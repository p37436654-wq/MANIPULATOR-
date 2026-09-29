# MANIPULATOR — Groq Backend

This backend keeps the Groq API key on the server.

## Vercel Environment Variable

Add:

GROQ_API_KEY = your Groq API key

Optional:

GROQ_MODEL = llama-3.1-8b-instant

Do NOT put the Groq key in the Android application.

## API

POST /api/chat

JSON:
{
  "message": "Hello MANIPULATOR"
}

Response:
{
  "reply": "Hello Boss..."
}
