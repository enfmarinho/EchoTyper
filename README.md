# Rodando o projeto
### Pré-requisitos
O projeto depende do docker e do docker compose, portanto é necessário tê-los instalados
### Env
O projeto depende de dois arquivos .env para rodar, um na pasta [src/.env](src/.env) e o segundo na pasta 
[src/backend/.env](src/backend/.env), nesses caminhos existem .env de exemplo para facilitar o uso, é possível
utilizá-los da maneira que estão, alterando apenas a variável GOOGLE_API_KEY para a sua chave de API do google. 

Abaixo está um exemplo de .env para o projeto: 
   ```env
    DB_NAME=echotyper_db
    DB_HOST=127.0.0.1
    DB_PORT=5432
    DB_USERNAME=echotyper_user
    DB_PASSWORD=echotyper_password
    GOOGLE_API_KEY=YOUR_GOOGLE_API_KEY
   ```

Ademais, é necessário um arquivo json com uma chave de API do google para acesso ao modelo voice-to-text, que deve ficar no caminho
[src/backend/assets/google_api_key.json](src/backend/assets/google_api_key.json) substituindo o arquivo de exemplo.

### Instalação do projeto
No seu terminal, clone o repositório:
```
git clone https://github.com/enfmarinho/EchoTyper
```
### Docker
Para rodar o projeto utilize:
```
cd /EchoTyper/src/
docker compose build
docker compose up
```

Dependendo da sua máquina, pode ser necessário utilizar sudo para executar os últimos dois comandos acima. 
O primeiro comando irá instalar o repositório, o terceiro irá resolver as dependências do projeto no container,
o quarto irá rodar o container. 

Caso enfrente problemas ao rodar, tente criar o banco de dados manualmente no postgres.

Em seguida, acesse o site no link http://localhost:3000/

Note que o backend ficará disponível por padrão em `http://localhost:8081`.

### Rodando o Servidor Frontend Manualmente
Instale o NodeJS:

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

Instale as dependências:

`pnpm i`

Rode o programa no terminal:

`pnpm dev`

Em seguida, acesse o site no link http://localhost:3000/


### Rodando o Servidor Backend Manualmente
Antes de começar, certifique-se de ter o seguinte instalado na sua máquina:

- Java JDK
- Maven
- PostgreSQL

---

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
