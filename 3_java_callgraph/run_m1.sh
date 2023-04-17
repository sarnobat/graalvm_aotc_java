set -e
GRAALVM_HOME=/Volumes/trash/trash/2022/graalvm-ce-java11-22.2.0/
# JDK_HOME=/Volumes/apps/homebrew/Cellar/openjdk@11/11.0.15/
JDK_HOME=$GRAALVM_HOME
ls -d $GRAALVM_HOME
ls -d $JDK_HOME
$GRAALVM_HOME/Contents/Home/lib/installer/bin/gu  install native-image
GRAALVM_HOME=$GRAALVM_HOME/Contents/Home/ JAVA_HOME=$JDK_HOME/Contents/Home ./gradlew nativeCompile -b app/build.gradle
#ls -lh app/build/native/nativeCompile/app
mv /Volumes/git/github/graalvm_aotc_java/3_java_callgraph/app/build/native/nativeCompile/app /Volumes/git/github/graalvm_aotc_java/3_java_callgraph/java_callgraph.m1
# /Volumes/git/github/graalvm_aotc_java/3_java_callgraph/java_callgraph.m1 ~/jd/module/ | tee /tmp/calls.csv
find ~/jd/module/ -maxdepth 50 -type f -iname "**class" | grep -i -e jwt -e micro | /Volumes/git/github/graalvm_aotc_java/3_java_callgraph/java_callgraph.m1 | tee /tmp/calls.csv
echo "now execute:\nsh ~/github/d3_csv/singlefile_automated/run_automated.sh | tee /tmp/index.html"