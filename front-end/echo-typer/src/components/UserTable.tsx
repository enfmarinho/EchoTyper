'use client';

import React, { useEffect, useState } from 'react';
import {
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
  Paper, IconButton, TextField, Button, Box
} from '@mui/material';
import { Delete, Edit, Save } from '@mui/icons-material';
import {
  fetchUsers, createUser, updateUser, deleteUser
} from '../lib/api';

type User = {
  id: number;
  username: string;
  email: string;
  password: string;
  registrationDate: string;
};

export default function UserTable() {
  const [users, setUsers] = useState<User[]>([]);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [formData, setFormData] = useState<Partial<User>>({});

  const loadUsers = async () => {
    try {
      const data = await fetchUsers();
      setUsers(data);
    } catch (err) {
      console.error('Erro ao carregar usuários:', err);
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  const handleChange = (field: keyof User, value: string) => {
    setFormData({ ...formData, [field]: value });
  };

  const handleEdit = (user: User) => {
    setEditingId(user.id);
    setFormData({
      username: user.username,
      email: user.email,
      password: user.password
    });
  };

  const handleSave = async () => {
    if (!editingId) return;

    try {
      await updateUser(editingId, formData);
      setEditingId(null);
      await loadUsers();
    } catch (err) {
      console.error('Erro ao atualizar usuário:', err);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteUser(id);
      await loadUsers();
    } catch (err) {
      console.error('Erro ao deletar usuário:', err);
    }
  };

  const handleCreate = async () => {
    try {
      await createUser(formData);
      setFormData({});
      await loadUsers();
    } catch (err) {
      console.error('Erro ao criar usuário:', err);
    }
  };

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Nome de Usuário</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Data de Registro</TableCell>
            <TableCell>Ações</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {users.map((user) => (
            <TableRow key={user.id}>
              <TableCell>
                {editingId === user.id ? (
                  <TextField
                    value={formData.username || ''}
                    onChange={(e) => handleChange('username', e.target.value)}
                  />
                ) : (
                  user.username
                )}
              </TableCell>
              <TableCell>
                {editingId === user.id ? (
                  <TextField
                    value={formData.email || ''}
                    onChange={(e) => handleChange('email', e.target.value)}
                  />
                ) : (
                  user.email
                )}
              </TableCell>
              <TableCell>
                {editingId === user.id ? (
                  <TextField
                    value={formData.password || ''}
                    onChange={(e) => handleChange('password', e.target.value)}
                  />
                ) : (
                  user.registrationDate
                )}
              </TableCell>
              <TableCell>
                {editingId === user.id ? (
                  <IconButton onClick={handleSave}><Save /></IconButton>
                ) : (
                  <>
                    <IconButton onClick={() => handleEdit(user)}><Edit /></IconButton>
                    <IconButton onClick={() => handleDelete(user.id)}><Delete /></IconButton>
                  </>
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Box sx={{ p: 2, display: 'flex', gap: 2 }}>
        <TextField
          label="Username"
          value={formData.username || ''}
          onChange={(e) => handleChange('username', e.target.value)}
        />
        <TextField
          label="Email"
          value={formData.email || ''}
          onChange={(e) => handleChange('email', e.target.value)}
        />
        <TextField
          label="Password"
          type="password"
          value={formData.password || ''}
          onChange={(e) => handleChange('password', e.target.value)}
        />
        <Button variant="contained" onClick={handleCreate}>Adicionar</Button>
      </Box>
    </TableContainer>
  );
}
