GRAALVM ?= /Volumes/apps/graalvm-ce-java11-22.2.0/
GRAALVM_HOME := $(GRAALVM)/Contents/Home/
GRADLEW := ./gradlew
GRADLE_BUILD := app/build.gradle
PROJECT_DIR := $(CURDIR)
NATIVE_BINARY := $(PROJECT_DIR)/app/build/native/nativeCompile/app
HOST_ARCH ?= $(shell uname -m)
NATIVE_IMAGE_BIN := $(GRAALVM_HOME)/bin/native-image

.PHONY: run

run: java_callgraph.$(HOST_ARCH)
	find $(PROJECT_DIR)/ -iname "*class" | ./java_callgraph.$(HOST_ARCH) | tee /tmp/calls.csv
	cp /Volumes/git/2023/github/2023/d3_csv/d3.v3.min.js /tmp/
	sh ~/github/d3_csv/singlefile_automated/run_automated.sh | tee /tmp/index.html
	cat /tmp/calls.csv | sort | uniq | tee /tmp/1.html | sh /tmp/filter_public.sh | sh /tmp/csv2d3.sh | tee /tmp/index2.html
	open /tmp/index2.html
# 	cat - | sort | uniq | tee /tmp/1.html | sh /tmp/filter_public.sh | sh /tmp/csv2d3.sh | tee /tmp/index2.html
# 	open /tmp/index2.html

java_callgraph.$(HOST_ARCH): $(NATIVE_IMAGE_BIN)
	GRAALVM_HOME=$(GRAALVM_HOME) JAVA_HOME=$(GRAALVM_HOME) $(GRADLEW) nativeCompile -b $(GRADLE_BUILD)
	cp $(NATIVE_BINARY) java_callgraph.$(HOST_ARCH)

$(NATIVE_IMAGE_BIN):
	ls -d $(GRAALVM)
	$(GRAALVM_HOME)/lib/installer/bin/gu install native-image
