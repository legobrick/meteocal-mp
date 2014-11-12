#!/bin/bash
SAVEIFS=$IFS
IFS=$(echo -en "\n\b")
list=($(find . -name "*.asta"))
IFS=$AVEIFS
for i in "${!list[@]}"; do
    /usr/lib/astah_professional/astah-command.sh -f "${list[$i]}" -image -t png -o ${list[$i]%/*}
    a=${list[$i]##*/}
    pct=$(bc <<< "scale=2; $i*100/${#list[@]}")
    printf "%-30s  %-30s \n" "Image for ${a%.*} created!" "[ $pct% ]"
done
exit 0
