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

// Represents the raw data structure for a "Reunião" (or any register) from API
type ReuniaoFromAPI = {
    id: number;
    title: string;
}

// 1. Updated types for Lecture and Group content
type LectureContent = {
    professor: string;
    course: string;
    // grade is no longer here
};

type CourseGroupContent = {
    grades: number[];
    score: number;
    currentAttendance: number;
    workload: number;
};

type LectureSeed = {
    title: string;
    transcription: string;
    summary: string;
    annotations: string;
    groupId: number | null;
    content: LectureContent;
};

// Type for creating a new group, now with specific content
type GrupoSeed = {
    groupName: string;
    registerIds: number[];
    content: CourseGroupContent;
};


// 2. Updated seed data for lectures (grade removed from content)
const lecturesSeed: Omit<LectureSeed, 'groupId' | 'id'>[] = [
    {
        title: "Introduction to Variables",
        annotations: "Good student engagement. Need to prepare more examples for the next class.",
        summary: "This lecture introduced the fundamental concept of variables in Python. Key topics included what variables are (containers for storing data values), how to declare them, and the basic data types: integers (`int`), floating-point numbers (`float`), and strings (`str`). The lecture concluded with examples of assigning values to variables.",
        transcription: "Dr. Grant: Good morning, everyone. Today, we begin with the building blocks of any program: variables. Think of a variable as a labeled box where you can store information. For example, `age = 25`. Here, `age` is the variable, and it's holding the integer value 25. Python is smart; you don't need to tell it what type of data you're storing. If you write `name = \"John\"`, Python knows `name` is a string. We'll focus on three main types for now: integers for whole numbers, floats for decimal numbers, and strings for text.",
        content: {
            professor: "Dr. Alan Grant",
            course: "Introduction to Python Programming"
        }
    },
    {
        title: "Variable Naming Conventions and Scope",
        annotations: "A student asked about special characters in variable names. Clarified the rules.",
        summary: "Building on the previous lesson, this lecture focused on the rules for naming variables in Python. It was established that names can contain letters, numbers, and underscores, but cannot start with a number. It was explicitly stated that variable names should not start with an underscore, as this is reserved for special system functions. The concept of variable scope (local vs. global) was also briefly introduced.",
        transcription: "Dr. Grant: Alright, now that we know what variables are, let's discuss how to name them. There are rules. A variable name must start with a letter or an underscore. Wait, let me correct that. For our purposes, and for clear, public code, a variable name must start with a letter. You should not use an underscore to start a variable name, as this has a special meaning in Python, often indicating a variable that should not be accessed directly. So, `my_variable` is good. `1variable` is bad. And `_internal_variable` is something we will avoid entirely. Let's stick to starting with letters.",
        content: {
            professor: "Dr. Alan Grant",
            course: "Introduction to Python Programming"
        }
    }
    // {
    //     title: "Functions and 'Internal Use' Variables",
    //     annotations: "The contradiction from the last class was a good teaching moment. Students seem to understand the nuance now.",
    //     summary: "This lecture introduced functions as reusable blocks of code. In this context, the discussion on variable naming was revisited with more nuance. It was explained that while variables starting with a single underscore are valid, they are used by convention to indicate that a variable is intended for 'internal use' within a module or class. This clarified the 'hard rule' from the previous lecture, explaining it as a best practice for beginners rather than a technical limitation.",
    //     transcription: "Dr. Grant: Today we move on to functions. A function lets you group a set of statements that you can run multiple times. Now, this brings us back to naming conventions. In our last class, I told you not to start your variable names with an underscore. I want to add some nuance to that rule today. While it's true you shouldn't do it for regular variables, there is a common Python convention where a single leading underscore, like `_my_helper_variable`, is used to signal that this variable is for internal use only. It's a hint to other programmers, saying, 'This is part of the inner workings, don't rely on it externally.' It doesn't actually prevent access, but it's a powerful convention. So, the rule I gave you was a simplification to prevent bad habits, but now you see the more advanced, contextual use.",
    //     content: {
    //         professor: "Dr. Alan Grant",
    //         course: "Introduction to Python Programming"
    //     }
    // }
];

// 3. Updated group seed data with new content structure
const courseGroupSeed: GrupoSeed = {
    groupName: "Introduction to Python Programming",
    registerIds: [],
    content: {
        grades: [9.5, 9.2, 9.8],
        score: 9.5,
        currentAttendance: 3,
        workload: 40
    }
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
        
        // 4. Seeding logic remains largely the same, but uses the new data structures
        // console.log("Iniciando o processo de popular o banco de dados com aulas...");

        // const createdGroup = await createGroup(courseGroupSeed);
        // console.log(`Grupo de curso criado: ${createdGroup.groupName} com ID: ${createdGroup.id}`);

        // // Prepare lectures with the new group ID
        // const lecturesToCreate: LectureSeed[] = lecturesSeed.map(lecture => ({
        //     ...lecture,
        //     groupId: createdGroup.id,
        // }));

        // for (const lecture of lecturesToCreate) {
        //    // We assume createReuniao is a generic function to create any register
        //    await createReuniao(lecture);
        //    console.log(`Aula criada: ${lecture.title}`);
        // }
        
        // console.log("Dados das aulas populados com sucesso!");
        
        // Fetch data to display on the page
        const [gruposData, reunioesData] = await Promise.all([
          fetchGroups() as Promise<GrupoFromAPI[]>,
          fetchReunioes() as Promise<ReuniaoFromAPI[]>
        ]);

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
        title="Cursos (Grupos)"
        items={grupos}
        itemHref={(id) => `/dashboard/reunioes/grupos/${id}`}
        createHref="/dashboard/reunioes/grupos/create"
        createLabel="Criar novo curso"
        searchPlaceholder="Buscar curso..."
      />
      
      <hr />

      <ItemGrid
        title="Aulas"
        items={reunioes}
        itemHref={(id) => `/dashboard/reunioes/${id}`}
        createHref="/dashboard/reunioes/create"
        createLabel="Criar nova aula"
        searchPlaceholder="Buscar aula..."
      />
    </main>
  );
}
