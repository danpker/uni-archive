# Daniel Parker
# University of Reading
# 215 Parallel algorithms for Bioinformatics
#
# Makefile

CC = gcc
OUT_DIR = ./bin

all: test_serial

test_serial: serial_test

serial_test:
	$(CC) ./lib/serial/test.c ./lib/serial/neighbour_joining_serial.c ./lib/serial/random.c -o $(OUT_DIR)/serial_test

