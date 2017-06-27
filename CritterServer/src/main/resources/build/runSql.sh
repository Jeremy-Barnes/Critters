for f in ../../../../CritterServer/src/main/sql/*.sql; do
 echo "Exec $f"
 versionhist=`psql -d critters build -t -c "select count(*) from version_history where filename='$f';"`
 if [ $versionhist -lt 1 ]; then
     psql -d critters -a -f $f
     psql -d critters -c "insert into version_history values ('$f', now())"
 fi
done