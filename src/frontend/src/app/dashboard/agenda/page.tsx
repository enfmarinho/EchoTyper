'use client'
import React, { useState } from "react";
import {
  Box,
  Grid,
  Typography,
  TextField,
  Button,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";
import { DateCalendar } from "@mui/x-date-pickers/DateCalendar";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import ConflictChecker from "@/components/ConflictChecker";
import ptBR from "date-fns/locale/pt-BR";
import { createEvento } from "@/lib/api";

type Evento = {
  title: string;
  description: string;
  date: string; // ISO format (yyyy-mm-dd)
};

export default function AgendaPage() {
  const [dataSelecionada, setDataSelecionada] = useState<Date | null>(new Date());
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [eventos, setEventos] = useState<Evento[]>([]);

  const handleAdicionarEvento = () => {
    if (!dataSelecionada || !titulo) return;

    const novoEvento: Evento = {
      title: titulo,
      description: descricao,
      date: dataSelecionada.toISOString().split("T")[0],
    };

    setEventos((prev) => [...prev, novoEvento]);
    setTitulo("");
    setDescricao("");
    createEvento(novoEvento);
  };

  const eventosDoDia = eventos.filter(
    (evento) => evento.date === dataSelecionada?.toISOString().split("T")[0]
  );

  return (
    <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={ptBR}>
      <Box sx={{ p: 4 }}>
        <Typography variant="h4" gutterBottom sx={{ fontWeight: "bold", color: "#0D1B2A" }}>
          Agenda
        </Typography>

        <Grid container spacing={4}>
          {/* Calendário */}
          <Grid sx={{ xs: 12, md: 6 }} component={"div"}>
            <DateCalendar
              slotProps={{
                calendarHeader: {
                  sx: {
                    color: '#0D1B2A', // cor do mês e botões de navegação
                  }
                },
                yearButton: {
                  sx: {
                    color: '#0D1B2A'
                  }
                }
              }}
              value={dataSelecionada}
              onChange={(newDate) => setDataSelecionada(newDate)}
            />

            <Box mt={2}>
              <Typography variant="h6" sx={{ color: "#0D1B2A" }}>Eventos do dia</Typography>
              {eventosDoDia.length === 0 ? (
                <Typography variant="body2" color="text.secondary">
                  Nenhum evento marcado.
                </Typography>
              ) : (
                <List dense>
                  {eventosDoDia.map((evento, idx) => (
                    <ListItem key={idx}>
                      <ListItemText
                        primary={evento.titulo}
                        secondary={evento.description}
                        slotProps={{
                          primary: {
                            sx: { color: "#0D1B2A", fontWeight: "bold" },
                          },
                        }}
                      />
                    </ListItem>
                  ))}
                </List>
              )}
            </Box>
          </Grid>

          {/* Formulário */}
          <Grid sx={{ xs: 12, md: 6 }} component={"div"}>
            <Typography variant="h6" sx={{ color: "#0D1B2A" }}>Marcar novo evento</Typography>
            <TextField
              label="Título"
              value={titulo}
              onChange={(e) => setTitulo(e.target.value)}
              fullWidth
              sx={{ mt: 2 }}
            />
            <TextField
              label="Descrição"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              fullWidth
              multiline
              rows={4}
              sx={{ mt: 2 }}
            />
            <Button
              variant="contained"
              sx={{ mt: 2 }}
              onClick={handleAdicionarEvento}
            >
              Adicionar evento
            </Button>
          </Grid>

          <Grid sx={{ xs: 12, md: 6 }} component={"div"}>
            <Typography variant="h6" sx={{ color: "#0D1B2A" }}>Checar conflitos</Typography>
            <ConflictChecker/>
        </Grid>
      </Grid>
      </Box>
    </LocalizationProvider>
  );
}
