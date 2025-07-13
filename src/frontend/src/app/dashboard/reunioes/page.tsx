'use client';
import React, { useEffect, useState } from "react";
import ItemGrid from "@/components/ItemGrid";
import { fetchReunioes, fetchGroups } from "@/lib/api";
import { Group } from "@mui/icons-material";
import { setgroups } from "process";
import { loadGrupos, loadReunioes } from "@/lib/utils";

type Reuniao = {
  id: string;
  title: string;
};

type Grupo = {
  id: string;
  title: string; // Naming the 'groupName' property 'title' to avoid type error on the ItemGrid component 
  meetings: number[];
};;

type Item = {
  id: string;
  title: string;
}

export default function ReunioesPage() {
  const [reunioes, setReunioes] = useState<Item[]>([]);
  const [grupos, setGrupos] = useState<Item[]>([]);

  useEffect(() => {
    // Fetches data and sets it into with the appropriate set function
    loadReunioes(setReunioes);
    loadGrupos(setGrupos);
  }, []);
  return (
    <div>
      <ItemGrid
        title="Grupos" items={grupos}
        onItemClick={(id) => console.log(`Abrindo grupo ${id}`)}
        itemHref={(id) => `/dashboard/reunioes/grupos/${id}`}
        createHref="/dashboard/reunioes/grupos/create"
        createLabel="Criar novo grupo"
        searchPlaceholder="Buscar grupo..."
      />
      <ItemGrid
        title="Entrevistas"
        items={reunioes}
        onItemClick={(id) => console.log(`Abrindo entrevista ${id}`)}
        itemHref={(id) => `/dashboard/reunioes/${id}`}
        createHref="/dashboard/reunioes/create"
        createLabel="Criar nova entrevista"
        searchPlaceholder="Buscar entrevista..."
      />
    </div>
  );
}
