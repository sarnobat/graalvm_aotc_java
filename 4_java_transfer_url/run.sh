set -e
ls -d /Volumes/trash/trash/graalvm-ce-java11-22.2.0/
ls -d /usr/local/Cellar/openjdk@11/11.0.12/
/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/lib/installer/bin/gu  install native-image
GRAALVM_HOME=/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/ JAVA_HOME=/usr/local/Cellar/openjdk@11/11.0.12/ ./gradlew clean nativeCompile -b app/build.gradle 
ls -lh app/build/native/nativeCompile/app

cat <<EOF
# /Volumes/trash/trash/jersey/examples/helloworld-programmatic
GRAALVM_HOME=/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/ JAVA_HOME=/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/ mvn package -P native-image
EOF