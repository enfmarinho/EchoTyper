FROM node:18

# Instala PNPM globalmente
RUN npm install -g pnpm

WORKDIR /app

COPY package.json pnpm-lock.yaml ./
RUN pnpm install

COPY . .

RUN pnpm run dev  # TODO maybe change this for build

EXPOSE 3000
CMD ["pnpm", "run", "dev"]
