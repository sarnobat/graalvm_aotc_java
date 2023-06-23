set -e
GRAALVM_DIST="/Volumes/apps/graalvm-ce-java11-22.2.0"
# GRAALVM_DIST="/Volumes/trash/trash/graalvm-ce-java11-22.2.0/"
# JDK_DIST="/usr/local/Cellar/openjdk@11/11.0.12/"
JDK_DIST="/opt/homebrew/Cellar/openjdk/20.0.1/"
test -d "${GRAALVM_DIST}" || echo "no GRAALVM at ${GRAALVM_DIST}"
test -d "${JDK_DIST}" || echo "no JDK at ${JDK_DIST}"
"${GRAALVM_DIST}"/Contents/Home/lib/installer/bin/gu  install native-image
# GRAALVM_HOME="${GRAALVM_DIST}"Contents/Home/ JAVA_HOME="$JDK_DIST" ./gradlew nativeCompile -b app/build.gradle
GRAALVM_HOME="${GRAALVM_DIST}"/Contents/Home/ JAVA_HOME="${GRAALVM_DIST}"/Contents/Home/ ./gradlew nativeCompile -b app/build.gradle
ls -lh app/build/native/nativeCompile/app
cp app/build/native/nativeCompile/app app/build/native/nativeCompile/app.macos.m1