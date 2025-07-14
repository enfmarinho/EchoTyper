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
import LogoutIcon from '@mui/icons-material/Logout';
import Image from "next/image";
import Link from 'next/link'

const drawerWidth = 240;

const menuItems = [
  { text: "Home", icon: <HomeIcon />,  url: `/dashboard/home` },
  { text: "Agenda", icon: <EventNoteIcon />,  url: `/dashboard/agenda` },
  { text: "Aulas", icon: <DescriptionIcon />,  url: `/dashboard/reunioes` },
  { text: "Configurações", icon: <SettingsIcon />,  url: `/dashboard/configuracoes` },
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
            backgroundColor: "#221232",
            color: "white",
          },
        }}
      >
        <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center", p: 1 }}>
          <Image src="/logo-lecture.jpg" alt="Logo" width={200} height={200} />
        </Box>
        <List>
          {menuItems.map((item, index) => (
            <ListItemButton key={index} component={Link} href={item.url}>
              <ListItemIcon sx={{ color: "white" }}>{item.icon}</ListItemIcon>
              <ListItemText primary={item.text} />
            </ListItemButton>
          ))}
        </List>


        <Box sx={{ mt: "auto", p: 2 }}>
          <ListItemButton
            sx={{
              backgroundColor: "#350952",
              color: "white",
              borderRadius: 1,
              "&:hover": {
                backgroundColor: "#3d0044",
              },
            }}
            component={Link}
            href="/login"
          >
            <ListItemIcon sx={{ color: "white" }}>
               <LogoutIcon sx={{ transform: 'scaleX(-1)' }} />
            </ListItemIcon>
            <ListItemText primary="Sair" />
          </ListItemButton>
        </Box>
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
