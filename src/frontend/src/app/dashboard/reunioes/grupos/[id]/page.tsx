'use client'
import { useState, useEffect } from 'react';
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
import { updateGroup, deleteGroup, fetchGroupById, fetchReunioes } from '@/lib/api';
import { useRouter, useParams } from 'next/navigation';
import { Item } from '@/lib/types';
import ItemGrid from '@/components/ItemGrid';

type Groupcontent = {
    candidates: string[];
    interviewers: string[];
    evaluations: string[];
    processBeginDate: string;
    processEndDate: string;
};

type Grupo = {
    id: number;
    groupName: string;
    register: string[];
    content: Groupcontent;
};

export default function GrupoPage() {
    const router = useRouter();
    const params = useParams();

    const [formData, setFormData] = useState<Partial<Grupo>>({
        groupName: "",
        register: [],
        content: {
            candidates: [],
            interviewers: [],
            evaluations: [],
            processBeginDate: "",
            processEndDate: "",
        },
    });

    const [allReunioes, setAllReunioes] = useState<Item[]>([]);
    const [currentItem, setCurrentItem] = useState({
        candidate: "",
        interviewer: "",
        evaluation: "",
    });

    useEffect(() => {
        const loadData = async () => {
            if (params.id) {
                try {
                    // Carrega os dados do grupo específico
                    const groupData = await fetchGroupById(Number(params.id));
                    setFormData(groupData);

                    // Carrega todas as reuniões para a seleção
                    const allReunioesData = await fetchReunioes();
                    setAllReunioes(allReunioesData);

                } catch (err) {
                    console.error('Erro ao carregar dados:', err);
                }
            }
        };
        loadData();
    }, [params.id]);


    const handleFormChange = (field: keyof Grupo | keyof Groupcontent, value: any, isContentField = false) => {
        setFormData(prev => {
            if (isContentField) {
                return {
                    ...prev,
                    content: {
                        candidates: prev.content?.candidates ?? [],
                        interviewers: prev.content?.interviewers ?? [],
                        evaluations: prev.content?.evaluations ?? [],
                        processBeginDate: prev.content?.processBeginDate ?? "",
                        processEndDate: prev.content?.processEndDate ?? "",
                        [field as keyof Groupcontent]: value
                    }
                };
            }
            return { ...prev, [field as keyof Grupo]: value };
        });
    };

    const handleCurrentItemChange = (field: keyof typeof currentItem, value: string) => {
        setCurrentItem(prev => ({ ...prev, [field]: value }));
    };

    const addToArray = (field: 'candidates' | 'interviewers' | 'evaluations', itemKey: keyof typeof currentItem) => {
        const value = currentItem[itemKey].trim();
        if (value === '' || !formData.content) return;
        handleFormChange(field, [...formData.content[field], value], true);
        setCurrentItem(prev => ({ ...prev, [itemKey]: '' }));
    };

    const removeFromArray = (field: 'candidates' | 'interviewers' | 'evaluations', indexToRemove: number) => {
        if (!formData.content) return;
        const updatedArray = formData.content[field].filter((_, index) => index !== indexToRemove);
        handleFormChange(field, updatedArray, true);
    };

    const handleToggleReuniaoInRegister = (id: string) => {
        if (!formData.register) return;
        const updatedRegister = formData.register.includes(id)
            ? formData.register.filter(regId => regId !== id) // Remove
            : [...formData.register, id]; // Adiciona
        handleFormChange('register', updatedRegister);
    };

    const handleDelete = async () => {
        if (window.confirm("Tem certeza que deseja excluir este grupo?")) {
            try {
                await deleteGroup(Number(params.id)!);
                router.push('/dashboard/reunioes');
            } catch (err) {
                console.error('Erro ao excluir grupo:', err);
            }
        }
    };

    const handleUpdate = async () => {
        try {
            await updateGroup(Number(params.id)!, formData);
            router.push('/dashboard/reunioes');
        } catch (err) {
            console.error('Erro ao atualizar grupo:', err);
        }
    };
    
    // Filtra as reuniões que ainda não estão no grupo para exibir na lista de adição
    const availableReunioes = allReunioes.filter(reuniao => !formData.register?.includes(String(reuniao.id)));

    // Função auxiliar para renderizar os campos de lista
    const renderListInput = (field: 'candidates' | 'interviewers' | 'evaluations', itemKey: keyof typeof currentItem, label: string) => (
        <Paper elevation={2} className="p-4">
            <Typography variant="h6" className="mb-2">{label}</Typography>
            <TextField label={`Adicionar ${label.slice(0, -1)}`} variant="outlined" fullWidth value={currentItem[itemKey]} onChange={(e) => handleCurrentItemChange(itemKey, e.target.value)}
                InputProps={{
                    endAdornment: (<InputAdornment position="end"><IconButton color="primary" onClick={() => addToArray(field, itemKey)}><AddCircleIcon /></IconButton></InputAdornment>)
                }} />
            <Box className="flex flex-wrap gap-2 mt-3">
                {formData.content?.[field]?.map((item, index) => (<Chip key={index} label={item} onDelete={() => removeFromArray(field, index)} />))}
            </Box>
        </Paper>
    );

    return (
        <Box className="flex flex-col items-center justify-start p-4 md:p-8 min-h-screen bg-gray-100">
            <Box className="flex flex-col gap-6 w-full max-w-4xl">
                <Typography variant="h4" className="font-bold text-gray-800">Editar Grupo de Processo</Typography>

                <Paper elevation={2} className="p-4">
                    <TextField label="Nome do Grupo" variant="outlined" fullWidth value={formData.groupName || ''} onChange={(e) => handleFormChange('groupName', e.target.value)} />
                </Paper>

                <Paper elevation={2} className="p-4">
                    <Typography variant="h6" className="mb-2">Datas do Processo</Typography>
                    <Box className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <TextField label="Data de Início" type="date" InputLabelProps={{ shrink: true }} value={formData.content?.processBeginDate || ''} onChange={(e) => handleFormChange('processBeginDate', e.target.value, true)} />
                        <TextField label="Data de Fim" type="date" InputLabelProps={{ shrink: true }} value={formData.content?.processEndDate || ''} onChange={(e) => handleFormChange('processEndDate', e.target.value, true)} />
                    </Box>
                </Paper>
                
                {renderListInput('candidates', 'candidate', 'Candidatos')}
                {renderListInput('interviewers', 'interviewer', 'Entrevistadores')}
                {renderListInput('evaluations', 'evaluation', 'Avaliações (Tipos)')}
                
                <Paper elevation={2} className="p-4">
                    <Typography variant="h6" className="mb-2">Associar Registros (Reuniões)</Typography>
                    <Box className="mb-4">
                        <Typography variant="subtitle1">Registros Atuais:</Typography>
                        <Box className="flex flex-wrap gap-2 mt-2">
                            {formData.register?.length === 0 && <Typography color="textSecondary">Nenhum registro associado.</Typography>}
                            {allReunioes.filter(r => formData.register?.includes(String(r.id))).map(item => (
                                <Chip key={item.id} label={item.title} onDelete={() => handleToggleReuniaoInRegister(String(item.id))} />
                            ))}
                        </Box>
                    </Box>
                    <ItemGrid title="Adicionar Registros Disponíveis" items={availableReunioes} onItemClick={(id) => handleToggleReuniaoInRegister(id)} searchPlaceholder="Buscar registro..." />
                </Paper>

                <Box className="flex justify-between items-center mt-4">
                    <Button variant="outlined" color="error" onClick={handleDelete}>Excluir Grupo</Button>
                    <Button variant="contained" color="primary" size="large" onClick={handleUpdate}>Salvar Alterações</Button>
                </Box>
            </Box>
        </Box>
    );
}