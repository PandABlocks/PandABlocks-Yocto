SUMMARY = "EXT4 file system to host pandablocks packages - based on petalinux-image-minimal"

LICENSE = "MIT"
LIC_FILES_CHKSUM =  "file://${WORKDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"


require recipes-core/images/petalinux-image-minimal.bb

IMAGE_INSTALL += "\
        pandablocks-server \
        pandablocks-driver \
        "
PROVIDES = "panda-packages"
