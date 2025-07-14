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
  const res = await fetch(`${API_BASE}/register`);
  if (!res.ok) throw new Error('Erro ao buscar entrevistas');
  return res.json();
}

export const fetchReuniaoByUserId = async (id: number) => {
  const res = await fetch(`${API_BASE}/register/user/${id}`);
  if (!res.ok) throw new Error('Erro ao buscar entrevistas por ID de usuário');
  return res.json();
};

export const fetchReuniaoById = async (id: number) => {
  const res = await fetch(`${API_BASE}/register/${id}`);
  if (!res.ok) throw new Error('Erro ao buscar entrevista por ID');
  return res.json();
};

export const fetchReuniaoByGroup = async (groupId: number) => {
  const res = await fetch(`${API_BASE}/register/groups/${groupId}/register`);
  if (!res.ok) throw new Error(`Erro ao buscar reunioes do grupo ${groupId}`);
  return res.json();
};

export const createReuniao = async (reuniao: any) => {
  const res = await fetch(`${API_BASE}/register/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(reuniao),
  });
  if (!res.ok) throw new Error('Erro ao criar entrevista');
  return res.json();
};

export const updateReuniao = async (id: number, reuniao: any) => {
  console.log('Entrevista a ser atualizada:', reuniao);
  const res = await fetch(`${API_BASE}/register/update/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(reuniao),
  });
  if (!res.ok) throw new Error('Erro ao atualizar entrevista');
  return res.json();
};

export const deleteReuniao = async (id: number) => {
  const res = await fetch(`${API_BASE}/register/delete/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Erro ao deletar entrevista');
  return res.ok;
};

export const fetchEvento = async () => {
  const res = await fetch(`${API_BASE}/calendar`);
  if (!res.ok) throw new Error('Erro ao buscar evento no calendário');
  return res.json();
}

export const fetchEventoById = async (id: number) => {
  const res = await fetch(`${API_BASE}/calendar/${id}`);
  if (!res.ok) throw new Error('Erro ao buscar evento no calendário por ID');
  return res.json();
};

export const createEvento = async (evento: any) => {
  const res = await fetch(`${API_BASE}/calendar/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(evento),
  });
  if (!res.ok) throw new Error('Erro ao criar evento de calendário');
  return res.json();
};

export const fetchEventoByDate = async (date: any) => {
  const res = await fetch(`${API_BASE}/calendar/date`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(date),
  });
  if (!res.ok) throw new Error('Erro ao buscar evento no calendário por data');
  return res.json();
};

export const updateEvento = async (id: number, evento: any) => {
  console.log('Evento a ser atualizada:', evento);
  const res = await fetch(`${API_BASE}/calendar/update/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(evento),
  });
  if (!res.ok) throw new Error('Erro ao atualizar evento de calendário');
  return res.json();
};

export const deleteEvento = async (id: number) => {
  const res = await fetch(`${API_BASE}/calendar/delete/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Erro ao deletar evento de calendário');
  return res.ok;
};

export const fetchGroups = async () => {
  const res = await fetch(`${API_BASE}/register/groups`);
  if (!res.ok) throw new Error('Erro ao buscar grupos');
  return res.json();
};

export const fetchGroupById = async (id: number) => {
  const res = await fetch(`${API_BASE}/register/groups/${id}`);
  if (!res.ok) throw new Error('Erro ao buscar grupo por ID');
  return res.json();
};

export const createGroup = async (group: any) => {
  console.log('Grupo a ser criada:', group);
  const res = await fetch(`${API_BASE}/register/groups/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(group),
  });
  if (!res.ok) throw new Error('Erro ao criar grupo');
  return res.json();
};

export const updateGroup = async (id: number, group: any) => {
  console.log('Entrevista a ser atualizada:', group);
  const res = await fetch(`${API_BASE}/register/groups/update/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(group),
  });
  if (!res.ok) throw new Error('Erro ao atualizar grupo');
  return res.json();
};

export const removeMeetingFromGroup = async (meetingId: number, groupId: number) => {
  const res = await fetch(`${API_BASE}/register/groups/remove/${meetingId}/${groupId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
  });
  if (!res.ok) throw new Error('Erro ao remover reuniao do grupo');
  return res.json();
};

export const addMeetingFromGroup = async (meetingId: number, groupId: number) => {
  const res = await fetch(`${API_BASE}/register/groups/add/${meetingId}/${groupId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
  });
  if (!res.ok) throw new Error('Erro ao adicionar reuniao do grupo');
  return res.json();
};

export const deleteGroup = async (id: number) => {
  const res = await fetch(`${API_BASE}/register/groups/delete/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Erro ao deletar grupo');
  return res.ok;
};
