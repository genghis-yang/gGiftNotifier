FROM ghcr.io/graalvm/graalvm-ce:21.1.0

WORKDIR /app

# Install native-image
RUN gu install native-image

ARG MUSL_VERSION="1.2.2"
ARG ZLIB_VERSION="1.2.11"
ARG SBT_VERSION="1.5.2"
ARG RESULT_LIB="/staticlibs"

ENV PATH="$PATH:${RESULT_LIB}/bin:/usr/local/sbt/bin"

# BEGIN PRE-REQUISITES FOR STATIC NATIVE IMAGES FOR GRAAL
# SEE: https://github.com/oracle/graal/blob/master/substratevm/StaticImages.md
RUN mkdir -p ${RESULT_LIB} && \
    curl -L -o musl.tar.gz https://musl.libc.org/releases/musl-${MUSL_VERSION}.tar.gz && \
    mkdir musl && tar -xvzf musl.tar.gz -C musl --strip-components 1 && cd musl && \
    ./configure --disable-shared --prefix=${RESULT_LIB} && \
    make && make install && \
    cp /usr/lib/gcc/x86_64-redhat-linux/8/libstdc++.a ${RESULT_LIB}/lib/ && \
    cd .. && rm -rf musl musl.tar.gz
ENV CC="musl-gcc"
RUN curl -L -o zlib.tar.gz https://zlib.net/zlib-${ZLIB_VERSION}.tar.gz && \
    mkdir zlib && tar -xvzf zlib.tar.gz -C zlib --strip-components 1 && cd zlib && \
    ./configure --static --prefix=${RESULT_LIB} && \
    make && make install && \
    cd .. && rm -rf zlib zlib.tar.gz
#END PRE-REQUISITES FOR STATIC NATIVE IMAGES FOR GRAAL

# Install sbt
RUN curl -L -o sbt.tgz https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz && \
    tar -xvzf sbt.tgz -C /usr/local && rm sbt.tgz