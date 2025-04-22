'use client'
import { useState, useEffect, use } from 'react';
import TextareaAutosize from '@mui/material/TextareaAutosize';
import { Card, CardContent, Button } from '@mui/material';
import UploadIcon from '@mui/icons-material/Upload';

export default function ReuniaoPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const [audioFile, setAudioFile] = useState<File | null>(null);
  const [transcription, setTranscription] = useState<string>('');
  const [summary, setSummary] = useState<string>('');
  const [annotations, setAnnotations] = useState<string>('');
  const [status, setStatus] = useState<'idle' | 'transcribing' | 'summarizing' | 'done'>('idle');


    // Ao carregar, verifica se já existe reunião salva
    useEffect(() => {
        if (!id) return;
        async function loadMeeting() {
            try {
                const res = await fetch(`/api/reunioes/${id}`);
                if (res.ok) {
                const data = await res.json();
                // Preenche estados com dados existentes
                setTranscription(data.transcription);
                setSummary(data.summary);
                setAnnotations(data.annotations);
                setStatus('done');
                }
            } catch (err) {
                console.error('Erro ao carregar reunião:', err);
            }
        }
        loadMeeting();
    }, [id]);

  const handleAudioUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) {
      setAudioFile(file)
      startTranscription(file)
    }
  }

  const startTranscription = async (file: File) => {
    setStatus('transcribing')
    // Simula transcrição
    setTimeout(() => {
      const fakeTranscription = `
        00:05:12 Precisamos começar a desenvolver a interface até segunda feira. Temos uma semana de prazo.
        00:05:27 Mas funcionalidades de integração com o banco de dados já foram lançadas no repositório?
        00:05:43 Ainda não, precisamos criar uma chave de API para fazer os testes finais de conexão.
      `
      setTranscription(fakeTranscription)
      startSummarization(fakeTranscription)
    }, 2000)
  }

  const startSummarization = async (text: string) => {
    setStatus('summarizing')
    // Simula resumo
    setTimeout(() => {
      const fakeSummary = `
        • Discussão sobre o desenvolvimento da interface com prazo de uma semana;
        • Integração com banco de dados já iniciada;
        • Falta criar chave de API para testes.
      `
      setSummary(fakeSummary)
      setStatus('done')
    }, 2000)
  }

  const renderContent = () => {
    switch (status) {
      case 'idle':
        return (
            <div className="flex flex-col items-center justify-center gap-6">
            <h2 className="text-xl font-semibold">Envie o áudio da reunião</h2>
            
            {/* Label estilizado que ativa o input de upload */}
            <label
              htmlFor="audio-upload"
              className="cursor-pointer bg-yellow-600 hover:bg-yellow-700 text-white px-6 py-3 rounded-md transition"
            >
              Selecionar arquivo de áudio
            </label>
        
            {/* Input escondido que abre o seletor de arquivos */}
            <input
              type="file"
              id="audio-upload"
              accept="audio/*"
              className="hidden"
              onChange={handleAudioUpload}
            />
          </div>
        );
      case 'transcribing':
        return <p className="text-lg font-medium">Transcrevendo áudio...</p>;
      case 'summarizing':
        return <p className="text-lg font-medium">Resumindo transcrição...</p>;
      case 'done':
        return (
          <div className="flex flex-col gap-6 w-full max-w-4xl">
            <div>
              <h2 className="text-2xl font-semibold">Transcrição</h2>
              <div className="mt-2 border rounded p-4 whitespace-pre-line bg-white text-gray-800 shadow-sm">
                {transcription}
              </div>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Card>
                <CardContent className="p-4">
                  <h3 className="font-semibold text-lg mb-2">Resumo (LLM)</h3>
                  <ul className="list-disc pl-4 text-gray-700">
                    {summary.split('\n').map((item, i) => (
                      <li key={i}>{item}</li>
                    ))}
                  </ul>
                </CardContent>
              </Card>
              <Card>
                <CardContent className="p-4">
                  <h3 className="font-semibold text-lg mb-2">Anotações</h3>
                  <TextareaAutosize
                    minRows={4}
                    maxRows={6}
                    className="w-full p-2 border rounded shadow-sm mt-2"
                    style={{ resize: 'none' }}
                    value={annotations}
                    onChange={(e) => setAnnotations(e.target.value)}
                    placeholder="Escreva suas anotações aqui..."
                  />
                  <Button
                    variant="contained"
                    className="mt-4"
                    onClick={() => {
                      // Lógica para salvar anotações
                      console.log('Anotações salvas:', annotations);
                    }}
                  >
                    Salvar Anotações
                  </Button>
                </CardContent>
              </Card>
            </div>
            <div className="flex justify-end">
              <Button onClick={() => setStatus('idle')}>Nova Transcrição</Button>
            </div>
          </div>
        );
    }
  };

  return (
    <div className="flex flex-col items-center justify-start p-8 min-h-screen bg-gray-100 text-gray-900">
      <h1 className="text-3xl font-bold mb-6">Reunião</h1>
      {renderContent()}
    </div>
  );
}
