#include <stdio.h>
#include <stdlib.h>
#include <windows.h>
#include <conio.h>
#include <time.h>
#include <unistd.h>

#define lartesia_max 20
#define gjeresia_max 20

void printMatrix(char x[lartesia_max][gjeresia_max], int *z) {
    int c[gjeresia_max];
    int i, j;

    // Shifting the elements of the matrix upward
    for (i = lartesia_max - 1; i >= 0; i--) {
        for (j = 0; j < gjeresia_max; ++j) {
            x[i + 1][j] = x[i][j];
        }
    }

    // Generating the array c with random integers
    for (j = 0; j < gjeresia_max; j++) {
        c[j] = rand() % 100 + 1;
    }

    // Modifying the matrix based on the values of c
    for (j = 0; j < gjeresia_max; ++j) {
        if (c[j] < 10) {
            x[0][j] = '|';
            (*z)++;
        } else {
            x[0][j] = ' ';
        }
    }

    HANDLE hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
    CONSOLE_CURSOR_INFO cursorInfo;
    GetConsoleCursorInfo(hConsole, &cursorInfo);
    cursorInfo.bVisible = FALSE;  // Hide the cursor
    SetConsoleCursorInfo(hConsole, &cursorInfo);
    SetConsoleCursorPosition(hConsole, (COORD){0, 0});

    // Set the text color to bright blue
	SetConsoleTextAttribute(hConsole, FOREGROUND_BLUE | FOREGROUND_INTENSITY);

    for (i = 0; i < lartesia_max; ++i) {
        for (j = 0; j < gjeresia_max; ++j) {
            printf("%c ", x[i][j]);
        }
        printf("\n");
    }

    fflush(stdout);
}

int main() {
    srand(time(NULL));
    char b[lartesia_max][gjeresia_max] = {' '};
    int x = 0;

    
    CONSOLE_CURSOR_INFO cursorInfo;
    HANDLE hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
    GetConsoleCursorInfo(hConsole, &cursorInfo);
    cursorInfo.bVisible = FALSE;  // Hide the cursor
    SetConsoleCursorInfo(hConsole, &cursorInfo);

    HWND consoleWindow = GetConsoleWindow();
    ShowWindow(consoleWindow, SW_MAXIMIZE); // Open console in full-screen mode

    while (!kbhit()) {
        printMatrix(b, &x);
    }
	FILE *fp;
	fp= fopen("nr i pikave.txt", "a");
    if (fp == NULL) {
        printf("Problem me hapjen e skedarit.\n");
        return 1;
    }

    fprintf(fp, "Numri i pikave te rena eshte: %d\n", x);
    fflush(fp);
    fclose(fp);

    cursorInfo.bVisible = TRUE;  // Restore cursor visibility before exiting
    SetConsoleCursorInfo(hConsole, &cursorInfo);

    return 0;
}
