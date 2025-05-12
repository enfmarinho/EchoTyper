'use client'
import React, { useState, useEffect } from "react";
import {
  Box,
  Typography,
  Grid,
  Paper,
  IconButton,
  TextField,
  InputAdornment,
} from "@mui/material";
import DescriptionIcon from "@mui/icons-material/Description";
import SearchIcon from "@mui/icons-material/Search";
import Link from "next/link";
import { fetchReunioes } from "@/lib/api";

type Reuniao = {
  id: string;
  title: string;
};

export default function ReunioesPage() {
  const [reunioes, setReunioes] = useState<Reuniao[]>([]);
  const [busca, setBusca] = useState("");

  const reunioesFiltradas = reunioes.filter((r) =>
    r.title.toLowerCase().includes(busca.toLowerCase())
  );

  const loadReunioes = async () => {
      try {
        const data = await fetchReunioes();
        setReunioes(data);
      } catch (err) {
        console.error('Erro ao carregar reuniões:', err);
      }
  };

  useEffect(() => {
      loadReunioes();
  }, []);

  const abrirReuniao = (id: string) => {
    // Lógica para abrir a reunião
    console.log(`Abrindo reunião com ID: ${id}`);
  };

  return (
    <Box sx={{ padding: 4 }}>
      <Typography variant="h4" gutterBottom sx={{ fontWeight: "bold", color: "#0D1B2A" }}>
        Reuniões
      </Typography>

      <Box sx={{ mb: 3 }}>
        <TextField
          size="small"
          placeholder="Buscar reunião..."
          variant="outlined"
          fullWidth
          value={busca}
          onChange={(e) => setBusca(e.target.value)}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon />
              </InputAdornment>
            ),
          }}
        />
      </Box>

      <Grid container spacing={2}>
        {reunioesFiltradas.map((reuniao) => (
          <Grid sx={{ xs: 6, sm: 4, md: 2.4 }} key={reuniao.id}>
            <Paper
              onClick={() => abrirReuniao(reuniao.id)}
              component={Link}
              href={`/dashboard/reunioes/${reuniao.id}`}
              sx={{
                height: 120,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                flexDirection: "column",
                border: "1px solid #c4c4c4",
                borderRadius: 2,
                cursor: "pointer",
                transition: "0.2s",
                "&:hover": {
                  backgroundColor: "#f0f0f0",
                },
              }}
            >
              <IconButton>
                <DescriptionIcon sx={{ fontSize: 36 }} />
              </IconButton>
              <Typography variant="body2" textAlign="center">
                {reuniao.title}
              </Typography>
            </Paper>
          </Grid>
        ))}
      </Grid>

      {reunioesFiltradas.length === 0 && (
        <Typography variant="body2" sx={{ mt: 4, textAlign: "center" }}>
          Nenhuma reunião encontrada.
        </Typography>
      )}
    </Box>
  );
}
