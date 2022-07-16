source ~/computers.git/mac/zsh/.zshrc-git
source ~/.zshrc-tab-completions
source ~/.zshrc-key-bindings
source ~/.zshrc-misc

source ~/.aliases

##
## Dotfile linking to home dir
##
cd
find ~/computers.git/    -type f -iname ".zshrc*" |  grep -v trash | grep -v work | xargs --delimiter '\n' --max-args=1 ln -sr 2>/dev/null
unlink .zshrc
#find ~/computers.git/antec -type f -iname ".zshrc" |  grep -v trash | xargs --delimiter '\n' --max-args=1 ln -sr
ln -sr ~/computers.git/antec/.zshrc

PATH=${PATH}:$HOME/github/binaries/ubuntu/bin/

#touch ~/.hushlogin

PROMPT='%F{173}antec %F{099}%D{%a %d %B %G} %F{179}%t>%f ' # Linux

alias bat='batcat --plain --paging=never'
alias m='mount_all_manually_local.sh'
alias cl='cleanup_videos_current_dir.sh 2>/dev/null'

bindkey -s "^[i"  'sh ~/bin/mv_promote_file_plus_1.sh '
bindkey -s "^[z"  'source ~/computers.git/antec/.zshrc\n'
bindkey -s "^[l"  'ls --color -lS --reverse --human-readable --almost-all\n'
bindkey -s "^l"  'locate.sh '
bindkey -s "^[d"      "du_non_recursive.sh\n"
bindkey -s "^[D"      "du.sh\n"
#bindkey -s "^[m"  'sh ~/bin/mv_promote_file_plus_1.sh '
bindkey -s "^[m"  'mv_file_to_subdir.sh '
bindkey -s "^[t"  'tail -10f /tmp/cron_ffplay_before_perl.log | sed --unbuffered '\''s/.*from //'\'' | sed --unbuffered '\''s/:$//'\''\n'

cd
unlink ~/videos; ln -s /media/sarnobat/3TB/disks/thistle/videos ~/
unlink ~/other; ln -s /media/sarnobat/e/misc_sync_master ~/other
unlink mediasarnobat; ln -s /media/sarnobat/ mediasarnobat
#unlink /media/sarnobat/sarnobat
popd >/dev/null
#ls -1d /net/nuc2017/media/sarnobat/* | shuf | head &
#sudo service autofs restart && cd /media/sarnobat/e/other &&  DISPLAY=:0 feh --randomize --slideshow-delay 2 --fullscreen --zoom max &!
cat <<EOF
source ~/computers.git/mac/zsh/.zshrc-screensaver
source ~/computers.git/mac/zsh/.zshrc-buildroot
EOF
# Show dock on xrdp
cat <<EOF > ~/.xsessionrc
export GNOME_SHELL_SESSION_MODE=ubuntu
export XDG_CURRENT_DESKTOP=ubuntu:GNOME
export XDG_CONFIG_DIRS=/etc/xdg/xdg-ubuntu:/etc/xdg
gnome-terminal
EOF
sudo sed -i 's/<allow_inactive>no</<allow_inactive>yes</' /usr/share/polkit-1/actions/org.freedesktop.color.policy
sudo sed -i 's/<allow_any>.*</<allow_any>yes</' /usr/share/polkit-1/actions/org.freedesktop.color.policy


cd ~/videos/ >/dev/null
#cd /media/sarnobat/unmirrored/trash/buildroot-2021.12/docker-buildroot
#echo "/media/sarnobat/unmirrored/trash/buildroot-2021.12/docker-buildroot/scripts/run.sh vi /buildroot_output/build/linux-headers-5.4.58/arch/x86/boot/main.c"
#echo "scripts/run.sh make linux-rebuild"
#echo "scripts/run.sh /buildroot_output/images/start-qemu.sh"
#echo "scripts/run.sh cflow  /buildroot_output/build/linux-5.4.58/init/main.c"
echo "xemacs -geometry 80x40+1600+100 ~/.zshrc ~/bin/cleanup_videos_current_dir.sh ~/computers.git/mac/bin/mwk_snippets_organize.sh"
#cd /media/sarnobat/3TB/disks
#cd "/media/sarnobat/unmirrored/trash/buildroot-2021.12/docker-buildroot/"
#tail -f /tmp/cron_ffplay.log
#tail -f /tmp/rsync_3tb_weekly_full.log
#tail -f /tmp/cron_ffplay_before_perl.log
echo "gvim is a graphical vim editor for linux"
echo "shellscript_path_checker.sh - check for broken paths"
cat <<EOF
after reboot:
(-) fix nfs mount on nuc2020 so yurl downloads work
(-) sudo unmount /dev/disk/by-id/ata-WDC_WD30EZRX-00SPEB0_WD-WCC4EK8ZSZ3D-part1

	lsblk -o name,size,fstype,label,model,serial,mountpoint
	sudo mount /dev/disk/by-partuuid/834ead1a-db63-40e5-b8b6-466038547327 /media/sarnobat/3TB
