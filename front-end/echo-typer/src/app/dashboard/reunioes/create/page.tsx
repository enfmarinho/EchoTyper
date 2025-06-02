'use client'
import { useState, useEffect, use } from 'react';
import TextareaAutosize from '@mui/material/TextareaAutosize';
import { Card, CardContent, Button, TextField } from '@mui/material';
import { createReuniao, updateReuniao, deleteReuniao, fetchReuniaoById, fetchGroups } from '@/lib/api';
import UploadIcon from '@mui/icons-material/Upload';
import { useRouter } from 'next/navigation';
import { Item } from '@/lib/types';
import ItemGrid from '@/components/ItemGrid';

type Reuniao = {
    id: string;
    title: string;
    transcription: string;
    summary: string;
    annotations: string;
    groupId: number;
};

export default function ReuniaoPage({ params }: { params: { id?: number } }) {
    const router = useRouter();
    const [audioFile, setAudioFile] = useState<File | null>(null);
    const [formData, setFormData] = useState<Partial<Reuniao>>({ title: '', transcription: '', summary: '', annotations: '' });
    const [status, setStatus] = useState<'idle' | 'transcribing' | 'summarizing' | 'done'>('idle');
    const [groups, setGroups] = useState<Item[]>([]);

    const handleAudioUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0]
        if (file) {
            setAudioFile(file)
            startTranscription(file)
        }
    }

    const handleCreate = async () => {
        try {
            console.log('FormData:', formData);
            await createReuniao(formData);
            setFormData({});
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao criar usuário:', err);
        }
    };

    const handleDelete = async () => {
        try {
            await deleteReuniao(params.id!);
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao excluir reunião:', err);
        }
    };

    const handleChange = (field: keyof Reuniao, value: string) => {
        setFormData({ ...formData, [field]: value });
    };

    const startTranscription = async (file: File) => {
        setStatus('transcribing')

        const formDataToSend = new FormData()
        formDataToSend.append('audioFile', file)

        try {
            const response = await fetch('http://localhost:8081/transcribe', {
                method: 'POST',
                body: formDataToSend
            })

            if (!response.ok) {
                throw new Error('Erro ao transcrever o áudio')
            }

            const transcription = await response.text()
            formData.transcription = transcription
            startSummarization(transcription)
        } catch (error) {
            console.error('Erro ao fazer upload/transcrição do áudio:', error)
            // setStatus('error')
        }
    }

    // Precisa implementar a lódica real
    const startSummarization = async (text: string) => {
        setStatus('summarizing')

        const formDataToSend = new FormData()
        formDataToSend.append('transcription', text)

        try {
            const response = await fetch("http://localhost:8081/llm/summarize", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ transcription: text, groupId: formData.groupId })
            });

            if (!response.ok) {
                throw new Error('Erro ao resumir o áudio')
            }

            const json = await response.json();
            formData.summary = json.data?.candidates?.[0]?.content?.parts?.[0]?.text;
            setStatus('done')
        } catch (error) {
            console.error('Erro ao fazer resumo do áudio:', error)
            // setStatus('error')
        }
    }

    const loadGroups = async () => {
        try {
            await fetchGroups().then((data) => {
                setGroups(data.map(group => ({ id: group.id, title: group.groupName })));
            });
        } catch (err) {
            console.error('Erro ao buscar grupos:', err);
        }
    }

    useEffect(() => {
        loadGroups();
    }, []);

    const renderContent = () => {
        switch (status) {
            case 'idle':
                return (
                    <div>
                        <div className="flex flex-col items-center justify-center gap-3">
                            <h2 className="text-xl font-semibold">Esta reuniao pertence a um grupo?</h2>
                            <ItemGrid
                                items={groups}
                                onItemClick={(id) => {
                                    setFormData({ ...formData, groupId: Number(id) });
                                }} />
                            <strong>Grupo Selecionado: {groups.find(meeting => (meeting.id == formData.groupId))?.title}</strong>
                        </div>
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
                    </div>
                );
            case 'transcribing':
                return <p className="text-lg font-medium">Transcrevendo áudio...</p>;
            case 'summarizing':
                return <p className="text-lg font-medium">Resumindo transcrição...</p>;
            case 'done':
                return (
                    <div className="flex flex-col gap-6 w-full max-w-4xl">
                        <TextField
                            label="Título da Reunião"
                            variant="outlined"
                            style={{ backgroundColor: 'white' }}
                            fullWidth
                            value={formData.title}
                            onChange={(e) => handleChange('title', e.target.value)}
                        />
                        <div>
                            <h2 className="text-2xl font-semibold">Grupo: {groups.find(meeting => (meeting.id == formData.groupId))?.title}</h2>
                            {/* <div className="mt-2 border rounded p-4 whitespace-pre-line bg-white text-gray-800 shadow-sm">
                                { }
                            </div> */}
                        </div>
                        <div>
                            <h2 className="text-2xl font-semibold">Transcrição</h2>
                            <div className="mt-2 border rounded p-4 whitespace-pre-line bg-white text-gray-800 shadow-sm">
                                {formData.transcription}
                            </div>
                        </div>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <Card>
                                <CardContent className="p-4">
                                    <h3 className="font-semibold text-lg mb-2">Resumo (LLM)</h3>
                                    <ul className="list-disc pl-4 text-gray-700">
                                        {formData.summary?.split('\n').map((line, index) => (
                                            <div key={index} className="mb-1">
                                                {line.trim()}
                                            </div>
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
                                        value={formData.annotations || ''}
                                        onChange={(e) => handleChange('annotations', e.target.value)}
                                        placeholder="Escreva suas anotações aqui..."
                                    />
                                    <Button
                                        variant="contained"
                                        className="mt-4"
                                        onClick={() => handleCreate()}
                                    >
                                        Salvar
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
            {renderContent()}
        </div>
    );
}
