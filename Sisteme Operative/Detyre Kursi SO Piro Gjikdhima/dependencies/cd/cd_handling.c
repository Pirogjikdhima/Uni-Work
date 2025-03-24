#include "cd_handling.h"  

void cd(char *path) {
    
    char cwd[1024];

    removeSpaces(path); 
    
    if (strchr(path, ' ') != NULL) {
        if (!(path[0] == '"' && path[strlen(path) - 1] == '"') &&
            !(path[0] == '\'' && path[strlen(path) - 1] == '\'')) {
            fprintf(stderr, "\033[31mcd: string not in pwd:%s\n",path);
            return;
        }
    }

    if(path[0]=='"'&& path[strlen(path)-1]=='"'){
        path = strtok(path,"\"");
    }
    else if(path[0]=='\''&& path[strlen(path)-1]=='\''){
        path = strtok(path,"'");
    }
    if(strcmp(path, "") == 0) {
        return;
    }

    if (chdir(path) != 0) {
        fprintf(stderr, "\033[31m");
        perror("");
    }
    
    getcwd(cwd, sizeof(cwd));
    fprintf(stdout,"\033[1;33mYou are in the direcory: %s\n",cwd);
}

void removeSpaces(char *path) {
    char *start = path; 
    char *end;

    while (isspace((unsigned char)*start)) {
        start++;
    }

    if (*start == '\0') {
        *path = '\0';
        return;
    }
    end = start + strlen(start) - 1;

    while (end > start && isspace((unsigned char)*end)) {
        end--;
    }

    *(end + 1) = '\0';

    memmove(path, start, strlen(start) + 1);
}
