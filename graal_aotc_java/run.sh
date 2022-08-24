NATIVE_IMAGE=/Library/Java/JavaVirtualMachines/graalvm-ce-java11-20.1.0/Contents/Home/lib/svm/bin/native-image

test -f $NATIVE_IMAGE || echo "Does not exist: $NATIVE_IMAGE"
test -f $NATIVE_IMAGE || exit 1

JAR_WITH_DEPS=./target/graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar

test -f $JAR_WITH_DEPS || echo "Does not exist: $JAR_WITH_DEPS"
test -f $JAR_WITH_DEPS || exit 1

native-image -jar $JAR_WITH_DEPS --no-fallback --no-server -H:Class=oracle.HelloWorld -H:Name=helloworld
