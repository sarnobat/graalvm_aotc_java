GRAALVM ?= /Volumes/apps/graalvm-ce-java11-22.2.0/
GRAALVM_HOME := $(GRAALVM)/Contents/Home/
GRADLEW := ./gradlew
GRADLE_BUILD := app/build.gradle
PROJECT_DIR := $(CURDIR)
NATIVE_BINARY := $(PROJECT_DIR)/app/build/native/nativeCompile/app
WORKDIR := $(PROJECT_DIR)/
HOST_ARCH ?= $(shell uname -m)
NATIVE_IMAGE_BIN := $(GRAALVM_HOME)/bin/native-image

# .PHONY: run build install-native-image

# run_x86_64: java_callgraph.$(HOST_ARCH)
# 	find $(PROJECT_DIR)/ -iname "*class" | ./java_callgraph.$(HOST_ARCH)

run:	java_callgraph.$(HOST_ARCH)
	find $(PROJECT_DIR)/ -iname "*class" | ./java_callgraph.$(HOST_ARCH)

# build: install-native-image
# 	GRAALVM_HOME=$(GRAALVM_HOME) JAVA_HOME=$(GRAALVM_HOME) $(GRADLEW) nativeCompile -b $(GRADLE_BUILD)
# 	cp $(NATIVE_BINARY) java_callgraph.$(HOST_ARCH)

java_callgraph.$(HOST_ARCH): $(NATIVE_IMAGE_BIN)
	GRAALVM_HOME=$(GRAALVM_HOME) JAVA_HOME=$(GRAALVM_HOME) $(GRADLEW) nativeCompile -b $(GRADLE_BUILD)
	cp $(NATIVE_BINARY) java_callgraph.$(HOST_ARCH)

$(NATIVE_IMAGE_BIN):
	ls -d $(GRAALVM)
	$(GRAALVM_HOME)/lib/installer/bin/gu install native-image
