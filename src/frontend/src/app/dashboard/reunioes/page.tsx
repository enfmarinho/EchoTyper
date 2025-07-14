'use client';
import React, { useEffect, useState } from "react";
import ItemGrid from "@/components/ItemGrid";
// Assuming API functions return promises with the data
import { fetchReunioes, fetchGroups } from "@/lib/api"; 

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


// --- COMPONENT ---

/**
 * Renders a management page for "Reuniões" (Classes/register) and "Grupos" (Groups).
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