set -e
#set -o pipefail

# GRAALVM_HOME=/Library/Java/JavaVirtualMachines/graalvm-ce-java11-20.1.0/Contents/Home/
# GRAALVM_HOME=/Volumes/trash/trash/graalvm-jdk-17.0.7+8.1
GRAALVM_HOME=/Volumes/Apps/graalvm-ce-java11-22.2.0/Contents/Home/

test -d $GRAALVM_HOME || echo "4 Does not exist: $GRAALVM_HOME"
test -d $GRAALVM_HOME || exit 1

NATIVE_IMAGE=$GRAALVM_HOME/lib/svm/bin/native-image
JAR_WITH_DEPS=build/libs/graal_aotc_java-1.0.jar

cat <<EOF | tee /tmp/compile_graalvm.sh | batcat --plain --paging=never --language sh --theme TwoDark
set -e

## This doesn't use a gradle plugin for native compilation, see next code sample

# Downloads "native-image"
$GRAALVM_HOME/bin/gu install native-image


test -z $NATIVE_IMAGE && echo "1 Does not exist: $NATIVE_IMAGE"
test -f $NATIVE_IMAGE || echo "2 Does not exist: $NATIVE_IMAGE"
test -f $NATIVE_IMAGE || exit 1
test -z $NATIVE_IMAGE && exit 1


./gradlew jar



test -f $JAR_WITH_DEPS || echo "3 Does not exist: $JAR_WITH_DEPS"
test -f $JAR_WITH_DEPS || exit 1

# ./gradlew nativeBuild

java -jar ./build/libs/graal_aotc_java-1.0.jar

# Note: this is case-sensitive
$NATIVE_IMAGE -jar $JAR_WITH_DEPS --no-fallback --no-server -H:Class=com.Helloworld -H:Name=helloworld.macos

./helloworld.macos
EOF

cat <<EOF | batcat --style=plain --paging=never --language sh --theme TwoDark

Note we don't use gradle to create the native image, just the jar
sh /tmp/compile_graalvm.sh
EOF