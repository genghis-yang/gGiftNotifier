function handler () {
  EVENT_DATA=$1
  bash -c "./gGiftNotifier"
  RESPONSE="{\"statusCode\": 200, \"body\": \"Run monitoring successfully!\"}"
  echo $RESPONSE
}

