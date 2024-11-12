# kill proxy process on exit
trap "trap - SIGTERM && kill -- -$$" SIGINT SIGTERM EXIT

nohup emulator -avd Pixel_4_XL_API_30 -dns-server "8.8.8.8,8.8.4.4" -no-snapshot -no-boot-anim &

cd ./mobileapp
yarn build_android_test
yarn test_android
# kill the Android Mobile emulator
adb emu kill