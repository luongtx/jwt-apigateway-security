#!/bin/sh
set -eu

is_true() {
  case "${1:-false}" in
    1|true|TRUE|yes|YES|on|ON) return 0 ;;
    *) return 1 ;;
  esac
}

APP_JAR="${APP_JAR:-${1:-}}"
if [ -z "$APP_JAR" ]; then
  echo "ERROR: APP_JAR not set and no jar argument provided" >&2
  exit 2
fi

JAVA_OPTS="${JAVA_TOOL_OPTIONS:-}"

if is_true "${DEBUG:-false}"; then
  JDWP_PORT="${JDWP_PORT:-5005}"
  JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${JDWP_PORT}"
fi

if is_true "${JMC:-false}"; then
  JMX_PORT="${JMX_PORT:-9010}"
  JMX_HOSTNAME="${JMX_HOSTNAME:-localhost}"
  JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote=true"
  JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=${JMX_PORT}"
  JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.rmi.port=${JMX_PORT}"
  JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
  JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
  JAVA_OPTS="$JAVA_OPTS -Djava.rmi.server.hostname=${JMX_HOSTNAME}"
fi

if is_true "${JFR_PROFILE:-false}"; then
  JAVA_OPTS="$JAVA_OPTS -XX:StartFlightRecording=settings=profile,dumponexit=true,filename=/tmp/recording.jfr"
fi

export JAVA_TOOL_OPTIONS="$JAVA_OPTS"
exec java -jar "$APP_JAR"

