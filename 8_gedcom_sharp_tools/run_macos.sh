set -e
GRAAL=/Volumes/Apps/graalvm-ce-java11-22.2.0
ls -d $GRAAL
ls -d /Volumes/Apps/graalvm-ce-java11-22.2.0

## Ugh, this needs internet. So you can't do it on a plane or in another country. Unless there's a setting you can change.
$GRAAL/Contents/Home/lib/installer/bin/gu  install native-image
GRAALVM_HOME=$GRAAL/Contents/Home/ JAVA_HOME=$GRAAL/Contents/Home/ ./gradlew nativeCompile -b app/build.gradle
ls -lh app/build/native/nativeCompile/app
# cp -v app/build/native/nativeCompile/app gedcom.osx
cp -v app/build/native/nativeCompile/app gedcom2mwk.osx
cat <<EOF | batcat --plain --paging=never --language sh --theme TwoDark
./gedcom2mwk.osx ~/sarnobat.git/genealogy/rohidekar.ged | tee ~/sarnobat.git/genealogy/rohidekar.auto.`date -I`.mwk | pandoc --from mediawiki --to html | tee ~/sarnobat.git/genealogy/rohidekar.auto.`date -I`.mwk | xq | tee ~/sarnobat.git/genealogy/rohidekar.auto.`date -I`.html
EOF