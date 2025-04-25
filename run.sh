#!/bin/bash
set -e

# Install frontend dependencies
cd front-end/echo-typer
pnpm install
cd ../../

# Run frontend and backend concurrently
npx concurrently \
  "cd front-end/echo-typer && pnpm dev" \
  "cd backend/EchoTyper && mvn clean install && mvn spring-boot:run"
 
