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

export const fetchReunioes = async () => {
  const res = await fetch(`${API_BASE}/meetings`);
  if (!res.ok) throw new Error('Erro ao buscar reuniões');
  return res.json();
}

export const fetchReuniaoByUserId = async (id: number) => {
  const res = await fetch(`${API_BASE}/meetings/user/${id}`);
  if (!res.ok) throw new Error('Erro ao buscar reuniões por ID de usuário');
  return res.json();
};

export const fetchReuniaoById = async (id: number) => {
  const res = await fetch(`${API_BASE}/meetings/${id}`);
  if (!res.ok) throw new Error('Erro ao buscar reunião por ID');
  return res.json();
};

export const createReuniao = async (reuniao: any) => {
  const res = await fetch(`${API_BASE}/meetings/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(reuniao),
  });
  if (!res.ok) throw new Error('Erro ao criar reunião');
  return res.json();
};

export const updateReuniao = async (id: number, reuniao: any) => {
  console.log('Reunião a ser atualizada:', reuniao);
  const res = await fetch(`${API_BASE}/meetings/update/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(reuniao),
  });
  if (!res.ok) throw new Error('Erro ao atualizar reunião');
  return res.json();
};

export const deleteReuniao = async (id: number) => {
  const res = await fetch(`${API_BASE}/meetings/delete/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Erro ao deletar reunião');
  return res.ok;
};

export const fetchGroups = async () => {
  const res = await fetch(`${API_BASE}/groups`);
  if (!res.ok) throw new Error('Erro ao buscar grupos');
  return res.json();
};

export const fetchGroupById = async (id: number) => {
  const res = await fetch(`${API_BASE}/groups/${id}`);
  if (!res.ok) throw new Error('Erro ao buscar grupo por ID');
  return res.json();
};

export const createGroup = async (group: any) => {
  const res = await fetch(`${API_BASE}/groups/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(group),
  });
  if (!res.ok) throw new Error('Erro ao criar grupo');
  return res.json();
};

export const removeMeetingFromGroup = async (meetingId: number, groupId: number) => {
  const res = await fetch(`${API_BASE}/groups/remove/${meetingId}/${groupId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
  });
  if (!res.ok) throw new Error('Erro ao remover reuniao do grupo');
  return res.json();
};

export const addMeetingFromGroup = async (meetingId: number, groupId: number) => {
  const res = await fetch(`${API_BASE}/groups/add/${meetingId}/${groupId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
  });
  if (!res.ok) throw new Error('Erro ao adicionar reuniao do grupo');
  return res.json();
};

export const deleteGroup = async (id: number) => {
  const res = await fetch(`${API_BASE}/groups/delete/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Erro ao deletar grupo');
  return res.ok;
};