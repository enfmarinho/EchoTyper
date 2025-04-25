#! /bin/bash
set -e

# Downloading and installing nvm
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.2/install.sh | bash

# Restarting shell
\. "$HOME/.nvm/nvm.sh"

# Downloading and installing Node.js
nvm install 22

# Downloading and installing pnpm
corepack enable pnpm

echo "front end tools successfully installed"

# Setting up environment variables
JDK_VERSION="17"
MAVEN_VERSION="3.9.6"
INSTALL_DIR="$HOME/.local"
JAVA_DIR="$INSTALL_DIR/java"
MAVEN_DIR="$INSTALL_DIR/maven"

# Creating destination directories
mkdir -p "$JAVA_DIR" "$MAVEN_DIR"

# Downloading and installing JDK
echo "Downloading OpenJDK $JDK_VERSION from Adoptium..."
curl -L -o temurin.tar.gz "https://api.adoptium.net/v3/binary/latest/$JDK_VERSION/ga/linux/x64/jdk/hotspot/normal/eclipse?project=jdk"
echo "Extracting JDK..."
tar -xf temurin.tar.gz -C "$JAVA_DIR" --strip-components=1
rm temurin.tar.gz

# Downloading and installing Maven
echo "Downloading Maven $MAVEN_VERSION..."
curl -L -o maven.tar.gz "https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz"
echo "Extracting Maven..."
tar -xf maven.tar.gz -C "$MAVEN_DIR" --strip-components=1
rm maven.tar.gz

echo "Setting up environment..."
cat <<EOF

✅ Installation complete.

To use Java and Maven, add the following to your shell config (e.g., ~/.bashrc or ~/.zshrc):

export PATH="$JAVA_DIR/bin:$MAVEN_DIR/bin:\$PATH"

Then run:
  source ~/.bashrc   # or source ~/.zshrc

To check versions:
  java -version
  mvn -version
EOF
