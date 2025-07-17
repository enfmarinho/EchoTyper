'use client';
import React, { useEffect, useState } from "react";
import ItemGrid from "@/components/ItemGrid";
// Assuming API functions return promises with the data
import { fetchReunioes, fetchGroups, createReuniao, createGroup } from "@/lib/api"; 

// --- TYPE DEFINITIONS ---

// A generic type for items displayed in the grid, promoting reusability.
type Item = {
  id: number;
  title: string;
};

// Represents the raw data structure for a "Grupo" as it comes from API.
type GrupoFromAPI = {
  id: number;
  groupName: string; // Original field name from the API
  meetings: number[];
};

// Represents the raw data structure for a "Reunião" from API.
type ReuniaoFromAPI = {
    id: number;
    title: string;
}

type ReuniaoContent = {
    candidate: string;
    interviewer: string;
    role: string;
    evaluation: string;
}

type Reuniao = {
    title: string;
    transcription: string;
    summary: string;
    annotations: string;
    groupId: number | null;
    content: ReuniaoContent;
};

type Groupcontent = {
    candidates: string[];
    interviewers: string[];
    evaluations: string[];
    processBeginDate: string;
    processEndDate: string;
};

type Grupo = {
    groupName: string;
    registerIds: string[];
    content: Groupcontent;
};

const reunioesSeed: Omit<Reuniao, 'groupId'>[] = [
  {
    title: "Entrevista de Triagem Ana Silva",
    transcription: "Fernanda: Olá, Ana! Obrigado por participar. Pode me contar um pouco sobre sua experiência com desenvolvimento backend?\n\nAna: Olá, Fernanda! Claro. Eu trabalho com desenvolvimento há cerca de 5 anos, e meu foco principal sempre foi backend. Tenho uma vasta experiência com Python, utilizando principalmente o framework Django para construir APIs RESTful e aplicações web robustas.\n\nFernanda: Ótimo, Python é fundamental para nós. E qual sua pretensão salarial?\n\nAna: Minha pretensão é em torno de R$ 12.000.\n\nFernanda: Entendido. E quanto à sua disponibilidade?\n\nAna: Tenho disponibilidade para começar imediatamente.",
    summary: "Ana Silva se apresentou como uma desenvolvedora sênior com 5 anos de experiência, com foco principal em desenvolvimento backend com Python e Django. Ela mencionou projetos de e-commerce e sistemas de gerenciamento interno. Sua pretensão salarial é de R$ 12.000 e ela pode começar imediatamente.",
    annotations: "Candidata muito comunicativa. Mencionou disponibilidade para início imediato.",
    content: {
      candidate: "Ana Silva",
      interviewer: "Fernanda Lima",
      role: "Desenvolvedor de Software Sênior",
      evaluation: "Candidata parece qualificada e alinhada com a cultura. Demonstra forte experiência em Python, que é um requisito chave."
    }
  },
  {
    title: "Entrevista Técnica Ana Silva",
    transcription: "Ricardo: Oi, Ana. Na sua conversa com a Fernanda, você mencionou vasta experiência com Python e Django. Poderia detalhar algum projeto complexo onde usou, por exemplo, Celery para tarefas assíncronas ou Django REST Framework para serialização avançada?\n\nAna: Oi, Ricardo. Então, para ser mais precisa, eu utilizei Python no início da minha carreira. Nos últimos três anos, na minha posição na TechSolutions, o foco principal da equipe foi em uma stack com Node.js e Express. Foi com Node que desenvolvi a maior parte das minhas habilidades em microserviços e APIs.\n\nRicardo: Entendo. Então sua experiência principal hoje é com Node.js, não Python?\n\nAna: Exato. Eu tenho familiaridade com Python, mas minha expertise diária é em Node.js.\n\nRicardo: Ok, obrigado pelo esclarecimento. Antes de terminarmos, gostaria de confirmar sua pretensão salarial.\n\nAna: Sim. Após avaliar melhor as responsabilidades da vaga e o mercado, minha pretensão salarial ajustada é de R$ 15.000.",
    summary: "Durante a entrevista técnica, Ana Silva foi questionada sobre detalhes de sua experiência com Python. Ela esclareceu que, embora tenha trabalhado com Python, sua experiência mais profunda e recente nos últimos 3 anos foi com Node.js e o framework Express. Ao final, mencionou que, após pesquisar melhor o mercado, sua pretensão salarial atualizada seria de R$ 15.000.",
    annotations: "Ajustou a pretensão salarial.",
    content: {
      candidate: "Ana Silva",
      interviewer: "Ricardo Mendes",
      role: "Desenvolvedor de Software Sênior",
      evaluation: "Seu conhecimento em Node.js é sólido, mas para a vaga em questão, a falta de profundidade em Python é um ponto de atenção."
    }
  },
  {
    title: "Entrevista de Triagem Bruno Costa",
    transcription: "Fernanda: Olá, Bruno. Vi no seu currículo que você trabalhou na InovaTech. Qual foi seu papel lá?\n\nBruno: Olá, Fernanda. Sim, trabalhei lá por três anos. Fui o líder técnico do principal projeto da empresa, o 'Projeto Phoenix'.\n\nFernanda: Que interessante! E quais eram suas responsabilidades como líder?\n\nBruno: Eu era responsável por definir toda a arquitetura de microserviços, escolher as tecnologias, e gerenciar as tarefas do time de cinco desenvolvedores. Foi um projeto desafiador que entregamos com sucesso.",
    summary: "Bruno Costa destacou sua experiência na empresa 'InovaTech', onde trabalhou por 3 anos. Ele afirmou ter sido o líder técnico do 'Projeto Phoenix', um sistema de logística de grande escala, sendo responsável por toda a arquitetura da solução e pela gestão da equipe de desenvolvimento.",
    annotations: "Mencionou ter trabalhado na 'InovaTech' de 2020 a 2023.",
    content: {
      candidate: "Bruno Costa",
      interviewer: "Fernanda Lima",
      role: "Desenvolvedor de Software Sênior",
      evaluation: "A experiência no 'Projeto Phoenix' é um grande diferencial. Recomendo para a fase técnica."
    }
  },
  {
    title: "Entrevista de Triagem Carla Dias",
    transcription: "Fernanda: Olá, Carla. Bem-vinda. Vi que você também trabalhou na InovaTech. Pode me contar sobre sua experiência lá?\n\nCarla: Olá! Com certeza. Eu fui a líder de produto e squad lead do 'Projeto Phoenix'. Foi um projeto incrível, onde eu era responsável por todo o roadmap e pela gestão da equipe.\n\nFernanda: Interessante. Tivemos outro candidato, o Bruno Costa, que também trabalhou nesse projeto.\n\nCarla: Ah, sim, o Bruno! Um ótimo desenvolvedor. Ele era um dos sêniores da minha equipe e foi fundamental no desenvolvimento dos módulos de faturamento. Eu era a responsável pela liderança e arquitetura geral, e ele pela execução de partes importantes.",
    summary: "Carla Dias também veio da 'InovaTech'. Ela se apresentou como a líder de produto e squad lead do 'Projeto Phoenix', responsável pelas definições estratégicas e pela liderança da equipe. Ela mencionou que Bruno Costa era um dos desenvolvedores sêniores da sua equipe, responsável pela implementação de alguns módulos.",
    annotations: "Bruno Costa (2020-2023).",
    content: {
      candidate: "Carla Dias",
      interviewer: "Fernanda Lima",
      role: "Desenvolvedor de Software Sênior",
      evaluation: "A experiência no 'Projeto Phoenix' é relevante, mas sua descrição do papel de liderança entra em conflito com a de outro candidato (Bruno Costa)."
    }
  }
];


