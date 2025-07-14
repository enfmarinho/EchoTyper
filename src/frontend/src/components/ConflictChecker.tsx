'use client'

import { useState, useEffect } from "react";
import { fetchReunioes } from "@/lib/api";
import { Button } from "@mui/material";

interface Event {
  id: string;
  date: string;
}

interface Meeting {
  id: number;
  title: string;
  transcription: string;
  summary: string;
  annotations: string;
}

export default function ConflictChecker() {
  const [transcription, setTranscription] = useState("");
  const [result, setResult] = useState<string | null>(null);
  const [meetings, setMeetings] = useState<Meeting[]>([]);
  const [showMeetings, setShowMeetings] = useState(false);


  const loadReunioes = async () => {
        try {
          const data = await fetchReunioes();
          setMeetings(data);
        } catch (err) {
          console.error('Erro ao carregar entrevistas:', err);
        }
    };

  const checkConflicts = async () => {
    try {
      const response = await fetch("http://localhost:8081/llm/check-conflicts", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ transcription }),
      });

      if (!response.ok) {
        throw new Error("Erro na requisição: " + response.statusText);
      }

      const json = await response.json();
      const text = json.data?.candidates?.[0]?.content?.parts?.[0]?.text;
      setResult(JSON.stringify(text, null, 2));
    } catch (error: any) {
      setResult("Erro ao verificar conflitos: " + error.message);
    }
  };

  useEffect(() => {
    loadReunioes();
  }, []);

  const handleMeetingSelection = (meeting: Meeting) => {
    setTranscription(meeting.transcription);
    setShowMeetings(false);
  };

  return (
    <main className="p-6 max-w-3xl mx-auto bg-white rounded shadow-md text-black border border-black">
      <h1 className="text-2xl font-bold mb-4">Verificador de Conflitos de Eventos</h1>

      <Button
        variant="contained"
        size="medium"
        sx={{ mt: 2 }}
        onClick={() => {
          setShowMeetings(!showMeetings);
        }}
      >
        Escolher Entrevista
      </Button>

      {showMeetings && (
        <div className="mb-4 border p-4 rounded">
          <h2 className="font-semibold mb-2">Selecione uma Entrevista</h2>
          <ul>
            {meetings.map((meeting) => (
              <li
                key={meeting.id}
                onClick={() => handleMeetingSelection(meeting)}
                className="cursor-pointer hover:underline text-blue-700"
              >
                {meeting.title}
              </li>
            ))}
          </ul>
        </div>
      )}

      <label className="block mb-2 font-medium">Transcrição da Entrevista</label>
      <textarea
        value={transcription}
        onChange={(e) => setTranscription(e.target.value)}
        className="w-full border border-black rounded p-2 mb-4 text-black"
        rows={5}
      />

      <Button
        variant="contained"
        sx={{ mt: 2 }}
        onClick={checkConflicts}
      >
        Verificar Conflitos
      </Button>
      

      {result && (
        <pre className="bg-gray-100 p-4 rounded text-sm whitespace-pre-wrap text-black border border-black">
          {result}
        </pre>
      )}
    </main>
  );
}
