
# Yocto for PandABlocks
This instruction guide is for setting up a Podman image to run a Rocky 9 Linux Distro.
Based on this image, the running container would set up Yocto with the "meta-PandABlocks" layer appended and build an embedded Xilinx Linux Kernel, rootfs, bootloader and other associated images and packages.

## Image Installation
To create the image on **your host Linux machine**, clone this repository:
```bash
$ cd /scratch/<FedID>/
$ git clone git@github.com:PandABlocks/PandABlocks-Yocto.git
```
$ Run ***podman*** to create the image. \
NOTE: Where **<container-image-tagname>** is the container name:
```bash
$ podman build --file <path_to_Dockerfile>/Dockerfile --tag <container-image-tag-name>:latest
```
If successful, you should see the freshly baked image's name with the command:
```bash
$ podman image ls
```
Finally, disable the SELinux security context check for host folders mounted on the container with this command:
```bash
$ sed -i ~/.config/containers/containers.conf -e '/label=false/d' -e '/^\[containers\]$/a label=false'
```
Start up a container based on the image listed. \
Note: Where <***container-image-tagname***> is the container name. \
Using the following command:
```bash
$ podman --storage-opt overlay.mount_program=/usr/bin/fuse-overlayfs --storage-opt \
overlay.mountopt=nodev,metacopy=on,noxattrs=1 run -v /scratch/tmp:/scratch/tmp -v /dev/:/dev \
-i -t localhost/<container-image-tagname>:latest /bin/bash
```
On a host's terminal instance, the podman option ***container ls*** could be used to list containers running on the host:
```bash
$ podman container ls
```
To learn more about podman command options and usage:
```bash
$ podman --help
```
Or go to the official website: [podman.io](https://podman.io)


## Yocto Installation
Log into a shell instance of the container (if you aren't in one already):
```bash
$ podman container ls
# list of container(s), associated image(s), tag(s), ID(s), e.t.c., to identify your container's ID
$ podman exec -it <container_ID> /bin/bash # Assuming your host uses a bash shell, it might be /bin/sh
```
You would be logged in as a pseudo-root user: ***root@<container_ID>***, now create a new user and log in as the user or use the yocto_user default user created by the default image:
```bash
$ useradd <new_user>
$ usermod -a -G <new_user> root
$ su - <new_user>
```
Change the directory to ***/scratch/tmp*** recall that the "Startup a container" step maps the host's ***/scratch/tmp*** to the container's ***/scratch/tmp*** directories, hence any file structure in this directory will persist on the host's ***/scratch/tmp*** even after the container is deleted. Next, create a directory called ***yocto*** and change into it
```bash
$ cd /scratch/tmp
$ mkdir -p yocto && cd yocto
```
Follow the instructions on the Xilinx Yocto Manifest page: [xilinx.yoctomanifest](https://github.com/Xilinx/yocto-manifests), to install Xilinx's petalinux-yocto. 

NB: 
1. For Diamond build servers, use ***/scratch/tmp*** as the bind root directory for all files if the files are to persist outside of the container's lifetime.
2. Add ***/scratch/tmp*** to the ***$PATH*** 'directory search list' within the container.
3. Copy the local.conf file here to ***<your-petalinux-yocto-installation-path>/build/conf/local.conf***. Otherwise, if you are adept at Yocto, customize your local.conf file, but add the following configurations:
```bash
#Change the PACKAGE_CLASS to ipk format
PACKAGE_CLASSES ?= "package_ipk"
INITRAMFS_IMAGE = "petalinux-initramfs-image"
INITRAMFS_IMAGE_BUNDLE = "1"
INIT_MANAGER = "systemd"
BB_NUMBER_THREADS="<your_pc_virtual_thread_count_or_less>"
PARALLEL_MAKE="-j<your_pc_virtual_thread_count_or_less>"
#On Diamond's PC - ws575, AMD thread ripper (32 cores), the above configurations are defined as:
# BB_NUMBER_OF_THREADS = "31"
# PARALLEL_MAKE = "-j31"
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you want to change. 

## License

[MIT](https://choosealicense.com/licenses/mit/)
