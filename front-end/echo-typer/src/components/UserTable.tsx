import React, { useState } from 'react';
import {
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  IconButton,
  TextField,
  Button,
  Box,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';

type Usuario = {
  id: number;
  nome: string;
  email: string;
};

const UserTable = () => {
  const [usuarios, setUsuarios] = useState<Usuario[]>([
    { id: 1, nome: 'Admin', email: 'admin@email.com' },
    { id: 2, nome: 'Usuário 1', email: 'user1@email.com' },
  ]);

  const [novoNome, setNovoNome] = useState('');
  const [novoEmail, setNovoEmail] = useState('');

  const adicionarUsuario = () => {
    const novo = {
      id: Date.now(),
      nome: novoNome,
      email: novoEmail,
    };
    setUsuarios([...usuarios, novo]);
    setNovoNome('');
    setNovoEmail('');
  };

  const excluirUsuario = (id: number) => {
    setUsuarios(usuarios.filter((u) => u.id !== id));
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
        <TextField
          label="Nome"
          value={novoNome}
          onChange={(e) => setNovoNome(e.target.value)}
        />
        <TextField
          label="Email"
          value={novoEmail}
          onChange={(e) => setNovoEmail(e.target.value)}
        />
        <Button variant="contained" onClick={adicionarUsuario}>
          Adicionar
        </Button>
      </Box>

      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Nome</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Ações</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {usuarios.map((usuario) => (
            <TableRow key={usuario.id}>
              <TableCell>{usuario.nome}</TableCell>
              <TableCell>{usuario.email}</TableCell>
              <TableCell>
                <IconButton color="primary">
                  <EditIcon />
                </IconButton>
                <IconButton color="error" onClick={() => excluirUsuario(usuario.id)}>
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Box>
  );
};

export default UserTable;
