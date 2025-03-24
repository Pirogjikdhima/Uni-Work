#include "dependencies/cd/cd_handling.h"
#include "dependencies/pipe/pipe_handling.h" 
#include "dependencies/execute/execute_handling.h" 
#include "dependencies/background/background_handling.h" 
#include "dependencies/redirection/redirection_handling.h"   

#define MAX_COMMAND_LENGTH 1024

int main() {
    
    char command[MAX_COMMAND_LENGTH];

    while (1) {
        
        printf("\033[1;34m");//blue
        printf("\n$> ");
        fflush(stdout);

        fgets(command, MAX_COMMAND_LENGTH, stdin);
        command[strcspn(command, "\n")] = '\0';
        fprintf(stdout, "\033[32m");

        removeSpaces(command); 
        
        if (strcmp(command, "exit") == 0) {
            printf("\033[1;32m");//green
            printf("Exiting ...");
            break;
        }
        else if(strncmp(command, "cd", 2) == 0){
            if (command[2] == ' ' || command[2] == '\0') {
                cd(command + 3);
            } else {
                fprintf(stderr, "\033[31mInvalid command: path expected after 'cd'\n");
            }
        }
        else if(strchr(command, '&') != NULL){
            background(command);
        }
        else if (strchr(command, '|') != NULL) {
            pipe_process(command);
        }
        else if (strchr(command, '>') != NULL || strchr(command, '<') != NULL) {
            redirect_process(command);
        }
        else {   
            execute(command); 
        } 
    }
    return 0;
}
