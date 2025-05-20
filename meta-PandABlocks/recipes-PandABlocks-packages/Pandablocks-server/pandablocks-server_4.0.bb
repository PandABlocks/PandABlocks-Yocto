SUMMARY = "This package is for fetching, building and installing the PandABlocks-server on the initramfs"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
FILES:${PN} = "/opt"

LICENCE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# variable ${S} is the location of the source in the working directory
S = "${WORKDIR}/git"

SRC_URI:append = " \
		git://https://github.com/PandABlocks/PandABlocks-server;branch=master;protocol=https;name=pandablocks-server \
		file://0001-Initial-Commit.patch \
		file://CONFIG \
		"

DEPENDS += "python3-sphinx-native"
DEPENDS += "python3-six-native"

SRCREV_pandablocks-server = "8debbfa3b9ca964cbb583d4259917aefaa131e66"

PROVIDES = "panda-server"

do_configure(){
	cp -f ${WORKDIR}/CONFIG ${S}
}

# do_compile Compiles the source code. This task runs with the current working directory set to ${B}
# The default behaviour of this task is to run the oe_runmake function if a makefile (Makefile, makefile, or GNUmakefile) is found. If no such file is found, the do_compile task does nothing

do_install() {
	install -d ${D}/opt/bin
	install -m 0755 ${S}/build/server/server ${D}/opt/bin
}

# possibility to "addtask do_deploy after do_compile
