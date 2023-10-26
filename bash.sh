#!/bin/bash

echo Running in $SHELL

echo "$(tput setaf 2)		START OF THE SCRIPT		$(tput sgr 0)"


# github script start here
if git add . ; then
    echo "$(tput setaf 2)1. git add succesfull$(tput sgr 0)"
else
    echo "$(tput setaf 1)EXPCEPTION AT GIT ADD$(tput sgr 0)"
    exit $0
fi

# github commit with messages
if git commit -am "$1" ; then
    echo "$(tput setaf 2)2. git commit succesfull$(tput sgr 0)"
else
    echo "$(tput setaf 1)EXPCEPTION AT GIT COMMIT$(tput sgr 0)"
    exit $0
fi

# github push
if git push ; then
    echo "$(tput setaf 2)3. git push succesfull$(tput sgr 0)"
else
    echo "$(tput setaf 1)EXPCEPTION AT GIT PUSH$(tput sgr 0)"
    exit $0
fi

echo "$(tput setaf 2)		COMPLETE WITHOUT ERROR		$(tput sgr 0)"