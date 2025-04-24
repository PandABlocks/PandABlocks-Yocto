# panda-initramfs-module-pinit.bb recipe file
# custom PandA recipe shell script to extend
# the boot functionality specific to PandA

SUMMARY = "PandA recipe for rootfs SD card partition"
DESCRIPTION = "${SUMMARY}"
#LICENSE = "MIT"
#LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit allarch

PR = "r1"

# 'pinit' - PandABox Init script
SRC_URI = " file://pinit \
	    file://COPYING.MIT \
	  "

# Set script to executable
do_install() {
  install -d ${D}/init.d
  install -m 0755 ${WORKDIR}/pinit ${D}/init.d/10-pinit
}

FILES:${PN} = "/init.d/10-pinit"
