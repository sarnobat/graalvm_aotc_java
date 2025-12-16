APP := httpcatwithwrite2

# ==============================
# JVMs (exact paths)
# ==============================

# macOS GraalVM 17 (arm64)
GRAALVM_MAC_17 := /Library/Java/JavaVirtualMachines/graalvm-17.jdk/Contents/Home

# macOS OpenJDK 21 (Homebrew)
OPENJDK21_MAC := /opt/homebrew/Cellar/openjdk@21/21.0.7/libexec/openjdk.jdk/Contents/Home

# Linux GraalVM (native install)
GRAALVM_LINUX := /usr/lib/jvm/graalvm-17

# Linux GraalVM (Docker)
GRAALVM_LINUX_IMAGE := ghcr.io/graalvm/native-image-community:17

# ==============================
# macOS native image
# ==============================
.PHONY: httpcatwithwrite2.arm.mac
httpcatwithwrite2.arm.mac:
	@echo "==> Building macOS arm64 native image"
	JAVA_HOME=$(GRAALVM_MAC_17) \
	./gradlew clean nativeCompile
	cp app/build/native/nativeCompile/$(APP) $@

# ==============================
# Linux native image (Docker)
# ==============================
.PHONY: httpcatwithwrite2.linux
httpcatwithwrite2.linux:
	@echo "==> Building Linux native image (Docker)"
	JAVA_HOME=$(OPENJDK21_MAC) \
	docker run --rm \
	  -v "$$(pwd)":/workspace \
	  -w /workspace \
	  $(GRAALVM_LINUX_IMAGE) \
	  ./gradlew clean nativeCompile
	cp app/build/native/nativeCompile/$(APP) $@

# ==============================
# Clean
# ==============================
.PHONY: clean
clean:
	./gradlew clean
	rm -f httpcatwithwrite2.arm.mac httpcatwithwrite2.linux
