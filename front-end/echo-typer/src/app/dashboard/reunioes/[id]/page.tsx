'use client'
import { useState, useEffect, use } from 'react';
import TextareaAutosize from '@mui/material/TextareaAutosize';
import { Card, CardContent, Button, TextField } from '@mui/material';
import { createReuniao, updateReuniao, deleteReuniao, fetchReuniaoById } from '@/lib/api';
import UploadIcon from '@mui/icons-material/Upload';
import { useRouter, useParams } from 'next/navigation';


type Reuniao = {
    id: string;
    title: string;
    transcription: string;
    summary: string;
    annotations: string;
};

export default function ReuniaoPage() {
    const router = useRouter();
    const [audioFile, setAudioFile] = useState<File | null>(null);
    const [formData, setFormData] = useState<Partial<Reuniao>>({ title: '', transcription: '', summary: '', annotations: '' });
    const params = useParams();
    const isEditing = !!params.id;

    const loadReuniao = async () => {
        if (isEditing && params.id) {
            fetchReuniaoById(Number(params.id)).then(data => {
                setFormData({
                    id: Number(params.id).toString(),
                    title: data.title,
                    transcription: data.transcription,
                    summary: data.summary,
                    annotations: data.annotations
                });
            }).catch(err => {
                console.error('Erro ao carregar reunião:', err);
            });
        }
    };

    useEffect(() => {
        loadReuniao();
    }, []);

    const handleDelete = async () => {
        try {
            await deleteReuniao(Number(params.id)!);
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao excluir reunião:', err);
        }
    };

    const handleUpdate = async () => {
        try {
            await updateReuniao(Number(params.id)!, formData);
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao atualizar reunião:', err);
        }
    };

    const handleChange = (field: keyof Reuniao, value: string) => {
        setFormData({ ...formData, [field]: value });
    };


    const renderContent = () => {
        if (isEditing) {
            return (
                <div className="flex flex-col gap-6 w-full max-w-4xl">
                    <div className="flex flex-col gap-6 w-full max-w-4xl">
                        <TextField
                            label="Título da Reunião"
                            variant="outlined"
                            style={{ backgroundColor: 'white' }}
                            fullWidth
                            value={formData.title || ''}
                            onChange={(e) => handleChange('title', e.target.value)}
                        />
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
                                        {(formData.summary ?? '').split('\n').map((item, i) => (
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
                                        value={formData.annotations || ''}
                                        onChange={(e) => handleChange('annotations', e.target.value)}
                                        placeholder="Escreva suas anotações aqui..."
                                    />
                                </CardContent>
                            </Card>
                        </div>
                    </div>
                    <div className="flex gap-4">
                        <Button variant="contained" onClick={handleUpdate}>Salvar Alterações</Button>
                        <Button variant="outlined" color="error" onClick={handleDelete}>Excluir</Button>
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
