'use client';
import React, { useEffect, useState } from "react";
import {
    Box,
    Typography,
    Paper,
    IconButton,
    Button,
    TextField,
    InputAdornment,
} from "@mui/material";
import Grid from "@mui/material/Grid";
import DescriptionIcon from "@mui/icons-material/Description";
import SearchIcon from "@mui/icons-material/Search";
import Link from "next/link";
import { Item } from "@/lib/types";
import { loadReunioes } from "@/lib/utils";
import ItemGrid from "@/components/ItemGrid";
import { createGroup, fetchReunioes } from "@/lib/api";
import { useRouter } from 'next/navigation';

// Tela de criacao de grupos
type Reuniao = {
    id: number;
    title: string;
    transcription: string;
    summary: string;
    annotations: string;
};

type Grupo = {
    id: string;
    title: string;
    meetings: string[];
};

type ItemGridProps = {
    id: string,
    title: string;
    items: Item[];
    onItemClick?: (id: string) => void;
    itemHref?: (id: string) => string;
    createHref?: string;
    createLabel?: string;
    searchPlaceholder?: string;
};

export default function CreateGroup() {
    const [busca, setBusca] = useState("");
    const [reunioes, setReunioes] = useState<Item[]>([]);
    const [grupos, setGrupos] = useState<Item[]>([]);
    const [reunioesSelecionadas, setReunioesSelecionadas] = useState<number[]>([]);
    const [status, setStatus] = useState<'select' | 'create' | 'search'>("select");
    const [formData, setFormData] = useState<Grupo>({ id: "", title: "", meetings: [] });
    const router = useRouter();

    useEffect(() => {
        loadReunioes(setReunioes);
    }, []);

    // Handles the changes made to the current Group object
    const handleChange = (field: keyof Grupo, value: string) => {
        setFormData({ ...formData, [field]: value });
    };

    const handleCreate = async () => {
        try {
            console.log('FormData:', formData);
            const createGroupReq = { groupName: formData.title, meetingIds: formData.meetings };
            console.log(`handleCreate: ${createGroupReq} `)
            await createGroup(createGroupReq);
            setFormData({ id: '', title: '', meetings: [] });
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao criar grupo:', err);
        }
    };

    return (
        <div className="flex flex-col gap-6 w-full max-w-4xl">
            <div >
                <TextField
                    label="Nome do Grupo"
                    variant="outlined"
                    style={{ backgroundColor: 'white' }}
                    fullWidth
                    value={formData.title}
                    onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                />
                <strong className="text-gray-700">Reunioes adicionadas</strong>
                <ul>
                    {formData.meetings.map((meetingId) => (
                        <li key={meetingId} className="list-disc pl-4 text-gray-700">
                            {reunioes.find(meeting => meeting.id === Number(meetingId))?.title || "Entrevista não encontrada"}
                        </li>
                    ))}
                </ul>
                <div>
                    <ItemGrid
                        title="Selecione Reunioes" items={reunioes}
                        onItemClick={(id) => {
                            if (formData.meetings.find((meetingId) => meetingId == id) == undefined) {
                                setFormData({ ...formData, meetings: [...formData.meetings, id] });
                            }
                        }}
                        createLabel="Criar novo grupo"
                        searchPlaceholder="Buscar grupo..."
                        emptyMessage="Nao existem grupos."
                    />
                </div>
                <Button variant="contained" onClick={handleCreate}>Criar</Button>
            </div>
        </div>

    )
}
