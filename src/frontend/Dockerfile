FROM node:22

# Enable PNPM
RUN corepack enable && corepack prepare pnpm@latest --activate

# Fix to permission bugs
RUN mkdir -p /app && chown -R node:node /app
USER node

WORKDIR /app

# Copy dependencies files and install dependencies
COPY --chown=node:node package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile

# Copy remaining files
COPY --chown=node:node . .

EXPOSE 3000
CMD ["pnpm", "run", "dev"]
