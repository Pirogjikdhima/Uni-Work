#include "background_handling.h"

void background(char *command){
    char *token = strtok(command, " ");
    char *args[64]; 
    int i = 0;

    while (token != NULL) {
        args[i++] = token;
        token = strtok(NULL, " ");
    }

    if (i > 0 && strcmp(args[i - 1], "&") == 0) {
        args[i - 1] = NULL;
        
        pid_t pid = fork();

        if(pid < 0){
            fprintf(stderr, "\033[31m");
            perror("Error forking process");
        }

        if(pid == 0){
            sleep(5);
            if (execvp(args[0], args) == -1) {
                fprintf(stderr, "\033[31m");
                perror("Error executing command");
            }
            exit(1);
        }
    } 
    else {
        fprintf(stderr, "\033[31mNot a valid background operation. Command must end with '&'.\n");
    }
    
}