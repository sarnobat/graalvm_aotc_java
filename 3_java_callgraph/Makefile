.DEFAULT_GOAL := run

GRAALVM ?= /Volumes/apps/graalvm-ce-java11-22.2.0/
GRAALVM_HOME := $(GRAALVM)/Contents/Home/
GRADLEW := ./gradlew
GRADLE_BUILD := app/build.gradle
NATIVE_BINARY := /Volumes/git/github/graalvm_aotc_java/3_java_callgraph/app/build/native/nativeCompile/app
WORKDIR := /Volumes/git/github/graalvm_aotc_java/3_java_callgraph/

.PHONY: run build install-native-image

run: build
	@echo "$(NATIVE_BINARY) $(WORKDIR)"

build: install-native-image
	GRAALVM_HOME=$(GRAALVM_HOME) JAVA_HOME=$(GRAALVM_HOME) $(GRADLEW) nativeCompile -b $(GRADLE_BUILD)

install-native-image:
	ls -d $(GRAALVM)
	$(GRAALVM_HOME)/lib/installer/bin/gu install native-image
