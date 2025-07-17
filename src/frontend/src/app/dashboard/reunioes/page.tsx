'use client';
import React, { useEffect, useState } from "react";
import ItemGrid from "@/components/ItemGrid";
// Assuming API functions return promises with the data
import { fetchReunioes, fetchGroups, createReuniao, createGroup } from "@/lib/api"; 

// --- TYPE DEFINITIONS ---

// A generic type for items displayed in the grid
type Item = {
  id: number;
  title: string;
};

// Represents the raw data structure for a "Grupo" as it comes from API
type GrupoFromAPI = {
  id: number;
  groupName: string;
};

// Represents the raw data structure for a "Reunião" from API
type ReuniaoFromAPI = {
    id: number;
    title: string;
}

// 1. New types for the meeting seed data
type MeetingContent = {
    participants: string[];    
}

type MeetingSeed = {
    title: string;
    transcription: string;
    summary: string;
    annotations: string;
    groupId: number | null; // groupId can be null for standalone meetings
    content: MeetingContent;
};

// Type for creating a new group
type GrupoSeed = {
    groupName: string;
    registerIds: number[];
    content: object; // Assuming content can be an empty object for this group type
};


// 2. Updated seed data with the new meetings
const meetingsSeed: Omit<MeetingSeed, 'groupId'>[] = [
    {
        title: "Planejamento da Sprint 1",
        summary: "A equipe definiu as tarefas prioritárias para a primeira sprint. O foco será no desenvolvimento do módulo de autenticação de usuários e na configuração inicial da infraestrutura na nuvem. As responsabilidades foram distribuídas entre os membros da equipe.",
        annotations: "Acompanhar o progresso da tarefa de infraestrutura, pois é um ponto crítico.",
        content: { "participants": ["Marcos", "Julia", "Pedro"] },
        transcription: "Marcos: Pessoal, bom dia. O objetivo hoje é fechar o escopo da Sprint 1. Julia, como está o planejamento do backend?\n\nJulia: Bom dia! A prioridade é a API de autenticação. Estimo que levamos 5 dias para ter uma primeira versão funcional.\n\nPedro: E a infra? Eu posso começar a provisionar os servidores.\n\nMarcos: Perfeito. Vamos marcar nossa primeira reunião diária para o dia 22 de Julho de 2025, às 10:30 da manhã, para alinharmos o progresso. Conto com todos."
    },
    {
        title: "Revisão Técnica da API",
        summary: "Foi realizada uma revisão técnica do código da API de autenticação. Identificamos a necessidade de adicionar uma camada extra de segurança usando refresh tokens para sessões de longa duração. A alteração será implementada por Julia.",
        annotations: "Verificar a implementação do refresh token antes do final da sprint.",
        content: { "participants": ["Marcos", "Julia"] },
        transcription: "Marcos: Julia, analisei o código da autenticação. Está ótimo, mas acho que podemos melhorar a segurança para o 'lembrar-me'. O que acha de usarmos refresh tokens?\n\nJulia: É uma excelente ideia, Marcos. Aumenta um pouco a complexidade, mas a segurança é muito maior. Vou adicionar essa tarefa ao meu backlog e começar a trabalhar nela amanhã.\n\nMarcos: Combinado. Isso vai deixar nossa aplicação muito mais robusta."
    },
    // {
    //     title: "Alinhamento com a Equipe de Design",
    //     summary: "A equipe de design apresentou os mockups finais para a tela de login e o dashboard do usuário. O frontend deu o aceite e confirmou que não há impedimentos técnicos para a implementação. O design foi aprovado.",
    //     annotations: "Entregar os assets do design para a equipe de frontend.",
    //     content: { "participants": ["Marcos", "Pedro", "Sofia"] },
    //     transcription: "Sofia: Aqui estão as telas finais, pessoal. Tentamos seguir o feedback de vocês da última reunião.\n\nPedro: Ficou excelente, Sofia! Do ponto de vista do frontend, está perfeito para implementar.\n\nMarcos: Ótimo trabalho, time! Com o design aprovado, podemos agendar um workshop de integração de um dia inteiro. Sugiro o dia 24 de Julho de 2025.\n\nPedro: Por mim, está fechado."
    // },
    {
        title: "Feedback Trimestral Carlos",
        summary: "Reunião de feedback com Carlos sobre seu desempenho no último trimestre. Foram destacados seus pontos fortes em liderança técnica e o sucesso na entrega do projeto anterior. Como ponto de desenvolvimento, foi sugerido aprimorar a documentação de projetos.",
        annotations: "Agendar curso de escrita técnica para Carlos.",
        content: { "participants": ["Laura", "Carlos"] },
        transcription: "Laura: Carlos, parabéns pelo resultado do último projeto. Sua liderança foi fundamental.\n\nCarlos: Obrigado, Laura! A equipe estava muito engajada.\n\nLaura: Um ponto que gostaria de alinhar para o próximo ciclo é melhorarmos a documentação técnica. Isso vai nos ajudar muito no futuro.\n\nCarlos: Concordo. É um ponto que preciso desenvolver.\n\nLaura: Ótimo. Vamos marcar nossa próxima conversa para o dia 10 de Agosto de 2025, às 15:00, para ver como estamos progredindo."
    }
];

