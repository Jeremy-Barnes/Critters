sudo sed -i 's/\(<[^"]*"connection.username">\)\([^<]*\)\(<[^>]*\)/\1USERNAMEHERE\3/g' ../../../../CritterServer/src/main/resources/hibernate.cfg.xml;
sudo sed -i 's/\(<[^"]*"connection.password">\)\([^<]*\)\(<[^>]*\)/\1PASSWORDHERE\3/g' ../../../../CritterServer/src/main/resources/hibernate.cfg.xml;
sudo sed -i 's/\([^"]*user" value="\)\([^<\/">]*\)\(["]*\)/\1USERNAMEHERE\3/g' ../../../../CritterServer/src/main/resources/META-INF/persistence.xml;
sudo sed -i 's/\([^"]*password" value="\)\([^<\/">]*\)\(["]*\)/\1PASSWORDHERE\3/g' ../../../../CritterServer/src/main/resources/META-INF/persistence.xml;

echo "PRINTING";
printf '%b\n' "$(cat ../../../../CritterServer/src/main/resources/hibernate.cfg.xml)";
printf '%b\n' "$(cat ../../../../CritterServer/src/main/resources/META-INF/persistence.xml)";
echo "PRINTED";