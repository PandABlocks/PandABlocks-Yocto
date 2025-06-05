SUMMARY = "Recipe to fetch, configure, build and pakage the PandABlocks-driver on the PandABlocks"

FILESEXTRAPATHS:prepend = "${THISDIR}/files:"

LICENSE = "MIT"
LIC_FILES_CHKSUM =  "file://${WORKDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit module

FILES:${PN} = "/opt"

S = "${WORKDIR}/git"

PR = "r0"
PV = "4.0"

SRC_URI:append = " \
                git://github.com/PandABlocks/PandABlocks-server;branch=master;protocol=https;name=pandablocks-driver \
                file://COPYING.MIT \
                file://CONFIG \
                file://Makefile \
                "

SRCREV_pandablocks-driver = "8debbfa3b9ca964cbb583d4259917aefaa131e66"

PROVIDES = "panda-driver"


do_configure(){
        cp -f ${WORKDIR}/CONFIG ${S}/
        cp -f ${WORKDIR}/Makefile ${S}/
}

do_install() {
        install -d ${D}/opt/bin
        install -m 0755 ${S}/build/driver/panda.ko ${D}/opt/bin
}