// 3. New group seed data
const groupSeed: GrupoSeed = {
    groupName: "Projeto Netuno",
    registerIds: [], // Start with no meetings, they will be associated after creation
    content: {}
};


// --- COMPONENT ---

export default function ReunioesPage() {
  const [reunioes, setReunioes] = useState<Item[]>([]);
  const [grupos, setGrupos] = useState<Item[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadInitialData = async () => {
      try {
        setIsLoading(true);
        
        // 4. Seeding logic activated and adapted
        // console.log("Iniciando o processo de popular o banco de dados...");

        // First, create the group to get an ID for it
        // Assuming createGroup returns the newly created group with its ID
        // const createdGroup = await createGroup(groupSeed);
        // console.log(`Grupo criado: ${createdGroup.groupName} com ID: ${createdGroup.id}`);

        // // Now, create the meetings
        // const meetingsToCreate: MeetingSeed[] = [
        //     { ...meetingsSeed[0], groupId: createdGroup.id }, // Associate with the new group
        //     { ...meetingsSeed[1], groupId: createdGroup.id }, // Associate with the new group
        //     // { ...meetingsSeed[2], groupId: createdGroup.id }, // Associate with the new group
        //     { ...meetingsSeed[2], groupId: null }, // This one has no group
        // ];

        // for (const meeting of meetingsToCreate) {
        //    await createReuniao(meeting);
        //    console.log(`Reunião criada: ${meeting.title}`);
        // }
        
        // console.log("Dados populados com sucesso!");
        
        // Fetch both datasets in parallel to display on the page
        const [gruposData, reunioesData] = await Promise.all([
          fetchGroups() as Promise<GrupoFromAPI[]>,
          fetchReunioes() as Promise<ReuniaoFromAPI[]>
        ]);

        // Map API data to the structure required by the ItemGrid component
        const formattedGrupos = gruposData.map(g => ({
          id: g.id,
          title: g.groupName,
        }));

        const formattedReunioes = reunioesData.map(r => ({
            id: r.id,
            title: r.title,
        }));

        setGrupos(formattedGrupos);
        setReunioes(formattedReunioes);
        setError(null);
      } catch (err) {
        console.error("Failed to seed or fetch data:", err);
        setError("Não foi possível carregar ou popular os dados. Verifique o console para mais detalhes.");
      } finally {
        setIsLoading(false);
      }
    };

    // This logic runs only once when the component mounts
    // To re-run, you might need to clear the database and refresh the page
    loadInitialData();
  }, []);

  // --- RENDER LOGIC ---

  if (isLoading) {
    return <div className="p-8 text-center">Populando e carregando dados...</div>;
  }

  if (error) {
    return <div className="p-8 text-center text-red-600">Erro: {error}</div>;
  }

  return (
    <main className="p-4 md:p-8 space-y-10">
      <ItemGrid
        title="Grupos"
        items={grupos}
        itemHref={(id) => `/dashboard/reunioes/grupos/${id}`}
        createHref="/dashboard/reunioes/grupos/create"
        createLabel="Criar novo grupo"
        searchPlaceholder="Buscar grupo..."
      />
      
      <hr />

      <ItemGrid
        title="Reuniões"
        items={reunioes}
        itemHref={(id) => `/dashboard/reunioes/${id}`}
        createHref="/dashboard/reunioes/create"
        createLabel="Criar nova reunião"
        searchPlaceholder="Buscar reunião..."
      />
    </main>
  );
}
