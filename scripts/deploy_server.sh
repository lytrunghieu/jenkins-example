#!/bin/bash

HEROKU_APP_NAME=$1
HEROKU_API_KEY=$2

echo $HEROKU_API_KEY | heroku auth:token
heroku config:set HEROKU_APP_DIRECTORY=server --app $HEROKU_APP_NAME
git push --force heroku main

# git subtree pull --prefix=server https://git.heroku.com/$HEROKU_APP_NAME.git main --squash
# git checkout main
# git merge temp-branch
# git branch -D temp-branch
# git subtree pull --prefix=server https://git.heroku.com/$HEROKU_APP_NAME.git main
# git subtree push --prefix=server https://git.heroku.com/$HEROKU_APP_NAME.git main
