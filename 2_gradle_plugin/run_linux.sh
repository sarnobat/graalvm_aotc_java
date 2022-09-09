set -e
GRAALVM_HOME=/home/sarnobat/trash/graalvm-ce-java11-22.2.0

test -d $GRAALVM_HOME || echo "Does not exist: $GRAALVM_HOME"
test -d $GRAALVM_HOME || exit 1

$GRAALVM_HOME/bin/gu install native-image

./gradlew jar

ls -d /usr/local/Cellar/openjdk@11/11.0.12/
/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/lib/installer/bin/gu  install native-image
GRAALVM_HOME=/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/ JAVA_HOME=/usr/local/Cellar/openjdk@11/11.0.12/ ./gradlew nativeCompile -b app/build.gradle
ls -lh app/build/native/nativeCompile/app
