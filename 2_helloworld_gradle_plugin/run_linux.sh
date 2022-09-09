set -e
GRAALVM_HOME=/home/sarnobat/trash/graalvm-ce-java11-22.2.0

test -d $GRAALVM_HOME || echo "Does not exist: $GRAALVM_HOME"
test -d $GRAALVM_HOME || exit 1

$GRAALVM_HOME/bin/gu install native-image

./gradlew jar

JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
ls -d $JAVA_HOME

$GRAALVM_HOME/lib/installer/bin/gu  install native-image
echo "Use -R:MaxHeapSize=14g , not -J-Xmx14g"
GRAALVM_HOME=/home/sarnobat/trash/graalvm-ce-java11-22.2.0 JAVA_HOME=/home/sarnobat/trash/graalvm-ce-java11-22.2.0 ./gradlew nativeCompile -b app/build.gradle
ls -lh app/build/native/nativeCompile/app
