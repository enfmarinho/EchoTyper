'use client'
import React, { useState } from 'react';
import {
  Box,
  Tabs,
  Tab,
  Typography,
  Switch,
  FormControlLabel,
  Button,
  Avatar,
  TextField,
} from '@mui/material';
import UserTable from '../../../components/UserTable';

type User = {
  nome: string;
  email: string;
  role: 'admin' | 'user';
};

export default function ConfiguracoesPage() {
  const [tab, setTab] = useState(0);
  const [darkMode, setDarkMode] = useState(false);

  // Simulação de autenticação
  const isAdmin = true;

  const handleTabChange = (_: React.SyntheticEvent, newValue: number) => {
    setTab(newValue);
  };

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" gutterBottom sx={{ fontWeight: 'bold', color: '#0D1B2A' }}>
        Configurações
      </Typography>

      <Tabs value={tab} onChange={handleTabChange} sx={{ mb: 3 }}>
        <Tab label="Geral" />
        <Tab label="Perfil" />
        {isAdmin && <Tab label="Usuários" />}
      </Tabs>

      {tab === 0 && (
        <Box>
          <FormControlLabel
            control={<Switch checked={darkMode} onChange={() => setDarkMode(!darkMode)} />}
            label="Modo escuro"
            slotProps={{
              typography: {
                sx: { color: darkMode ? '#fff' : '#000' },
              },
            }}
          />
        </Box>
      )}

      {tab === 1 && (
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, maxWidth: 400 }}>
          <Avatar sx={{ width: 80, height: 80 }} />
          <TextField label="Nome" defaultValue="Fulano da Silva" />
          <TextField label="Email" defaultValue="fulano@email.com" />
          <Button variant="contained">Salvar alterações</Button>
        </Box>
      )}

      {tab === 2 && isAdmin && (
        <UserTable />
      )}
    </Box>
  );
}
