#!/bin/bash
SAVEIFS=$IFS
IFS=$(echo -en "\n\b")
for i in $(find . -name "*.asta"); do
    /usr/lib/astah_professional/astah-command.sh -f "${i}" -image -t png -o ${i%/*}
done
IFS=$AVEIFS
exit 0
