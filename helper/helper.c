#include <stdio.h>
#include <errno.h>
#include <limits.h>
#include <sys/resource.h>
#include <stdlib.h>

int main(int argc, char **argv)
{
	struct rlimit rlim;
	
	if (getrlimit(RLIMIT_NPROC, &rlim) < 0) {
		printf("Egetrlimit failed: errno %d\n", errno);
		exit(1);
	}
	printf("Lrlim.rlim_cur = %d, rlim.rlim_max = %d\n", rlim.rlim_cur, rlim.rlim_max);

	printf("S\n");

	printf("Edone\n");	
	return 0;
}
