const API_BASE = 'http://localhost:8081';

export const fetchUsers = async () => {
  const res = await fetch(`${API_BASE}/user`);
  if (!res.ok) throw new Error('Erro ao buscar usuários');
  return res.json();
};

export const fetchUserById = async (id: number) => {
  const res = await fetch(`${API_BASE}/user/${id}`);
  if (!res.ok) throw new Error('Erro ao buscar usuário por ID');
  return res.json();
};

export const fetchUserByUsername = async (username: string) => {
  const res = await fetch(`${API_BASE}/user/name/${username}`);
  if (!res.ok) throw new Error('Erro ao buscar usuário por nome');
  return res.json();
};

export const fetchUserFriends = async (id: number) => {
  const res = await fetch(`${API_BASE}/user/${id}/friends`);
  if (!res.ok) throw new Error('Erro ao buscar amigos do usuário');
  return res.json();
};

export const createUser = async (user: any) => {
  const res = await fetch(`${API_BASE}/user/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(user),
  });
  if (!res.ok) throw new Error('Erro ao criar usuário');
  return res.json();
};

export const updateUser = async (id: number, user: any) => {
  const res = await fetch(`${API_BASE}/user/update/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(user),
  });
  if (!res.ok) throw new Error('Erro ao atualizar usuário');
  return res.json();
};

export const deleteUser = async (id: number) => {
  const res = await fetch(`${API_BASE}/user/delete/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Erro ao deletar usuário');
  return res.ok;
};

export const addFriend = async (id: number, friendId: number) => {
  const res = await fetch(`${API_BASE}/user/addfriend/${id}/${friendId}`, {
    method: 'POST',
  });
  if (!res.ok) throw new Error('Erro ao adicionar amigo');
  return res.json();
};
