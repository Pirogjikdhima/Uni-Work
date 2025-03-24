#include "pipe_handling.h"

bool valid_pipe_command(char *command) {
    char *position = strchr(command, '|');

    if (position != NULL) {
        if (position  == command || *(position - 1) != ' ' || *(position + 1) != ' ') {
            return false; 
        }
    }
    return true; 
}

void pipe_process(char *command){
    
    
    if (!valid_pipe_command(command)) {
        fprintf(stderr, "\033[31m");
        fprintf(stderr, "Error: The '|' must be surrounded by spaces.\n");
        return;
    }

    char *command1 = strtok(command,"|");
    char *command2 = strtok(NULL,"|");

    if (strcmp(command1, " ") == 0 || strcmp(command2, " ") == 0) {
        fprintf(stderr, "\033[31m");
        fprintf(stderr, "Invalid pipe command\n");
        return;
    }
    int p[2];
    
    if (pipe(p) == -1) {
        fprintf(stderr, "\033[31m");
        fprintf(stderr, "Pipe creation failed\n");
        return;
    }

    pid_t pid1 = fork();
        
    if(pid1 < 0){
        fprintf(stderr, "\033[31m");
        perror("Error forking process");
    }
    
    if (pid1 == 0) {

        close(p[0]);
        dup2(p[1], STDOUT_FILENO);
        close(p[1]);

        char *token = strtok(command1, " ");
        char *args[64];
        int i = 0;

        while (token != NULL) {
            args[i++] = token;
            token = strtok(NULL, " ");
        }
        args[i] = NULL;

        if (execvp(args[0], args) == -1) {
            fprintf(stderr, "\033[31m");
            perror("Command execution failed");
        }
        exit(1);
    } 
    else {

        pid_t pid2 = fork();
            
        if(pid2 < 0){
            fprintf(stderr, "\033[31m");
            perror("Error forking process");
        }
        
        if (pid2 == 0) {

            close(p[1]);
            dup2(p[0], STDIN_FILENO);
            close(p[0]);

            char *token = strtok(command2, " ");
            char *args[64];
            int i = 0;

            while (token != NULL) {
                args[i++] = token;
                token = strtok(NULL, " ");
            }
            args[i] = NULL;

            if (execvp(args[0], args) == -1) {
                fprintf(stderr, "\033[31m");
                perror("Command execution failed");
            }
            exit(1);
        } 
        else {
            close(p[0]);
            close(p[1]);
            waitpid(pid1, NULL, 0);
            waitpid(pid2, NULL, 0);
        }
    }

}