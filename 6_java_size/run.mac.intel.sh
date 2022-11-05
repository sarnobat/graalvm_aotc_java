cat <<EOF
I only got this working after the M1 build was used for weeks on my work laptop.
Note that the build took 3 mins 31 secs
EOF
set -e
ls -d /Volumes/trash/trash/graalvm-ce-java11-22.2.0/
ls -d /usr/local/Cellar/openjdk@11/11.0.12/
/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/lib/installer/bin/gu  install native-image
# If you try to use a vanilla JAVA_HOME with v11 you'll get
# Error: Could not find option 'BuildOutputColorful'
GRAALVM_HOME=/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/ JAVA_HOME=/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/ ./gradlew clean nativeCompile -b app/build.gradle --info
ls -lh app/build/native/nativeCompile/app

cat <<EOF
# /Volumes/trash/trash/jersey/examples/helloworld-programmatic
GRAALVM_HOME=/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/ JAVA_HOME=/Volumes/trash/trash/graalvm-ce-java11-22.2.0/Contents/Home/ mvn package -P native-image
EOF
