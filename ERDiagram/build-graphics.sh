#!/bin/bash
if [ -s "/Applications/astah professional/astah-command.sh" ]; then
    acmd="/Applications/astah professional/astah-command.sh"
else
    acmd="/usr/lib/astah_professional/astah-command.sh"
fi
SAVEIFS=$IFS
IFS=$(echo -en "\n\b")
list=($(find . -name "*.asta"))
IFS=$AVEIFS
for i in "${!list[@]}"; do
    $($acmd -f "${list[$i]}" -image -t png -o ${list[$i]%/*})
    a=${list[$i]##*/}
    pct=$(echo $(bc <<< "scale=2; ($i+1)*100/${#list[@]}") | sed -r "/[0-9]+\.[0-9]+/s/\./,/g")
    printf "%-70s  %s%6.2f%s \n" "Image for ${a%.*} created!" "[ " "$pct" "% ]"
done
exit 0
