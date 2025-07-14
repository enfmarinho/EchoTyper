'use client'
import { useState, useEffect, use } from 'react';
import TextareaAutosize from '@mui/material/TextareaAutosize';
import { Card, CardContent, Button, TextField } from '@mui/material';
import { createReuniao, updateReuniao, deleteReuniao, fetchReuniaoById, fetchGroupById } from '@/lib/api';
import UploadIcon from '@mui/icons-material/Upload';
import { useRouter, useParams } from 'next/navigation';

type ReuniaoContent = {};

type Reuniao = {
    id: string;
    title: string;
    transcription: string;
    summary: string;
    annotations: string;
    groupId: string;
    content: ReuniaoContent;
};

export default function ReuniaoPage() {
    const router = useRouter();
    const [audioFile, setAudioFile] = useState<File | null>(null);
    const [formData, setFormData] = useState<Partial<Reuniao>>({
        title: '',
        transcription: '',
        summary: '',
        annotations: '',
        groupId: '',
        content: {}
    });
    const [groupName, setGroupName] = useState<string>("");
    const params = useParams();
    const isEditing = !!params.id;

    useEffect(() => {
        const loadReuniaoAndGroup = async () => {
            if (params.id) {
                try {
                    // Carrega os dados da reunião
                    const reuniaoData = await fetchReuniaoById(Number(params.id));
                    setFormData(reuniaoData); // Assume que a API já retorna o objeto com a estrutura aninhada

                    // Carrega o nome do grupo usando o groupId dos dados recebidos
                    if (reuniaoData.groupId) {
                        const groupData = await fetchGroupById(Number(reuniaoData.groupId));
                        setGroupName(groupData.groupName);
                    }
                } catch (err) {
                    console.error('Erro ao carregar dados:', err);
                }
            }
        };

        loadReuniaoAndGroup();
    }, [params.id]);


    const handleDelete = async () => {
        try {
            await deleteReuniao(Number(params.id)!);
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao excluir entrevista:', err);
        }
    };

    const handleUpdate = async () => {
        try {
            await updateReuniao(Number(params.id)!, formData);
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao atualizar entrevista:', err);
        }
    };

    const handleChange = (field: keyof Reuniao, value: string) => {
        setFormData({ ...formData, [field]: value });
    };

     return (
        <div className="flex flex-col items-center justify-start p-8 min-h-screen bg-gray-100 text-gray-900">
            <div className="flex flex-col gap-6 w-full max-w-4xl">
                <TextField
                    label="Título da Entrevista"
                    variant="outlined"
                    style={{ backgroundColor: 'white' }}
                    fullWidth
                    value={formData.title || ''}
                    onChange={(e) => handleChange('title', e.target.value)}
                />

                <div>
                    <h2 className="text-2xl font-semibold">Grupo: {groupName}</h2>
                </div>

                {formData.transcription && (
                     <div>
                        <h2 className="text-2xl font-semibold">Transcrição</h2>
                        <div className="mt-2 border rounded p-4 whitespace-pre-line bg-white text-gray-800 shadow-sm max-h-60 overflow-y-auto">
                            {formData.transcription}
                        </div>
                    </div>
                )}
               
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <Card>
                        <CardContent className="p-4">
                            <h3 className="font-semibold text-lg mb-2">Resumo (LLM)</h3>
                            <div className="list-disc pl-4 text-gray-700 max-h-60 overflow-y-auto">
                                {formData.summary?.split('\n').map((line, index) => (
                                    <div key={index} className="mb-1">
                                        {line.trim()}
                                    </div>
                                ))}
                            </div>
                        </CardContent>
                    </Card>
                    <Card>
                        <CardContent className="p-4">
                            <h3 className="font-semibold text-lg mb-2">Anotações</h3>
                            <TextareaAutosize
                                minRows={4}
                                className="w-full p-2 border rounded shadow-sm mt-2"
                                style={{ resize: 'none', backgroundColor: 'white' }}
                                value={formData.annotations || ''}
                                onChange={(e) => handleChange('annotations', e.target.value)}
                                placeholder="Escreva suas anotações aqui..."
                            />
                        </CardContent>
                    </Card>
                </div>
                <div className="flex justify-between items-center mt-4">
                    <Button 
                        variant="outlined" 
                        color="error" 
                        onClick={handleDelete}
                    >
                        Excluir
                    </Button>
                    <div className='flex gap-4'>
                        <Button onClick={() => router.back()}>Cancelar</Button>
                        <Button variant="contained" onClick={handleUpdate}>
                            Salvar Alterações
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
}