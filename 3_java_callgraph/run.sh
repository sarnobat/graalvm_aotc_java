set -e
GRAALVM=/Volumes/apps/graalvm-ce-java11-22.2.0/
ls -d $GRAALVM
# ls -d /usr/local/Cellar/openjdk@11/11.0.12/
$GRAALVM/Contents/Home/lib/installer/bin/gu  install native-image
GRAALVM_HOME=$GRAALVM/Contents/Home/ JAVA_HOME=$GRAALVM/Contents/Home/ ./gradlew nativeCompile -b app/build.gradle
#ls -lh app/build/native/nativeCompile/app
/Volumes/git/github/graalvm_aotc_java/3_java_callgraph/app/build/native/nativeCompile/app /Volumes/git/github/graalvm_aotc_java/3_java_callgraph/

