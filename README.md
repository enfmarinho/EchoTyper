# Rodando o Servidor Frontend
### Instalação do projeto

No seu terminal do Linux, clone o repositório:
```
git clone https://github.com/enfmarinho/EchoTyper
cd /EchoTyper/echo-typer
```


Em seguida, instale o NodeJS:

```
# Baixar e instalar nvm:
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.2/install.sh | bash

# Para evitar reiniciar o shell
\. "$HOME/.nvm/nvm.sh"

# Baixar e instalar Node.js:
nvm install 22

# Verificar versão do node:
node -v # Should print "v22.14.0".
nvm current # Should print "v22.14.0".

# Baixar e instalar pnpm:
corepack enable pnpm

# Verificar versão do pnpm:
pnpm -v

```

### Rodar o projeto

Instale as dependências:

`pnpm i`

Rode o programa no terminal:

`pnpm dev`

Em seguida, acesse o site no link http://localhost:3000/


# Rodando o Servidor Backend

### Pré-requisitos

Antes de começar, certifique-se de ter o seguinte instalado na sua máquina:

- Java JDK
- Maven
- PostgreSQL

---

### Passos para compilar e executar

1. **Crie o banco de dados no PostgreSQL**  
   Crie um banco com nome, usuário e senha de sua escolha.

2. **Configure as variáveis de ambiente**  
   No diretório `/backend/EchoTyper`, crie um arquivo `.env` com o seguinte conteúdo (ajuste os valores conforme o seu banco):
   ```env
    DB_NAME={NOME_DO_BD}
    DB_HOST={IP_DO_BD:PORTA(provavelmente 5432)}
    DB_USERNAME={SEU_NOME_USUARIO_BD}
    DB_PASSWORD={SUA_SENHA_BD}
   ```

3. **Compile o projeto (sem rodar os testes)**  
   No terminal, dentro da pasta `/backend/EchoTyper`:
   ```bash
   mvn clean install -DskipTests
   ```

4. **Execute o servidor Spring Boot**  
   Ainda no mesmo diretório:
   ```bash
   mvn spring-boot:run
   ```

> O backend ficará disponível por padrão em `http://localhost:8081`.
