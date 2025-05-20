# ./Dockerfile
FROM rockylinux:9
# Host dependencies autoconf bmap-tools docbook-utils texinfo
RUN yum -y upgrade && yum -y install \
    automake \
    bzip2 \
    chrpath \
    cpio \
    cpp \
    diffstat \
    diffutils \
    dnf \
    epel-release \
    file \
    findutils \
    gawk \
    gcc \
    gcc-c++ \
    git \
    glibc-devel \
    glibc-langpack-en \
    gzip \
    hostname \
    iproute \
    libacl \
    libtool \
    lz4 \
    make \
    man \
	ncurses \
    patch \
    perl \
    perl-bignum \
    perl-Data-Dumper \
    python3-pexpect \
    perl-Text-ParseWords \
    perl-Thread-Queue \
    procps-ng \
	python3 \
    python3-jinja2 \
    python3-pip \
    socat \
    sudo \
    tar \
    unzip \
    wget \
    which \
    xalan-j2 \
    xmlto \
    xz \
    zstd

# Enable devel repo and install packages
RUN yum install --enablerepo devel -y \
	python3-sphinx \
	rpcgen \
	ncurses-compat-libs \
	perl-File-Compare \
	perl-locale \
	screen

RUN curl -SL https://dl.rockylinux.org/pub/rocky/8/AppStream/x86_64/os/Packages/p/perl-File-Copy-2.34-473.module+el8.10.0+1616+0d20cc68.noarch.rpm -o /tmp/perl-FileCopy-2.rpm && \
    yum -y install --skip-broken /tmp/perl-FileCopy-2.rpm && \
    rm /tmp/perl-FileCopy-2.rpm

# RUN curl

RUN yum -y group install "Development Tools"

#Get fakeroot which needs epel-release
#RUN yum -y install fakeroot

#install rpcgen



# Copy in scripts and dls rootfs, annotypes, pymalcolm, and malcolmjs
# COPY PandABlocks-rootfs/.github/scripts /scripts
# COPY rootfs /rootfs
# COPY annotypes /annotypes
# COPY pymalcolm /pymalcolm
# COPY malcolmjs /malcolmjs

# Toolchains and tar files
# RUN bash scripts/GNU-toolchain.sh
# RUN bash scripts/tar-files.sh

# For the documentation
#RUN pip3 install matplotlib \ 
#    rst2pdf \
#    sphinx \
#    sphinx-rtd-theme \
#    --upgrade docutils==0.16

# Create config file for dls-rootfs
# RUN bash scripts/config-file-rootfs.sh

# Error can't find python
RUN ln -s /usr/bin/python3 /usr/bin/python

#RUN echo "en_US.UTF-8 UTF-8" > /etc/locale.gen && \
#    locale-gen

ENV LANG en_US.utf8

# Make sure git doesn't fail when used to obtain a tag name
RUN git config --global --add safe.directory '*'

# Entrypoint into the container
WORKDIR /repos
CMD ["/bin/bash"]
RUN useradd yocto_user -u 1000
RUN usermod -a -G yocto_user root
RUN su - yocto_user
