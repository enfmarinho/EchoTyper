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
### Docker
Para rodar o projeto utilize:
```
git clone https://github.com/enfmarinho/EchoTyper
cd EchoTyper/src
docker compose build
docker compose up
```

Dependendo da sua máquina, pode ser necessário utilizar sudo para executar os últimos dois comandos acima. 
O primeiro comando irá instalar o repositório, o terceiro irá resolver as dependências do projeto no container,
o quarto irá rodar o container. 

Em seguida, acesse o site no link http://localhost:3000/

Note que o backend ficará disponível por padrão em `http://localhost:8081`.
