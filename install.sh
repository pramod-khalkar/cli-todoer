#!/bin/bash
set -e

INSTALL_DIR="$HOME/.todo-cli"
BIN_DIR="$HOME/.local/bin"
JAR_NAME="cli-todoer.jar"
CMD_NAME="todo"

echo "📁 Creating install directory at $INSTALL_DIR..."
mkdir -p "$INSTALL_DIR"

echo "📁 Creating bin directory at $BIN_DIR..."
mkdir -p "$BIN_DIR"

echo "📦 Copying $JAR_NAME to $INSTALL_DIR..."
cp "$JAR_NAME" "$INSTALL_DIR/$JAR_NAME"

echo "📝 Creating launcher script at $BIN_DIR/$CMD_NAME..."
cat > "$BIN_DIR/$CMD_NAME" <<EOF
#!/bin/bash
java -jar "$INSTALL_DIR/$JAR_NAME" "\$@"
EOF

chmod +x "$BIN_DIR/$CMD_NAME"

EXPORT_CMD='export PATH="$HOME/.local/bin:$PATH"'

case "$SHELL" in
  */zsh)
    CONFIG_FILE="$HOME/.zshrc"
    SOURCE_CMD="source $CONFIG_FILE"
    ;;
  */bash)
    CONFIG_FILE="$HOME/.bashrc"
    SOURCE_CMD="source $CONFIG_FILE"
    ;;
  */fish)
    CONFIG_FILE="$HOME/.config/fish/config.fish"
    EXPORT_CMD='set -Ux PATH $HOME/.local/bin $PATH'
    SOURCE_CMD="source $CONFIG_FILE"
    ;;
  *)
    CONFIG_FILE="$HOME/.profile"
    SOURCE_CMD="source $CONFIG_FILE"
    ;;
esac

if ! grep -Fxq "$EXPORT_CMD" "$CONFIG_FILE"; then
  echo "$EXPORT_CMD" >> "$CONFIG_FILE"
  echo "🔄 Added PATH to $CONFIG_FILE. Running: $SOURCE_CMD"
  $SOURCE_CMD
else
  echo "ℹ️  PATH already set in $CONFIG_FILE"
fi

echo "✅ Installed! Try running:"
echo "$CMD_NAME --help"
