set -e

GRAALVM_HOME=/Volumes/Apps/graalvm-ce-java11-22.2.0/Contents/Home/

test -d $GRAALVM_HOME || echo "4 Does not exist: $GRAALVM_HOME"
test -d $GRAALVM_HOME || exit 1

NATIVE_IMAGE=$GRAALVM_HOME/lib/svm/bin/native-image
echo "$NATIVE_IMAGE"

set -e
GRAAL=/Volumes/Apps/graalvm-ce-java11-22.2.0
ls -d $GRAAL
ls -d /Volumes/Apps/graalvm-ce-java11-22.2.0
$GRAAL/Contents/Home/lib/installer/bin/gu  install native-image
GRAALVM_HOME=$GRAAL/Contents/Home/ JAVA_HOME=$GRAAL/Contents/Home/ ./gradlew nativeCompile -b app/build.gradle
ls -lh app/build/native/nativeCompile/app
cp -v app/build/native/nativeCompile/app csv2path.osx

# to do the native compile separately
$GRAALVM_HOME/bin/native-image -H:Class=com.App -jar $GRAALVM_HOME/app/build/libs/app.jar -H:Name=csv2path.osx
