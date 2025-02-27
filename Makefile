.PHONY: all clean build setup test compile package push

all: clean build push
build: setup test compile package

clean:
	rm archive.zip
setup:

test:

compile:
	docker build -t al2023-graalvm21:native-app . &&  \
	docker run --platform linux/amd64 \
	-v $(GITHUB_WORKSPACE):$(GITHUB_WORKSPACE) \
	-w $(GITHUB_WORKSPACE) \
	-v ~/.m2:/root/.m2 \
	al2023-graalvm21:native-app \
	./mvnw clean -X -Pnative -DskipTests native:compile
package:
	zip archive.zip -9 -j bootstrap ./target/spring-cloud-function-demo && \
	unzip -l archive.zip
push:
	aws s3 cp archive.zip $(ASSET_TARGET_URL)

