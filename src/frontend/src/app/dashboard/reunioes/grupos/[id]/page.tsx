
'use client'
import { useState, useEffect, use } from 'react';
import TextareaAutosize from '@mui/material/TextareaAutosize';
import { Card, CardContent, Button, TextField, Grid, Paper, IconButton, Typography } from '@mui/material';
import { updateGroup, createGroup, removeMeetingFromGroup, addMeetingFromGroup, deleteGroup, fetchGroupById, fetchReuniaoByGroup, fetchReuniaoById, fetchReunioes } from '@/lib/api';
import UploadIcon from '@mui/icons-material/Upload';
import DescriptionIcon from "@mui/icons-material/Description";
import { useRouter, useParams } from 'next/navigation';
import ItemGrid from '@/components/ItemGrid';
import { Item } from '@/lib/types';

type Reuniao = {
    id: number;
    title: string;
    transcription: string;
    summary: string;
    annotations: string;
};

type Grupo = {
    id: number;
    groupName: string;
    meetings: Reuniao[];
};

export default function GrupoPage() {
    const router = useRouter();
    const [outrasReunioes, setOutrasReunioes] = useState<Item[]>([]);
    const [formData, setFormData] = useState<Partial<Grupo>>({ groupName: '', meetings: [] });
    const params = useParams();
    const isEditing = !!params.id;

    // Carrega todas as reunioes em seus respectivos estados: as que pertencem a reuniao atual e as que nao
    const loadData = async () => {
        if (params.id) {
            fetchGroupById(Number(params.id)).then(data => {
                setFormData({
                    id: Number(data.id),
                    groupName: data.groupName,
                    meetings: data.meetings
                });
            }).catch(err => {
                console.error('Erro ao carregar aula:', err);
            });
            fetchReunioes().then(data => {
                setOutrasReunioes(data.filter(meeting => (meeting.groupId == undefined)));
            }).catch(err => {
                console.error('Erro ao carregar aula:', err);
            });
            formData.meetings?.filter(meeting => (meeting.id))
        }
    };

    const adicionarReuniao = async (meetingId: number) => {
        try {
            await addMeetingFromGroup(meetingId, params.id);
            loadData();
        } catch (err) {
            console.error('Erro ao atualizar grupo:', err);
        }
    }

    const removerReuniao = async (reuniaoId: number) => {
        try {
            await removeMeetingFromGroup(reuniaoId, Number(params.id)!);
            // Provavelmente errado
            loadData();
        } catch (err) {
            console.error('Erro ao remover reuniao', err);
        }
    }

    const handleDelete = async () => {
        try {
            await deleteGroup(Number(params.id)!);
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao excluir grupo:', err);
        }
    };

    const handleUpdate = async () => {
        try {
            const formattedUpdate = { groupName: formData.groupName, meetingIds: formData.meetings?.map(meeting => (meeting.id)) }
            await updateGroup(Number(params.id)!, formattedUpdate);
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao atualizar grupo:', err);
        }
    };

    const handleChange = (field: keyof Grupo, value: string) => {
        setFormData({ ...formData, [field]: value });
    };

    useEffect(() => {
        loadData();
    }, []);

    const renderContent = () => {
        if (isEditing) {
            return (
                <div>
                    <div className="flex flex-col gap-6 w-full max-w-4xl">
                        <div className="flex flex-col gap-6 w-full max-w-4xl">
                            <TextField
                                label="Nome do Grupo"
                                variant="outlined"
                                style={{ backgroundColor: 'white' }}
                                fullWidth
                                value={formData.groupName || ''}
                                onChange={(e) => handleChange('groupName', e.target.value)}
                            />
                            <div>
                            </div>
                        </div>
                        <h1>Reunioes</h1>
                        <Grid container spacing={2}>
                            {formData.meetings?.map((item) => (
                                <Grid xs={6} sm={4} md={2.4} key={item.id} >
                                    <Paper
                                        ux={{
                                            height: 120,
                                            display: "flex",
                                            justifyContent: "center",
                                            alignItems: "center",
                                            flexDirection: "column",
                                            border: "1px solid #c4c4c4",
                                            borderRadius: 2,
                                            cursor: "pointer",
                                            transition: "0.2s",
                                            "&:hover": {
                                                backgroundColor: "#f0f0f0",
                                            },
                                        }}
                                    >
                                        <IconButton>
                                            <DescriptionIcon sx={{ fontSize: 36 }} />
                                        </IconButton>
                                        <Typography variant="body2" textAlign="center">
                                            {item.title}
                                        </Typography>
                                    </Paper>
                                    <Button onClick={() => removerReuniao(item.id)}>Remover</Button>
                                </Grid>
                            ))}

                        </Grid>
                        <div>
                            <h1>Adicionar Reunioes</h1>
                            <ItemGrid
                                items={outrasReunioes}
                                onItemClick={(id) => {
                                    adicionarReuniao(Number(id));
                                }}
                                createLabel="Criar novo grupo"
                                searchPlaceholder="Buscar grupo..."
                            />
                            <div className="flex gap-4">
                                <Button variant="contained" onClick={handleUpdate}>Salvar Alterações</Button>
                                <Button variant="outlined" color="error" onClick={handleDelete}>Excluir</Button>
                            </div>
                        </div>
                    </div>
                </div>
            );


        }
    }
    return (
        <div className="flex flex-col items-center justify-start p-8 min-h-screen bg-gray-100 text-gray-900">
            {renderContent()}
        </div>
    );
}
