'use client'
import React, { useEffect, useState } from "react";
import {
  Box,
  Grid,
  Typography,
  TextField,
  Button,
  List,
  ListItem,
  ListItemText,
  Paper,
} from "@mui/material";
import { DateCalendar } from "@mui/x-date-pickers/DateCalendar";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import ConflictChecker from "@/components/ConflictChecker";
import ptBR from "date-fns/locale/pt-BR";
import { createEvento } from "@/lib/api";
import { fetchEvento } from "@/lib/api";

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

  const handleAdicionarEvento = async () => {
    if (!dataSelecionada || !titulo) return;

    const novoEvento: Evento = {
      title: titulo,
      description: descricao,
      date: dataSelecionada.toISOString().split("T")[0],
    };

    try {
      await createEvento(novoEvento); // Wait for the event to be created
      loadEventos(); // Refresh the list from the database
      setTitulo("");
      setDescricao("");
    } catch (error) {
      console.error("Failed to create event:", error);
    }
  };

  const eventosDoDia = eventos.filter(
    (evento) => evento.date === dataSelecionada?.toISOString().split("T")[0]
  );

  // Function to load events from the database
  const loadEventos = async () => {
    try {
      const data = await fetchEvento();
      setEventos(data);
    } catch (error) {
      console.error("Failed to fetch events:", error);
    }
  };

  // Fetch events when the component mounts
  useEffect(() => {
    loadEventos();
  }, []);

return (
    <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={ptBR}>
      <Box sx={{ p: 4 }}>
        <Typography variant="h4" gutterBottom sx={{ fontWeight: "bold", color: "#0D1B2A" }}>
          Agenda
        </Typography>

        <Grid container spacing={4}>
          {/* Column 1: Calendar and Events of the Day */}
          <Grid sx={{ xs: 12, md: 6 }}>
            <DateCalendar
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
                  {eventosDoDia.map((evento) => (
                    <ListItem>
                      <ListItemText
                        primary={evento.title}
                        secondary={evento.description}

                        slotProps={{
                          primary: {
                            sx: {
                              color: '#0D1B2A', // A dark color from your theme
                              fontWeight: 'bold', // Optionally make it bold
                            },
                          },
                        }}
                      />
                    </ListItem>
                  ))}
                </List>
              )}
            </Box>
          </Grid>

          {/* Column 2: All Events List */}
          <Grid sx={{ xs: 12, md: 6 }}>
            <Paper elevation={2} sx={{ p: 2, height: '100%' }}>
              <Typography variant="h6" sx={{ color: "#0D1B2A", mb: 2 }}>
                Todos os Eventos
              </Typography>
              <Box sx={{ maxHeight: '70vh', overflow: 'auto' }}>
                {eventos.length === 0 ? (
                  <Typography variant="body2" color="text.secondary">
                    Nenhum evento cadastrado.
                  </Typography>
                ) : (
                  <List dense>
                    {eventos.map((evento) => (
                      <ListItem>
                        <ListItemText
                          primary={evento.title}
                          secondary={`${new Date(evento.date).toLocaleDateString('pt-BR', {timeZone: 'UTC'})} - ${evento.description}`}
                        />
                      </ListItem>
                    ))}
                  </List>
                )}
              </Box>
            </Paper>
          </Grid>

          {/* Column 3: New Event Form and Conflict Checker */}
          <Grid sx={{ xs: 12, md: 6 }}>
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

            <Box mt={4}>
                <Typography variant="h6" sx={{ color: "#0D1B2A" }}>Checar conflitos</Typography>
                <ConflictChecker/>
            </Box>
          </Grid>
        </Grid>
      </Box>
    </LocalizationProvider>
  );
}

