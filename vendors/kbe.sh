#!/bin/sh

java -Duser.home=/users/jwheatley -Dgrails.env=prod -jar build/libs/vendors-0.1.war
