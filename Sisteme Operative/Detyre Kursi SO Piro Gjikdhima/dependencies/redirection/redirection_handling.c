#include "redirection_handling.h"

void redirection(char *command, char *input_file, char *output_file){
    
    if(input_file != NULL){
        int descriptor_of_input_file = open(input_file,O_RDONLY);
        
        if(descriptor_of_input_file == -1){
            fprintf(stderr, "\033[31m");
            perror("Input file opening failed");
            return;
        }
        dup2(descriptor_of_input_file, STDIN_FILENO);
        close(descriptor_of_input_file);
    }
    
    if(output_file != NULL){
        int descriptor_of_output_file = open(output_file, O_WRONLY | O_CREAT | O_TRUNC, 0644);
        
        if(descriptor_of_output_file == -1){
            fprintf(stderr, "\033[31m");
            perror("Output file opening failed");
            return;
        }
        dup2(descriptor_of_output_file, STDOUT_FILENO);
        close(descriptor_of_output_file);
    }
}

void redirect_process(char *command){
    
    pid_t pid = fork();
    
    if(pid == 0){
        char *token = strtok(command, " ");
        char *args[64]; 
        int i = 0;

        while (token != NULL) {
            args[i++] = token;
            token = strtok(NULL, " ");
        }
        
        args[i] = NULL;
        char *input = NULL;
        char *output = NULL;
        
        for (int j = 0; j < i; j++) {
            if (strcmp(args[j], "<") == 0) {
                input = args[j + 1];
                args[j] = NULL;
            } 
            else if (strcmp(args[j], ">") == 0) {
                output = args[j + 1];
                args[j] = NULL;
            }
        }
        
        redirection(command,input,output);

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