echo 'TSC Compile'
pwd
(cd ../../../../CritterClient/CritterClient/; npm install;)
(cd ../../../../CritterClient/CritterClient/; tsc -p './tsconfig.json';)