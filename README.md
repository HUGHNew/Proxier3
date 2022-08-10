# Proxier

an app that can set and reset proxy on your phone

## settings way

reference here : https://github.com/theappbusiness/android-proxy-toggle

> solution: use `adb` to grant higher level permission to let app modify proxy setting

## command way

> programmatic solution [here][1]
> but it needs `android.permission.INTERACT_ACROSS_USERS` which CAN'T be granted to 3rd apps

### set

```sh
# in adb shell
settings put global http_proxy $IP:$PORT
```

### reset

```sh
# in adb shell
settings put global http_proxy :0
settings delete global http_proxy
settings delete global global_http_proxy_host
settings delete global global_http_proxy_port
```

command from @bytedance/CodeLocator [Code Here][2]

[1]: http://www.java2s.com/example/android/android-os/execute-shell-command-and-get-result.html
[2]: https://github.com/bytedance/CodeLocator/blob/main/CodeLocatorPlugin/src/main/java/com/bytedance/tools/codelocator/tools/BaseTool.kt#L190