set -e
GRAAL=/Volumes/Apps/graalvm-ce-java11-22.2.0
ls -d $GRAAL
ls -d /Volumes/Apps/graalvm-ce-java11-22.2.0
$GRAAL/Contents/Home/lib/installer/bin/gu  install native-image
GRAALVM_HOME=$GRAAL/Contents/Home/ JAVA_HOME=$GRAAL/Contents/Home/ ./gradlew nativeCompile -b app/build.gradle
ls -lh app/build/native/nativeCompile/app
cp -v app/build/native/nativeCompile/app bashparser.osx
