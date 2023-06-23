set -e
GRAALVM_DIST="/Volumes/trash/trash/graalvm-ce-java11-22.2.0/"
JDK_DIST="/usr/local/Cellar/openjdk@11/11.0.12/"
test -d "${GRAALVM_DIST}"
test -d "${JDK_DIST}"
"${GRAALVM_DIST}"/Contents/Home/lib/installer/bin/gu  install native-image
GRAALVM_HOME="${GRAALVM_DIST}"Contents/Home/ JAVA_HOME="$JDK_DIST" ./gradlew nativeCompile -b app/build.gradle
ls -lh app/build/native/nativeCompile/app
