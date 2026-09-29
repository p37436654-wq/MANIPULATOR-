export default async function handler(req, res) {
  if (req.method !== "POST") {
    return res.status(405).json({ error: "Method not allowed" });
  }

  try {
    const { message, messages = [] } = req.body || {};

    if (!message && (!Array.isArray(messages) || messages.length === 0)) {
      return res.status(400).json({ error: "Message is required" });
    }

    const conversation = Array.isArray(messages) && messages.length
      ? messages
      : [{ role: "user", content: String(message) }];

    const response = await fetch("https://api.groq.com/openai/v1/chat/completions", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${process.env.GROQ_API_KEY}`
      },
      body: JSON.stringify({
        model: process.env.GROQ_MODEL || "llama-3.1-8b-instant",
        messages: [
          {
            role: "system",
            content:
              "You are MANIPULATOR, a futuristic personal AI assistant. Be helpful, concise, clear, and natural. Address the user as Boss when appropriate."
          },
          ...conversation
        ],
        temperature: 0.7
      })
    });

    const data = await response.json();

    if (!response.ok) {
      return res.status(response.status).json({
        error: data?.error?.message || "Groq request failed"
      });
    }

    return res.status(200).json({
      reply: data?.choices?.[0]?.message?.content || ""
    });
  } catch (error) {
    return res.status(500).json({
      error: "Server error",
      details: error instanceof Error ? error.message : "Unknown error"
    });
  }
}
