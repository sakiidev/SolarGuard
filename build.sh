cat << 'EOF' > build.sh
#!/bin/bash
echo "Iniciando compilação do SolarGuard..."
mvn clean package
if [ $? -eq 0 ]; then
    echo "---------------------------------------"
    echo "SUCESSO: O plugin está na pasta target/"
    echo "---------------------------------------"
else
    echo "ERRO: Falha na compilação. Verifique o código."
fi
EOF
chmod +x build.sh
