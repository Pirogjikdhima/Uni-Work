#ifndef REDIRECTION_HANDLING_H
#define REDIRECTION_HANDLING_H

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

void redirect_process(char *command);
void redirection(char *command, char *input_file, char *output_file);

#endif  