const grupoSeed: Grupo = {
  groupName: "Vaga Dev Sênior",
  registerIds: [], // Se for necessário registrar reuniões depois, pode deixar vazio agora
  content: {
    candidates: ["Ana Silva", "Bruno Costa", "Carla Dias"],
    interviewers: ["Fernanda Lima", "Ricardo Mendes"],
    evaluations: [
      "Candidata parece qualificada e alinhada com a cultura. Demonstra forte experiência em Python.",
      "A candidata demonstrou bom raciocínio lógico, mas sua experiência principal não é em Python.",
      "Candidato com boa comunicação e experiência de liderança no Projeto Phoenix.",
      "Candidata muito experiente, mas há conflito de papéis com outro candidato. Precisa de esclarecimento.",
    ],
    processBeginDate: "2025-07-01",
    processEndDate: "2025-07-31"
  }
};


// --- COMPONENT ---

/**
 * Renders a management page for "Entrevistas" (Classes/registerIds) and "Grupos" (Groups).
 * It fetches both data sets and displays them in separate, searchable grids.
 */
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

        // // First, create the group to get an ID for it
        // // Assuming createGroup returns the newly created group with its ID
        // const createdGroup = await createGroup(grupoSeed);
        // console.log(`Grupo criado: ${createdGroup.groupName} com ID: ${createdGroup.id}`);

        // // Now, create the meetings
        // const meetingsToCreate: Reuniao[] = [
        //     { ...reunioesSeed[0], groupId: createdGroup.id }, // Associate with the new group
        //     { ...reunioesSeed[1], groupId: createdGroup.id }, // Associate with the new group
        //     { ...reunioesSeed[2], groupId: createdGroup.id }, // Associate with the new group
        //     { ...reunioesSeed[3], groupId: createdGroup.id }, // This one has no group
        // ];

        // for (const meeting of meetingsToCreate) {
        //    await createReuniao(meeting);
        //    console.log(`Reunião criada: ${meeting.title}`);
        // }
        
        console.log("Dados populados com sucesso!");

        
        // Fetch both datasets in parallel for better performance
        const [gruposData, reunioesData] = await Promise.all([
          fetchGroups() as Promise<GrupoFromAPI[]>,
          fetchReunioes() as Promise<ReuniaoFromAPI[]>
        ]);

        // Map API data to the structure required by the ItemGrid component
        const formattedGrupos = gruposData.map(g => ({
          id: g.id,
          title: g.groupName, // Explicitly map groupName to title
        }));

        const formattedReunioes = reunioesData.map(r => ({
            id: r.id,
            title: r.title,
        }));

        setGrupos(formattedGrupos);
        setReunioes(formattedReunioes);
        setError(null); // Clear any previous errors on success
      } catch (err) {
        console.error("Failed to fetch data:", err);
        setError("Não foi possível carregar os dados. Por favor, tente novamente mais tarde.");
      } finally {
        setIsLoading(false);
      }
    };

    loadInitialData();
  }, []); // Empty dependency array ensures this runs only once on mount

  // --- RENDER LOGIC ---

  if (isLoading) {
    return <div className="p-8 text-center">Carregando...</div>;
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
        title="Entrevistas"
        items={reunioes}
        itemHref={(id) => `/dashboard/reunioes/${id}`}
        createHref="/dashboard/reunioes/create"
        createLabel="Criar nova entrevista"
        searchPlaceholder="Buscar entrevista..."
      />
    </main>
  );
}