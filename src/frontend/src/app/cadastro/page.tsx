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

export default function CadastroPage() {
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
            label="Nome"
            fullWidth
            variant="filled"
            size="small"
            sx={{ mb: 2, backgroundColor: "white", borderRadius: 1 }}
            />
            <TextField
            label="Email"
            fullWidth
            variant="filled"
            size="small"
            sx={{ mb: 2, backgroundColor: "white", borderRadius: 1 }}
            />
            <TextField
            label="Senha"
            type="password"
            fullWidth
            variant="filled"
            size="small"
            sx={{ mb: 2, backgroundColor: "white", borderRadius: 1 }}
            />
    
            <Button
            fullWidth
            variant="contained"
            sx={{ backgroundColor: "#c28807", '&:hover': { backgroundColor: "#a37205" } }}
            onClick={() => {
                // Aqui pode adicionar a requisição na api, para criar a conta
                // Após o cadastro, redirecionar para a página inicial
                router.push("/dashboard/home");
            } }
            >
            Criar Conta
            </Button>
    
            <Box mt={2} textAlign="right">
            <Link href="/login" underline="hover" sx={{ color: "#c28807" }}>
                Já tenho uma conta
            </Link>
            </Box>
            <Box mt={2} textAlign="right">
            <Link href="/recuperar-senha" underline="hover" sx={{ color: "#c28807" }}>
                Esqueci minha senha
            </Link>
            </Box>
            </Box>
        </Box>
    //   </AuthContainer>
    );
  }