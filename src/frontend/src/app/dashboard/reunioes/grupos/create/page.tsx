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

type Groupcontent = {
    candidates: string[];
    interviewers: string[];
    evaluations: string[];
    processBeginDate: string;
    processEndDate: string;
};

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
        content: {
            candidates: [],
            interviewers: [],
            evaluations: [],
            processBeginDate: "",
            processEndDate: "",
        },
    });
    
    // Estados para os campos de texto e a lista de reuniões disponíveis
    const [reunioes, setReunioes] = useState<Item[]>([]);
    const [currentItem, setCurrentItem] = useState({
        candidate: "",
        interviewer: "",
        evaluation: "",
    });

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

    const handleCurrentItemChange = (field: keyof typeof currentItem, value: string) => {
        setCurrentItem(prev => ({ ...prev, [field]: value }));
    };

    const addToArray = (field: 'candidates' | 'interviewers' | 'evaluations', itemKey: keyof typeof currentItem) => {
        const value = currentItem[itemKey].trim();
        if (value === '') return;
        handleFormChange(field, [...formData.content[field], value], true);
        setCurrentItem(prev => ({ ...prev, [itemKey]: '' }));
    };

    const removeFromArray = (field: 'candidates' | 'interviewers' | 'evaluations', indexToRemove: number) => {
        const updatedArray = formData.content[field].filter((_, index) => index !== indexToRemove);
        handleFormChange(field, updatedArray, true);
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
    
    // Função auxiliar para renderizar os campos de lista
    const renderListInput = (
        field: 'candidates' | 'interviewers' | 'evaluations',
        itemKey: keyof typeof currentItem,
        label: string
    ) => (
        <Paper elevation={2} className="p-4">
            <Typography variant="h6" className="mb-2">{label}</Typography>
            <TextField
                label={`Adicionar ${label.slice(0, -1)}`}
                variant="outlined"
                fullWidth
                value={currentItem[itemKey]}
                onChange={(e) => handleCurrentItemChange(itemKey, e.target.value)}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            <IconButton color="primary" onClick={() => addToArray(field, itemKey)}>
                                <AddCircleIcon />
                            </IconButton>
                        </InputAdornment>
                    )
                }}
            />
            <Box className="flex flex-wrap gap-2 mt-3">
                {formData.content[field].map((item, index) => (
                    <Chip key={index} label={item} onDelete={() => removeFromArray(field, index)} />
                ))}
            </Box>
        </Paper>
    );

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
                <Typography variant="h6" className="mb-2">Datas do Processo</Typography>
                <Box className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <TextField label="Data de Início" type="date" InputLabelProps={{ shrink: true }} value={formData.content.processBeginDate} onChange={(e) => handleFormChange('processBeginDate', e.target.value, true)} />
                    <TextField label="Data de Fim" type="date" InputLabelProps={{ shrink: true }} value={formData.content.processEndDate} onChange={(e) => handleFormChange('processEndDate', e.target.value, true)} />
                </Box>
            </Paper>
            
            {renderListInput('candidates', 'candidate', 'Candidatos')}
            {renderListInput('interviewers', 'interviewer', 'Entrevistadores')}
            {renderListInput('evaluations', 'evaluation', 'Avaliações (Tipos)')}
            
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