#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 2 ]]; then
  echo "Usage: $0 <jwt-token> <request-count>"
  exit 1
fi

token="$1"
count="$2"
url="http://localhost:8080/api/v1/dashboard?patientId=p-001"

for i in $(seq 1 "$count"); do
  response=$(curl -sS "$url" \
    -H "Authorization: Bearer $token" \
    -H "X-Correlation-Id: demo3-$i")

  served_by=$(printf "%s" "$response" | sed -n 's/.*"servedBy"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p')
  if [[ -z "$served_by" ]]; then
    served_by="unknown"
  fi

  printf "request=%s servedBy=%s\n" "$i" "$served_by"
done
