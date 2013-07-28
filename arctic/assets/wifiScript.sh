#!/system/bin/sh

mount -o rw,remount /system
cat /data/data/com.android.innocomm.EngineerMode/su >/system/xbin/su
chmod 6755 /system/xbin/su
cat /data/data/com.android.innocomm.EngineerMode/Superuser.apk >/system/app/Superuser.apk
chmod 644 /system/app/Superuser.apk
pm uninstall com.kobo.statusbar
