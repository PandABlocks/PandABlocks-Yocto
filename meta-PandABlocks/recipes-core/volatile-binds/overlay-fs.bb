SUMMARY = "Package to configure the Overlay FS on panda-packages.ext4 - based on overlafs.bbclass"

# FILESEXTRAPATHS:prepend = "${THISDIR}/files:"

LICENSE = "MIT"
LIC_FILES_CHKSUM =  "file://${WORKDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit overlayfs

# OVERLAYFS_WRITEABLE_PATHS[data] += "" # Writable  file system

# DISTRO_FEATURES:append = " overlayfs"
