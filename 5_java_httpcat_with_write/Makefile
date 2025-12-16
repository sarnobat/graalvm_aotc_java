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
run:
	# There are lots of dependencies: 
	# txt files, port changes, hostname changes, static file paths 
	# that need to be set up correctly. 
	# A Dockerfile would be good probably
	cd /Library/WebServer/Documents
	ln -sf /Volumes/git/github/2024/repos/yurl
	ls /Volumes/git/github/2024/repos/groovy_libraries
	socat TCP-LISTEN:44443,reuseaddr,fork TCP:127.0.0.1:4443 &
	./httpcatwithwrite2.arm.mac &
	cd /Volumes/git/github/2024/repos/yurl && export JAVA_OPTS="$JAVA_OPTS -Xmx64M" && java -classpath "/Volumes/git/github/2024/repos/groovy_libraries/.groovy/lib/*:/Volumes/git/github/2024/repos/groovy_libraries/.groovy/lib_java11/*"  YurlList.java --port=4443 -DcacheUrls=true

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
