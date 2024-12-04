
# Yocto for PandABlocks
This instruction guide is for setting up a Podman image to run a Rocky 9 Linux Distro.
Based on this image, the running container would be used to set up Yocto and build a Xilinx-embedded Linux Kernel.

## Image Installation
To create the image on **your host Linux machine**, clone this repository:
```bash
$ cd /scratch/<FedID>/
$ git clone git@github.com:PandABlocks/PandABlocks-Yocto.git
```
$ Run ***podman*** to create the image:
```bash
$ podman build --file <path_to_Dockerfile>/Dockerfile --tag rocky9-yocto-container:latest
```
If successful, you should see the freshly baked image's name with the command:
```bash
$ podman image ls
```
Finally, disable the SELinux security context check for host folders mounted on the container with this command:
```bash
$ sed -i ~/.config/containers/containers.conf -e '/label=false/d' -e '/^\[containers\]$/a label=false'
```
Startup a container based on the image listed using the following command:
```bash
$ podman --storage-opt overlay.mount_program=/usr/bin/fuse-overlayfs --storage-opt \
overlay.mountopt=nodev,metacopy=on,noxattrs=1 run -v /scratch/tmp:/scratch/tmp -v /dev/:/dev \
-i -t localhost/rocky9-yocto-dev-container:latest /bin/bash
```
One a host's terminal instance, the podman option <> could be used to list containers running on the host:
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
$ podman exec -it <container_ID> /bin/bash # assuming your host uses a bash shell, it might be /bin/sh
```
You would be logged in as a psedo-root user: ***root@<container_ID>***, now create a new user and login as the user:
```bash
$ useradd <new_user>
$ usermod -a -G <new_user> root
$ su - <new_user>
```
Change directory to ***/scratch/tmp***; recall that the "Startup a container" step maps the host's ***/scratch/tmp*** to the container's ***/scratch/tmp*** directories, hence any file structure in this directory will persist on the host's ***/scratch/tmp*** even after the container is deleted. Next create a directory called ***yocto*** and change into it
```bash
$ cd /scratch/tmp
$ mkdir -p yocto && cd yocto
```
Follow the instructions delineated on the Xilinx Yocto Manifest page: [xilinx.yoctomanifest](https://github.com/Xilinx/yocto-manifests), to install yocto. 

NB: 
1. Use ***/scratch/tmp*** as the root directory for all files if the files are to persist post-container existense.
2. Add ***/scratch/tmp*** to the $PATH files list within the container.
3. Insert the following configuration macros to the ***/scratch/tmp/yocto/build/conf/local.conf*** file to limit the parallel build tasks and threads spawn during the kernel image and other boot recipe's build process:
```bash
BB_NUMBER_OF_THREADS="<your_pc_virtual_thread_count_or_less>"
PARALLEL_MAKE="-j<your_pc_virtual_thread_count_or_less>"
#On Diamond's PC - ws575, AMD thread ripper (32 cores), the above configurations are defined as:
# BB_NUMBER_OF_THREADS = "31"
# PARALLEL_MAKE = "-J31"
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change. 

## License

[MIT](https://choosealicense.com/licenses/mit/)
