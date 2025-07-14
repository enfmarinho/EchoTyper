'use client';
import React, { useState, useEffect } from "react";
import {
    Box,
    Typography,
    Paper,
    IconButton,
    Button,
    TextField,
    InputAdornment,
    Chip,
} from "@mui/material";
import AddCircleIcon from '@mui/icons-material/AddCircle';
import { createGroup } from "@/lib/api";
import { useRouter } from 'next/navigation';
import { Item } from "@/lib/types";
import { loadReunioes } from "@/lib/utils";
import ItemGrid from "@/components/ItemGrid";

type Groupcontent = {};

type Grupo = {
    groupName: string;
    register: string[];
    content: Groupcontent;
};

export default function CreateGroup() {
    const router = useRouter();

    const [formData, setFormData] = useState<Grupo>({
        groupName: "",
        register: [], // Array para os IDs das reuniões/registros
        content: {},
    });
    
    // Estados para os campos de texto e a lista de reuniões disponíveis
    const [reunioes, setReunioes] = useState<Item[]>([]);

    // Carrega as reuniões disponíveis para seleção
    useEffect(() => {
        loadReunioes(setReunioes);
    }, []);

    // Handlers para os campos aninhados e de nível superior
    const handleFormChange = (field: keyof Grupo | keyof Groupcontent, value: any, isContentField = false) => {
        if (isContentField) {
            setFormData(prev => ({
                ...prev,
                content: { ...prev.content, [field as keyof Groupcontent]: value },
            }));
        } else {
            setFormData(prev => ({ ...prev, [field as keyof Grupo]: value }));
        }
    };

    const handleSelectReuniao = (id: string) => {
        if (!formData.register.includes(id)) {
            handleFormChange('register', [...formData.register, id]);
        }
    };

    const handleCreate = async () => {
        try {
            const createGroupReq = {
                groupName: formData.groupName,
                registerIds: formData.register, // Correct key
                content: formData.content
            };

            console.log('Criando grupo com os seguintes dados:', createGroupReq);
            await createGroup(createGroupReq); // The createGroup function in api.ts will send this object
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao criar grupo:', err);
        }
    };

    return (
        <Box className="flex flex-col gap-6 w-full max-w-4xl p-4">
            <Typography variant="h4" className="font-bold text-gray-800">Criar Novo Grupo de Processo</Typography>
            
            <Paper elevation={2} className="p-4">
                <TextField
                    label="Nome do Grupo"
                    variant="outlined"
                    fullWidth
                    value={formData.groupName}
                    onChange={(e) => handleFormChange('groupName', e.target.value)}
                />
            </Paper>
            
            <Paper elevation={2} className="p-4">
                 <Typography variant="h6" className="mb-2">Associar Registros Existentes</Typography>
                 <Typography variant="body2" color="textSecondary" className="mb-2">
                    Registros selecionados: {formData.register.length}
                 </Typography>
                 <ItemGrid
                    title=""
                    items={reunioes}
                    onItemClick={handleSelectReuniao}
                    searchPlaceholder="Buscar registro..."
                    emptyMessage="Nenhum registro encontrado."
                />
            </Paper>

            <Box className="flex justify-end mt-4">
                <Button variant="contained" color="primary" size="large" onClick={handleCreate}>
                    Criar Grupo
                </Button>
            </Box>
        </Box>
    );
}