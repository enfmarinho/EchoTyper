'use client'
import React, { useState } from "react";
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

type Reuniao = {
  id: string;
  titulo: string;
};

const reunioes: Reuniao[] = [
  { id: "1", titulo: "Reunião - 03" },
  { id: "2", titulo: "Reunião - 01" },
  { id: "3", titulo: "Reunião 7" },
  { id: "4", titulo: "Sprint Review" },
  { id: "5", titulo: "Planejamento" },
  { id: "6", titulo: "Reunião Final" },
];

export default function ReunioesPage() {
  const [busca, setBusca] = useState("");

  const reunioesFiltradas = reunioes.filter((r) =>
    r.titulo.toLowerCase().includes(busca.toLowerCase())
  );

  const abrirReuniao = (id: string) => {
    console.log("Abrir reunião:", id);
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
                {reuniao.titulo}
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
