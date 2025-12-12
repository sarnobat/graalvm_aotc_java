GRAALVM ?= /Volumes/apps/graalvm-ce-java11-22.2.0/
GRAALVM_HOME := $(GRAALVM)/Contents/Home/
GRADLEW := ./gradlew
GRADLE_BUILD := app/build.gradle
PROJECT_DIR := $(CURDIR)
NATIVE_BINARY := $(PROJECT_DIR)/app/build/native/nativeCompile/app
WORKDIR := $(PROJECT_DIR)/

# .PHONY: run build install-native-image

run_x86_64: java_callgraph.x86_64
	find $(PROJECT_DIR)/ -iname "*class" |  ./java_callgraph.x86_64

run: build
	find $(PROJECT_DIR)/ -iname "*class" | $(NATIVE_BINARY)

build: install-native-image
	GRAALVM_HOME=$(GRAALVM_HOME) JAVA_HOME=$(GRAALVM_HOME) $(GRADLEW) nativeCompile -b $(GRADLE_BUILD)

java_callgraph.x86_64:
	GRAALVM_HOME=$(GRAALVM_HOME) JAVA_HOME=$(GRAALVM_HOME) $(GRADLEW) nativeCompile -b $(GRADLE_BUILD)
	cp $(NATIVE_BINARY) java_callgraph.x86_64

install-native-image:
	ls -d $(GRAALVM)
	$(GRAALVM_HOME)/lib/installer/bin/gu install native-image
