'use client'

import { useState, useEffect } from "react";
import { fetchReunioes } from "@/lib/api";

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
  const [events, setEvents] = useState<Event[]>([]);
  const [newEvent, setNewEvent] = useState<Event>({ id: "", date: "" });
  const [result, setResult] = useState<string | null>(null);
  const [meetings, setMeetings] = useState<Meeting[]>([]);
  const [showMeetings, setShowMeetings] = useState(false);

  const fetchEvents = async () => {
    const response = await fetch("http://localhost:8081/calendar");
    const data = await response.json();
    setEvents(data);
  };

  const loadReunioes = async () => {
        try {
          const data = await fetchReunioes();
          setMeetings(data);
        } catch (err) {
          console.error('Erro ao carregar reuniões:', err);
        }
    };

  const checkConflicts = async () => {
    try {
      const response = await fetch("http://localhost:8081/llm/check-conflicts", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ transcription, events }),
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
    fetchEvents();
  }, []);

  const handleMeetingSelection = (meeting: Meeting) => {
    setTranscription(meeting.transcription);
    setShowMeetings(false);
  };

  return (
    <main className="p-6 max-w-3xl mx-auto bg-white rounded shadow-md text-black border border-black">
      <h1 className="text-2xl font-bold mb-4">Verificador de Conflitos de Eventos</h1>

      <button
        onClick={() => {
          setShowMeetings(!showMeetings);
        }}
        className="bg-purple-600 text-white px-4 py-2 rounded mb-4"
      >
        Escolher Reunião
      </button>

      {showMeetings && (
        <div className="mb-4 border p-4 rounded">
          <h2 className="font-semibold mb-2">Selecione uma Reunião</h2>
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

      <label className="block mb-2 font-medium">Transcrição da Reunião</label>
      <textarea
        value={transcription}
        onChange={(e) => setTranscription(e.target.value)}
        className="w-full border border-black rounded p-2 mb-4 text-black"
        rows={5}
      />

      <div className="mb-4">
        <h2 className="font-semibold mb-2">Adicionar Evento</h2>
        <input
          type="text"
          placeholder="ID do evento"
          value={newEvent.id}
          onChange={(e) => setNewEvent({ ...newEvent, id: e.target.value })}
          className="border border-black rounded p-2 mr-2 text-black"
        />
        <input
          type="datetime-local"
          value={newEvent.date}
          onChange={(e) => setNewEvent({ ...newEvent, date: e.target.value })}
          className="border border-black rounded p-2 mr-2 text-black"
        />
        <button onClick={() => {
          if (newEvent.id && newEvent.date) {
            setEvents([...events, newEvent]);
            setNewEvent({ id: "", date: "" });
          }
        }} className="bg-blue-500 text-white px-4 py-2 rounded">
          Adicionar
        </button>
      </div>

      <ul className="mb-4">
        {events.map((event, idx) => (
          <li key={idx} className="text-sm">
            {event.id} - {event.date}
          </li>
        ))}
      </ul>

      <button
        onClick={checkConflicts}
        className="bg-green-600 text-white px-6 py-2 rounded mb-4"
      >
        Verificar Conflitos
      </button>

      {result && (
        <pre className="bg-gray-100 p-4 rounded text-sm whitespace-pre-wrap text-black border border-black">
          {result}
        </pre>
      )}
    </main>
  );
}
