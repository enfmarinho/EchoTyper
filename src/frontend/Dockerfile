FROM node:22

# Enable PNPM
RUN corepack enable && corepack prepare pnpm@latest --activate

# Fix to permission bugs
RUN mkdir -p /app && chown -R node:node /app
USER node

WORKDIR /app
COPY --chown=node:node . .

RUN pnpm install --frozen-lockfile

EXPOSE 3000
CMD ["pnpm", "run", "dev"]
