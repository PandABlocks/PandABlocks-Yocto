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

To learn more about podman command options and usage:
```bash
$ podman --help
```
Or go to the official website: [podman.io](https://podman.io)


## Yocto Installation

## Usage

```python
import foobar

# returns 'words'
foobar.pluralize('word')

# returns 'geese'
foobar.pluralize('goose')

# returns 'phenomenon'
foobar.singularize('phenomena')
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change. 

## License

[MIT](https://choosealicense.com/licenses/mit/)
