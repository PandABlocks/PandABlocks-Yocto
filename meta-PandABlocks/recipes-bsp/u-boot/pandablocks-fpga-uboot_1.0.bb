SUMMARY = "This recipe is to fetch the boot images from the remote repository for the panda device specified"
DESCRIPTION = "Set the device as: yyy_mmm for the PandaBox and \
		bbb_lll for the xu5_st1 device \
		"
FILESEXTRAPATHS:prepend = "${THISDIR}/files:"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI:append = " \
        file://COPYING.MIT \
        "
SRC_URI[sha256sum]  = "58f55a4d9530fdf465481022a96b01aae11771a29404e0916628e675326fb663"
PBFPGA_BOOT_VERSION = "4.0"
PBFPGA_BOOT_STAGING_DIR = "${TOPDIR}/PandABlocks-boot"
BOOT_VER_MAIN ?= "${PBFPGA_BOOT_VERSION}"
BOOT_VER_MAIN[doc] ?= "PandABlocks-FPGA boot images release version"

PBFPGA_BOOT_SDK = "${PBFPGA_BOOT_STAGING_DIR}/pandablocks/${BOOT_VER_MAIN}/"
PROVIDES += " panda-boot pandafpga-uboot"

PBFPGA_BOOT_URL[4.0] ?= "https://github.com/PandABlocks/PandABlocks-FPGA/releases/download/4.0/boot@PandABox-4.0.zip"
#PBFPGA_BOOT_URL:XU5-ST1[4.0]
PBFPGA_BOOT_VERSION = "4.0"
PBFPGA_BOOT_URL ?= "${@d.getVarFlag('PBFPGA_BOOT_URL', d.getVar('BOOT_VER_MAIN'))}"


PBFPGA_BOOT_ZIP ?= "pbfpga_boot_${BOOT_VER_MAIN}.zip"
PBFPGA_BOOT_DLDIR ?= "${DL_DIR}/PandaBlocks-FPGA/"
#PBFPGA_BOOT_STAGING_DIR = "${TOPDIR}/PandABlocks-FPGA"

BB_HASHEXCLUDE_COMMON += "PBFPGA_BOOT_STAGING_DIR"

# Famous: NOTE this is a SHA256 SUM, might be wrong!!!
PBFPGA_BOOT_CHECKSUM[4.0] ?= "58f55a4d9530fdf465481022a96b01aae11771a29404e0916628e675326fb663"

VALIDATE_PBFPGA_BOOT_CHECKSUM ?= '1'

EXTERNAL_BOOT_BINS ?= ""
EXTERNAL_BOOT_BINS[docs] = "Variable that defines where the pandablocks-fpga boot binaries are stored"

USE_PBFPGA_BOOT_ZIP ?= '1'
USE_PBFPGA_BOOT_ZIP[doc] = "Flag to determine whether or not to use the pbfpga-zip class. \
If enabled, the zip from path EXTERNAL_PBFPGA_BOOT_ZIP is copied to downloads/PandABlocks-FPGA, and extracted \
to tmp/pandablock-boot"

FILES:${PN} = "/boot"

python do_fetch() {
    def check_pbfpga_version():
        pbfpga_path = d.getVar("PBFPGA_BOOT_SDK")
        if not os.path.exist(pbfpga_path):
            bb.fatal("PandaBlocks-FPGA. This ussually means the wrong version is being used. \nUnable to find %s." %pbfpga_path) 
    checksum_zip_bin = d.getVar("PBFPGA_BOOT_CHECKSUM")
    checksum_actual = ""
    validate = d.getVar("VALIDATE_PBFPGA_BOOT_CHECKSUM")
    ext_zip = d.getVar("EXTERNAL_BOOT_BINS")
    pbfpga_url = d.getVar("PBFPGA_BOOT_URL")
    use_pbfpga = d.getVar('USE_PBFPGA_BOOT_ZIP')
    #Famous TO-DO Possible that there are a couple of more conditions this 'conditional blob' ought to handle, but this is a good start. 
    if use_pbfpga == "0":
        bb.note("Ensure Xilinx Bootloader recipe or 3rd-party binary is enabled and configured!")
        bb.note("You have choose to disable this package!")
        return;
    elif d.getVar("WITHIN_EXT_BOOT_SDK") != '1':
        if not ext_zip and not pbfpga_url:
            bb.note("PandaBlocks-FPGA Boot Images (dtb and u-boot) are not enabled, ensure xlnx-u-boot package is built and deployed!")
    if ext_zip and os.path.exists(ext_zip):
        bb.note("Checking the local zip file checksum")
        import hashlib
        sha256sum = hashlib.sha256()
        readsize = 1024*sha256sum.block_size
        with open(ext_zip, 'rb') as f:
            for chunk in iter(lambda: f.read(readsize), b''):
                sha256hash.update(chunk)
        checksum_actual = sha256hash.hexdigest()
        if validate == '1' and checksum_zip_bin != checksum_actual:
            bb.fatal('Provided external zip\'s sha256sum does not match checksum defined in pbfpga class')
    pbfpgadldir = d.getVar("PBFPGA_BOOT_DLDIR")
    zipfilename = d.getVar("PBFPGA_BOOT_ZIP")

    pbfpgaroots = d.getVar("PBFPGA_BOOT_STAGING_DIR")
    zipchksum = os.path.join(pbfpgaroots, zipfilename + ".chksum")

    try:
        import subprocess
        import shutil
        zipfilepath = os.path.join(pbfpgadldir, zipfilename)
        if not os.path.exists(pbfpgadldir):
            bb.utils.mkdirhier(pbfpgadldir)

        if os.path.exists(ext_zip):
            shutil.copy(ext_zip, zipfilepath)
        elif pbfpga_url:
            localdata = bb.data.createCopy(d)
            localdata.setVar('FILES_PATH', "")
            srcuri = d.expand("${PBFPGA_BOOT_URL};downloadfilename=%s" % (zipfilename))

            bb.note("Fetching PandaBlocks-FPGA boot.bin and dtb binaries from %s" % srcuri)
            fetcher =  bb.fetch2.Fetch([srcuri], localdata)
            fetcher.download()
            localpath = fetcher.localpath(srcuri)

        cmd = d.expand("\
            cp ${DL_DIR}/${PBFPGA_BOOT_ZIP} ${WORKDIR}/; \
            cd ${WORKDIR}/ ; ")
        bb.note('Unzipping PandaBlocks-FPGA boot.bin and devicetree blob binaries')
        subprocess.check_output(cmd,shell=True)
        #with open(zipchksum, "w") as f:
            #f.write(checksum_actual)

    except bb.fetch2.BBFetchException as e:
        bb.fatal(str(e))
    except RuntimeError as e:
        bb.fatal(str(e))
    except subprocess.CalledProcessError as exc:
        bb.fatal("Unable to extract pbfpga tarball: %s" % str(exc))
}

inherit deploy

do_install[depends] += "unzip-native:do_populate_sysroot"
do_install(){
    install -d ${D}/boot
    cd ${D}/boot
    ${bindir}/env unzip ${WORKDIR}/${PBFPGA_BOOT_ZIP}
}

do_deploy() {
    cd ${D}/boot
    install -Dm 0755 * -t ${DEPLOYDIR}/pandablocks-uboot
}

addtask deploy after do_install

#do_configure(){
#    cd ${WORKDIR}
#    gunzip ${WORKDIR}/${PBFPGA_BOOT_ZIP}
#}

