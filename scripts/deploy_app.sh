#!/bin/bash

# Kiểm tra nếu có đủ tham số đầu vào
if [ $# -lt 5 ]; then
    echo "Usage: $0 <app_name> <app_owner> <build_file_path> <distribution_group> <api_token>"
    exit 1
fi

# Lấy tham số từ đầu vào
APP_NAME=$1
APP_OWNER=$2
BUILD_FILE_PATH=$3
DISTRIBUTION_GROUP=$4
API_TOKEN=$5

# Kiểm tra nếu file APK tồn tại
if [ ! -f "$BUILD_FILE_PATH" ]; then
    echo "Build file not found: $BUILD_FILE_PATH"
    exit 1
fi

# Upload APK lên App Center
response=$(appcenter distribute release \
    --app "$APP_OWNER/$APP_NAME" \
    --file "$BUILD_FILE_PATH" \
    --group "$DISTRIBUTION_GROUP" \
    --token "$API_TOKEN" \
    --output json \
    --silent)

# Kiểm tra xem upload có thành công không
if [ $? -eq 0 ]; then
    echo "App upload successful!"
    # Trích xuất release_id từ phản hồi
    download_url=$(echo "$response" | jq -r '.downloadUrl')
    
    echo "Download URL: $download_url"

else
    echo "Failed to upload App."
    exit 1
fi
