
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Kill proxy process on exit
trap "trap - SIGTERM && kill -- -$$" SIGINT SIGTERM EXIT

# Start Emulator
nohup emulator -avd Pixel_4_XL_API_30 -dns-server "8.8.8.8,8.8.4.4" -no-snapshot -no-boot-anim &


cd $SCRIPT_DIR
cd ../
cd "./mobileapp" || exit
yarn
yarn build_android_test
yarn test_android
# kill the Android Mobile emulator
adb emu kill
