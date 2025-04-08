import React, { ReactNode } from "react";
import {
  Box,
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Typography,
} from "@mui/material";
import HomeIcon from "@mui/icons-material/Home";
import EventNoteIcon from "@mui/icons-material/EventNote";
import DescriptionIcon from "@mui/icons-material/Description";
import SettingsIcon from "@mui/icons-material/Settings";
import Image from "next/image";

const drawerWidth = 240;

const menuItems = [
  { text: "Home", icon: <HomeIcon /> },
  { text: "Reuniões", icon: <EventNoteIcon /> },
  { text: "Resumos", icon: <DescriptionIcon /> },
  { text: "Configurações", icon: <SettingsIcon /> },
];

type DefaultLayoutProps = {
  children: ReactNode;
};

export default function DefaultLayout({ children }: DefaultLayoutProps) {
  return (
    <Box sx={{ display: "flex" }}>
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: {
            width: drawerWidth,
            boxSizing: "border-box",
            backgroundColor: "#122232",
            color: "white",
          },
        }}
      >
        <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center", p: 1 }}>
          <Image src="/logo.png" alt="Logo" width={200} height={200} />
        </Box>
        <List>
          {menuItems.map((item, index) => (
            <ListItemButton key={index}>
              <ListItemIcon sx={{ color: "white" }}>{item.icon}</ListItemIcon>
              <ListItemText primary={item.text} />
            </ListItemButton>
          ))}
        </List>
      </Drawer>
      <Box
        component="main"
        sx={{ flexGrow: 1, bgcolor: "#f5f6f7", minHeight: "100vh", p: 3 }}
      >
        {children}
      </Box>
    </Box>
  );
}
