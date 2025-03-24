CC = gcc

SRCS = main.c \
       dependencies/cd/cd_handling.c \
       dependencies/background/background_handling.c \
       dependencies/redirection/redirection_handling.c \
       dependencies/pipe/pipe_handling.c \
       dependencies/execute/execute_handling.c

TARGET = main

all: $(TARGET)

$(TARGET): $(SRCS)
	$(CC) $(CFLAGS) $^ -o $@
