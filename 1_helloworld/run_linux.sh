## This doesn't use a gradle plugin for native compilation, see next code sample

set -e
#set -o pipefail

NATIVE_IMAGE=/home/sarnobat/trash/graalvm-ce-java11-22.2.0/lib/svm/bin/native-image

test -f $NATIVE_IMAGE || echo "Does not exist: $NATIVE_IMAGE"
test -f $NATIVE_IMAGE || exit 1


GRAALVM_HOME=/home/sarnobat/trash/graalvm-ce-java11-22.2.0

test -d $GRAALVM_HOME || echo "Does not exist: $GRAALVM_HOME"
test -d $GRAALVM_HOME || exit 1

$GRAALVM_HOME/bin/gu install native-image

./gradlew jar

JAR_WITH_DEPS=build/libs/graal_aotc_java-1.0.jar

test -f $JAR_WITH_DEPS || echo "Does not exist: $JAR_WITH_DEPS"
test -f $JAR_WITH_DEPS || exit 1

# ./gradlew nativeBuild

java -jar build/libs/graal_aotc_java-1.0.jar

# Note: this is case-sensitive
$NATIVE_IMAGE -jar $JAR_WITH_DEPS --no-fallback --no-server -H:Class=com.Helloworld -H:Name=helloworld

./helloworld
