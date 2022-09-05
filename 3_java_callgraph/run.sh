set -e
ls -d /Volumes/trash/trash/graalvm-ce-java11-22.2.0/
ls -d /usr/local/Cellar/openjdk@11/11.0.12/
/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/lib/installer/bin/gu  install native-image
GRAALVM_HOME=/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/ JAVA_HOME=/usr/local/Cellar/openjdk@11/11.0.12/ ./gradlew nativeCompile -b app/build.gradle
#ls -lh app/build/native/nativeCompile/app
/Volumes/git/github/graalvm_aotc_java/3_java_callgraph/app/build/native/nativeCompile/app /Volumes/git/github/graalvm_aotc_java/3_java_callgraph/
