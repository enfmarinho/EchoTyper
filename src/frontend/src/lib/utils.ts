// src/utils/loadData.ts
import React from "react";
import { Item } from "./types";
import { fetchGroups, fetchReuniaoByGroup, fetchReunioes } from "@/lib/api";

export async function loadGrupos(setGrupos: (grupos: Item[]) => void) {
  try {
    const data = await fetchGroups();
    const items: Item[] = data.map((item: any) => ({
      id: item.id,
      title: item.groupName,
    }));
    setGrupos(items);
  } catch (err) {
    console.error("Erro ao carregar grupos:", err);
  }
}
export async function loadReunioes(setReunioes: (reunioes: Item[]) => void) {
  try {
    const data = await fetchReunioes();
    const items: Item[] = data.map((item: any) => ({
      id: item.id,
      title: item.title,
    }));
    setReunioes(items);
  } catch (err) {
    console.error("Erro ao carregar entrevistas:", err);
  }
}

export async function loadReunioesByGroup(groupId: number, setReunioes: (reunioes: Item[]) => void) {
  try {
    const data = await fetchReuniaoByGroup(groupId);
    const items: Item[] = data.map((item: any) => ({
      id: item.id,
      title: item.title,
    }));
    setReunioes(items);
  } catch (err) {
    console.error("Erro ao carregar entrevistas:", err);
  }
}