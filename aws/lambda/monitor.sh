function handler () {
  EVENT_DATA=$1
  bash ./gGiftNotifier
  RESPONSE="{\"statusCode\": 200, \"body\": \"Run monitoring successfully!\"}"
  echo $RESPONSE
}

