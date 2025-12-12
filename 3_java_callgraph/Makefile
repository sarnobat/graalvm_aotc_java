.DEFAULT_GOAL := run

GRAALVM ?= /Volumes/apps/graalvm-ce-java11-22.2.0/
GRAALVM_HOME := $(GRAALVM)/Contents/Home/
GRADLEW := ./gradlew
GRADLE_BUILD := app/build.gradle
PROJECT_DIR := $(CURDIR)
NATIVE_BINARY := $(PROJECT_DIR)/app/build/native/nativeCompile/app
WORKDIR := $(PROJECT_DIR)/

.PHONY: run build install-native-image

run: build
	@echo "$(NATIVE_BINARY) $(WORKDIR)"

build: install-native-image
	GRAALVM_HOME=$(GRAALVM_HOME) JAVA_HOME=$(GRAALVM_HOME) $(GRADLEW) nativeCompile -b $(GRADLE_BUILD)

install-native-image:
	ls -d $(GRAALVM)
	$(GRAALVM_HOME)/lib/installer/bin/gu install native-image
