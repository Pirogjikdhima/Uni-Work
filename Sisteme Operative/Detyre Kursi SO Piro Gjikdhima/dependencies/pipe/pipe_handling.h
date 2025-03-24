#ifndef PIPE_HANDLING_H
#define PIPE_HANDLING_H

#include <stdio.h>      
#include <stdlib.h>    
#include <string.h>     
#include <unistd.h>     
#include <sys/types.h>  
#include <sys/wait.h>   
#include <stdbool.h> 

bool valid_pipe_command(char *command);
void pipe_process(char *command);

#endif  