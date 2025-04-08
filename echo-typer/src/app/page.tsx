import React from "react";
import {
  Box,
  Typography,
  Grid,
  Paper,
  Button,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";

type Reuniao = {
  titulo: string;
  descricao: string;
};

const reunioesSalvas : Reuniao[] = [
  { titulo: "Reunião - 03/04", descricao: "Discussão sobre API e interface" },
  { titulo: "Reunião - 01/04", descricao: "Revisão do progresso semanal" },
  { titulo: "Sprint Review", descricao: "Resumo das tarefas concluídas" },
];

export default function SelecaoReunioes() {
  return (
    <Box>
      <Box sx={{ p: 4 }}>
        <Typography variant="h4" gutterBottom sx={{color:"#0D1B2A"}}>
          Reuniões Recentes
        </Typography>

        <Grid container spacing={2}>
          {reunioesSalvas.map((reuniao, index) => (
            <Grid size={{ xs: 12, md: 4, sm: 6 }} key={index} component={"div"}>
              <Paper
                elevation={3}
                sx={{ p: 2, height: 140, cursor: 'pointer', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}
              >
                <Typography variant="h6">{reuniao.titulo}</Typography>
                <Typography variant="body2" color="text.secondary">
                  {reuniao.descricao}
                </Typography>
              </Paper>
            </Grid>
          ))}

          {/* Nova reunião */}
          <Grid size={{ xs: 12, md: 4, sm: 6 }} component={"div"}>
            <Paper
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
                Nova Reunião
              </Button>
            </Paper>
          </Grid>
        </Grid>
      </Box>
    </Box>
  );
}
