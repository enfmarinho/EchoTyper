'use client'
import React, { useEffect, useState } from "react";
import {
  Box,
  Typography,
  Grid,
  Paper,
  Button,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import Link from "next/link";
import { fetchReunioes } from "@/lib/api";

type Evento = {
  titulo: string;
  descricao: string;
};

type Reuniao = {
  id: string;
  title: string;
};


const proximosEventos: Evento[] = [
  { titulo: "Daily Standup", descricao: "04/04 - Alinhamento rápido da equipe" },
  { titulo: "Planejamento da Sprint", descricao: "05/04 - Definir backlog da semana" },
];

export default function SelecaoReunioes() {
  const [reunioes, setReunioes] = useState<Reuniao[]>([]);
  const loadReunioes = async () => {
    try {
      const data = await fetchReunioes();
      setReunioes(data);
    } catch (err) {
      console.error('Erro ao carregar entrevistas:', err);
    }
  };

  useEffect(() => {
    loadReunioes();
  }, []);
  return (
    <Box>
      <Box sx={{ p: 4 }}>
        <Typography variant="h4" gutterBottom sx={{ color: "#0D1B2A" }}>
          Entrevistas Recentes
        </Typography>

        <Grid container spacing={2}>
          {reunioes.map((reuniao, index) => (
            <Grid size={{ xs: 12, md: 4, sm: 6 }} key={index} component={"div"}>
              <Paper
                component={Link}
                href={`/dashboard/reunioes/${reuniao.id}`}
                elevation={3}
                sx={{ p: 2, height: 140, cursor: 'pointer', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}
              >
                <Typography variant="h6">{reuniao.title}</Typography>
              </Paper>
            </Grid>
          ))}

          {/* Nova entrevista */}
          <Grid size={{ xs: 12, md: 4, sm: 6 }} component={"div"}>
            <Paper
              component={Link}
              href="/dashboard/reunioes/create"
              elevation={3}
              sx={{
                p: 2,
                height: 140,
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                border: '2px dashed #ccc',
                cursor: 'pointer',
              }}
            >
              <Button startIcon={<AddIcon />} variant="outlined">
                Nova Entrevista
              </Button>
            </Paper>
          </Grid>
        </Grid>
      </Box>

      {/* Próximos eventos */}
      <Box sx={{ p: 4, pt: 2 }}>
        <Typography variant="h5" gutterBottom sx={{ color: "#0D1B2A" }}>
          Próximos Eventos
        </Typography>

        <Grid container spacing={2}>
          {proximosEventos.map((evento, index) => (
            <Grid size={{ xs: 12, md: 4, sm: 6 }} key={index} component={"div"}>
              <Paper
                elevation={2}
                sx={{
                  p: 2,
                  height: 100,
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'center',
                  backgroundColor: "#e3f2fd",
                }}
              >
                <Typography variant="subtitle1" fontWeight="bold">
                  {evento.titulo}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {evento.descricao}
                </Typography>
              </Paper>
            </Grid>
          ))}
        </Grid>
      </Box>
    </Box>
  );
}
