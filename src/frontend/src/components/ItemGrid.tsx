'use client';
import React, { useState } from "react";
import {
  Box,
  Typography,
  Paper,
  IconButton,
  TextField,
  InputAdornment,
} from "@mui/material";
import Grid from "@mui/material/Grid";
import DescriptionIcon from "@mui/icons-material/Description";
import SearchIcon from "@mui/icons-material/Search";
import Link from "next/link";
import { Item } from "@/lib/types";

type ItemGridProps = {
  title?: string;
  items: Item[];
  onItemClick?: (id: string) => void;
  itemHref?: (id: string) => string;
  createHref?: string;
  createLabel?: string;
  searchPlaceholder?: string;
  emptyMessage?: string;
};

type ItemProps = {
  item: Item;
  onItemClick?: (id: string) => void;
  itemHref?: (id: string) => string;
};

export default function ItemGrid({
  title,
  items,
  onItemClick,
  itemHref,
  createHref,
  createLabel = "Criar novo item",
  searchPlaceholder = "Buscar...",
  emptyMessage,
}: ItemGridProps) {
  const [busca, setBusca] = useState("");

  const itensFiltrados = items.filter((item) =>
    item.title.toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <Box sx={{ padding: 4 }}>
      <Typography variant="h4" gutterBottom sx={{ fontWeight: "bold", color: "#0D1B2A" }}>
        {title}
      </Typography>

      <Box sx={{ mb: 3 }}>
        <TextField
          size="small"
          placeholder={searchPlaceholder}
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
        {itensFiltrados.map((item) => (
          <Grid xs={6} sm={4} md={2.4} key={item.id} >
            <Paper
              onClick={() => onItemClick?.(item.id)}
              component={itemHref ? Link : "div"}
              href={itemHref?.(item.id) || undefined}
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
                {item.title}
              </Typography>
            </Paper>
          </Grid>
        ))}

        {createHref && (
          <Grid xs={6} sm={4} md={2.4} >
            <Paper
              component={Link}
              href={createHref}
              sx={{
                height: 120,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                flexDirection: "column",
                border: "1px dashed #c4c4c4",
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
                {createLabel}
              </Typography>
            </Paper>
          </Grid>
        )}
      </Grid>

      {itensFiltrados.length === 0 && (
        <Typography variant="body2" sx={{ mt: 4, textAlign: "center" }}>
          {emptyMessage}
        </Typography>
      )}
    </Box>
  );
}
