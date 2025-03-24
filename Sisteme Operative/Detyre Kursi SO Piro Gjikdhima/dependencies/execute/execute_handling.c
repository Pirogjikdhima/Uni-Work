#include "execute_handling.h"

void execute(char *command){
    
    char *token = strtok(command, " ");
    char *args[64]; 
    int i = 0;

    while (token != NULL) {
        args[i++] = token;
        token = strtok(NULL, " ");
    }
    
    args[i] = NULL;

    pid_t pid = fork();
    
    if(pid < 0){
        fprintf(stderr, "\033[31m");
        perror("Error forking process");
    }

    if(pid == 0){
        if (execvp(args[0], args) == -1) {
            fprintf(stderr, "\033[31m");
            perror("Error executing command");
        }
        exit(1);
    }
    else{
        waitpid(pid,NULL,0);
    } 
}