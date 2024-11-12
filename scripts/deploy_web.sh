#!/bin/bash

NETLIFY_AUTH_TOKEN=$1
NETLIFY_SITE_ID=$2

# Authentication credentials
export NETLIFY_AUTH_TOKEN=$NETLIFY_AUTH_TOKEN
netlify status

# Link to Netlify server
netlify link --id $NETLIFY_SITE_ID

# Build web
cd ./web

# Deploy to Netlify server
echo "Deploying to Netlify..."
netlify deploy --prod --dir=build --site=$NETLIFY_SITE_ID
