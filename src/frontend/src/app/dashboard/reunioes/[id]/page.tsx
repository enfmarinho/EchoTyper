'use client'
import { useState, useEffect } from 'react';
import { useRouter, useParams } from 'next/navigation';
import { Card, CardContent, Button, TextField, Chip, IconButton } from '@mui/material';
import TextareaAutosize from '@mui/material/TextareaAutosize';
import CloseIcon from '@mui/icons-material/Close';
import { createReuniao, updateReuniao, deleteReuniao, fetchReuniaoById, fetchGroupById } from '@/lib/api';

// 1. Tipos atualizados para incluir 'participants'
type ReuniaoContent = {
    participants?: string[]; // Array de strings para os participantes
};

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
    const params = useParams();
    const [formData, setFormData] = useState<Partial<Reuniao>>({
        title: '',
        transcription: '',
        summary: '',
        annotations: '',
        groupId: '',
        content: {
            participants: [] // Inicializa como um array vazio
        }
    });
    const [groupName, setGroupName] = useState<string>("");
    const [newParticipant, setNewParticipant] = useState<string>(""); // Estado para o input de novo participante

    useEffect(() => {
        const loadReuniaoAndGroup = async () => {
            if (params.id) {
                try {
                    const reuniaoData = await fetchReuniaoById(Number(params.id));
                    // Garante que 'participants' seja sempre um array
                    if (reuniaoData.content && !reuniaoData.content.participants) {
                        reuniaoData.content.participants = [];
                    }
                    setFormData(reuniaoData);

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

    const handleChange = (field: keyof Omit<Reuniao, 'content'>, value: string) => {
        setFormData({ ...formData, [field]: value });
    };

    // 2. Funções para gerenciar a lista de participantes
    const handleAddParticipant = () => {
        if (newParticipant.trim() !== "") {
            const currentParticipants = formData.content?.participants || [];
            setFormData({
                ...formData,
                content: {
                    ...formData.content,
                    participants: [...currentParticipants, newParticipant.trim()]
                }
            });
            setNewParticipant(""); // Limpa o input
        }
    };

    const handleRemoveParticipant = (indexToRemove: number) => {
        const currentParticipants = formData.content?.participants || [];
        setFormData({
            ...formData,
            content: {
                ...formData.content,
                participants: currentParticipants.filter((_, index) => index !== indexToRemove)
            }
        });
    };

    return (
        <div className="flex flex-col items-center justify-start p-8 min-h-screen bg-gray-100 text-gray-900">
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
                    <h2 className="text-xl font-semibold">Grupo: {groupName}</h2>
                </div>

                {/* 3. Nova seção para gerenciar participantes */}
                <Card>
                    <CardContent>
                        <h3 className="font-semibold text-lg mb-2">Participantes</h3>
                        <div className="flex items-center gap-2 mb-4">
                            <TextField
                                label="Adicionar participante"
                                variant="outlined"
                                size="small"
                                style={{ backgroundColor: 'white' }}
                                value={newParticipant}
                                onChange={(e) => setNewParticipant(e.target.value)}
                                onKeyPress={(e) => e.key === 'Enter' && handleAddParticipant()}
                                fullWidth
                            />
                            <Button variant="contained" onClick={handleAddParticipant}>Adicionar</Button>
                        </div>
                        <div className="flex flex-wrap gap-2">
                            {formData.content?.participants?.map((participant, index) => (
                                <Chip
                                    key={index}
                                    label={participant}
                                    onDelete={() => handleRemoveParticipant(index)}
                                    deleteIcon={<CloseIcon />}
                                />
                            ))}
                        </div>
                    </CardContent>
                </Card>

                {formData.transcription && (
                     <div>
                        <h2 className="text-xl font-semibold">Transcrição</h2>
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
                                    <div key={index} className="mb-1">{line.trim()}</div>
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