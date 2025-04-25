#! /bin/bash

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
