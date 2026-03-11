#!/bin/bash

usage() {
  echo "Usage: debug.sh [-r] [-s SERVICE_NAME]"
  echo "  -r  Rebuild images (passes --build to docker-compose)"
  echo "  -s  Start only the specified service"
  exit 1
}

RUN_ARGS=()
SERVICE_NAME=""

while getopts ":rs:" opt; do
  case "$opt" in
    r)
      RUN_ARGS+=(--build)
      ;;
    s)
      SERVICE_NAME="$OPTARG"
      ;;
    \?)
      echo "Error: Invalid option -$OPTARG" >&2
      usage
      ;;
    :)
      echo "Error: Option -$OPTARG requires an argument." >&2
      usage
      ;;
  esac
done

shift $((OPTIND - 1))

if [[ -n "$SERVICE_NAME" ]]; then
  DEBUG=true docker-compose up "${RUN_ARGS[@]}" -d "$SERVICE_NAME"
else
  DEBUG=true docker-compose up "${RUN_ARGS[@]}" -d
fi