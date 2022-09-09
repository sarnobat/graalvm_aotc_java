set -e
ls -d $HOME/trash/graalvm-ce-java11-22.2.0/
ls -d /usr/lib/jvm/java-11-openjdk-amd64/
$HOME/trash/graalvm-ce-java11-22.2.0//lib/installer/bin/gu  install native-image
GRAALVM_HOME=$HOME/trash/graalvm-ce-java11-22.2.0/ JAVA_HOME=$HOME/trash/graalvm-ce-java11-22.2.0/ ./gradlew clean nativeCompile -b app/build.gradle --debug
ls -lh app/build/native/nativeCompile/app

cat <<EOF
# /Volumes/trash/trash/jersey/examples/helloworld-programmatic
GRAALVM_HOME=$HOME/trash/graalvm-ce-java11-22.2.0/ JAVA_HOME=$HOME/trash/graalvm-ce-java11-22.2.0/ mvn package -P native-image
EOF
