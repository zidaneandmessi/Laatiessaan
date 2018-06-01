set -e
cd "$(dirname "$0")"
export CCHK="java -classpath ./lib/laatiessaan.jar:./bin/classes gzotpa.core.Compiler --check-semantic /dev/stdin"
cat > program.txt   # save everything in stdin to program.txt
$CCHK