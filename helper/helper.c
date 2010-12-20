#include <stdio.h>

int main(int argc, char **argv)
{
	FILE *logfd;

	if (argc != 2) {
		printf("usage: helper writable-path\n");
		return 1;
	}
	
	logfd = fopen(argv[1], "w");
	if (!logfd) {
		printf("AH FARGH IT COULDN'T CREATE THE GODDAMN MARKER\n");
		return 1;
	}
	fprintf(logfd, "I'm a helper!\n");
	fclose(logfd);
	
	return 0;
}
