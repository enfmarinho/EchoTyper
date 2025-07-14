'use client'
import React from "react";
import {
  Box,
  Button,
  TextField,
  Link,
} from "@mui/material";
import Image from "next/image";
import { useRouter } from "next/navigation";

export default function LoginPage() {
    const router = useRouter();
  
    return (
    //   <AuthContainer>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "flex-start",
            height: "100vh",
            backgroundColor: "#221232"
          }}
        >
        <Image src="/logo-lecture.jpg" alt="Logo" width={450} height={450} />
  
        <Box
            sx={{
            display: "flex",
            flexDirection: "column",
            width: "100%",
            maxWidth: 500,
            gap: 2,
            }}
        >
            <TextField
            label="Login"
            variant="filled"
            fullWidth
            size="small"
            sx={{ mb: 2, backgroundColor: "white", borderRadius: 1 }}
            />
            <TextField
            label="Senha"
            type="password"
            variant="filled"
            fullWidth
            size="small"
            sx={{ mb: 2, backgroundColor: "white", borderRadius: 1 }}
            />
            <Button
            variant="contained"
            fullWidth
            sx={{ mt: 1, backgroundColor: "#C0821A", textTransform: "none" }}
            onClick={() => {
                // Aqui pode adicionar a lógica de autenticação
                router.push("/dashboard/home");
            } }
            >
            Entrar
            </Button>
            <Link href="/cadastro" underline="hover" sx={{ color: "#C0821A", textAlign: "center" }}>
            Criar conta
            </Link>
        </Box>
      </Box>
     // </AuthContainer>
    );
  }