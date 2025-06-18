# panda-initramfs-module-pinit.bb recipe file
# custom PandA recipe shell script to extend
# the boot functionality specific to PandA

SUMMARY = "PandA recipe for rootfs SD card partition"
DESCRIPTION = "${SUMMARY}"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch

PR = "r1"

# 'pinit' - PandABlocks custom Init script
SRC_URI = " file://panda-init \
	    file://COPYING.MIT \
	  "

# Set script to executable
do_install() {
  install -d ${D}/init.d
  install -m 0755 ${WORKDIR}/panda-init ${D}/init.d/10-panda-init
}

FILES:${PN} = "/init.d/10-pinit"
