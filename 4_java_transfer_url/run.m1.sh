# For use on work computer
set -e
ls -d /Volumes/trash/trash/graalvm-ce-java17-22.2.0/
ls -d /Volumes/Apps/homebrew/Cellar/openjdk@17/17.0.3/libexec/openjdk.jdk/Contents/Home
/Volumes/trash/trash/graalvm-ce-java17-22.2.0/Contents/Home/lib/installer/bin/gu install native-image
sudo xattr -r -d com.apple.quarantine /Volumes/trash/trash/graalvm-ce-java17-22.2.0/
GRAALVM_HOME=/Volumes/trash/trash/graalvm-ce-java17-22.2.0/Contents/Home JAVA_HOME=/Volumes/trash/trash/graalvm-ce-java17-22.2.0/Contents/Home/ ./gradlew clean nativeCompile -b app/build.gradle --info
ls -lh app/build/native/nativeCompile/app
