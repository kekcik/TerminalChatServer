mvn package
git add .
git commit -m "auto: $1"
git push http://github.com/kekcik/TerminalChatServer master
sshpass -p "1a296a9daNt2" ssh -o StrictHostKeyChecking=no root@93.189.43.66 "bash run.sh" 





