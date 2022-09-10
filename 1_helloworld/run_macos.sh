## This doesn't use a gradle plugin for native compilation, see next code sample

set -e
#set -o pipefail


GRAALVM_HOME=/Library/Java/JavaVirtualMachines/graalvm-ce-java11-20.1.0/Contents/Home/

test -d $GRAALVM_HOME || echo "Does not exist: $GRAALVM_HOME"
test -d $GRAALVM_HOME || exit 1

# Downloads "native-image"
$GRAALVM_HOME/bin/gu install native-image

NATIVE_IMAGE=/Library/Java/JavaVirtualMachines/graalvm-ce-java11-20.1.0/Contents/Home/lib/svm/bin/native-image

test -f $NATIVE_IMAGE || echo "Does not exist: $NATIVE_IMAGE"
test -f $NATIVE_IMAGE || exit 1


./gradlew jar

JAR_WITH_DEPS=build/libs/graal_aotc_java-1.0.jar

test -f $JAR_WITH_DEPS || echo "Does not exist: $JAR_WITH_DEPS"
test -f $JAR_WITH_DEPS || exit 1

# ./gradlew nativeBuild

java -jar /Volumes/git/github/docker_image_helloworld/graal_aotc_java/build/libs/graal_aotc_java-1.0.jar

# Note: this is case-sensitive
$NATIVE_IMAGE -jar $JAR_WITH_DEPS --no-fallback --no-server -H:Class=com.Helloworld -H:Name=helloworld.macos

./helloworld.macos
