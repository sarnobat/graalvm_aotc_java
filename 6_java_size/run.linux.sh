set -e
ls -d $HOME/trash/graalvm-ce-java11-22.2.0/
ls -d /usr/lib/jvm/java-11-openjdk-amd64/
$HOME/trash/graalvm-ce-java11-22.2.0//lib/installer/bin/gu  install native-image
cat <<EOF
!!!! If the next part runs out of memory, do killall java and check free -h (I needed that on nuc2020)!!!!
EOF
GRAALVM_HOME=$HOME/trash/graalvm-ce-java11-22.2.0/ JAVA_HOME=$HOME/trash/graalvm-ce-java11-22.2.0/ ./gradlew clean nativeCompile -b app/build.gradle
ls -lh app/build/native/nativeCompile/app
cp app/build/native/nativeCompile/app ./size.linux
