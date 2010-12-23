#define _GNU_SOURCE
#include <sched.h>
#include <stdio.h>
#include <errno.h>
#include <limits.h>
#include <sys/resource.h>
#include <stdlib.h>

#define MAX_BABBIES 4096

unsigned int stacks[MAX_BABBIES*1024];

void _child(void *arg)
{
	while(1)
		sleep(36400);
}

int main(int argc, char **argv)
{
	struct rlimit rlim;
	int nbabbies, i;
	int babbies[4096];
	
	setlinebuf(stdout);
	
	if (argc == 2 && !strcmp(argv[1], "root")) {
		if (getuid() == 0)
			printf("LI appear to be running as root.\n");
		else
			printf("EI do not appear to be running as root: uid %d, euid %d\n", getuid(), geteuid());
		return 0;
	}
	
	if (getrlimit(RLIMIT_NPROC, &rlim) < 0) {
		printf("Egetrlimit failed: errno %d\n", errno);
		return 1;
	}
	printf("Lrlim.rlim_cur = %d, rlim.rlim_max = %d\n", rlim.rlim_cur, rlim.rlim_max);
	
	printf("Lforking...\n");
	
	for (nbabbies = 0; nbabbies < MAX_BABBIES; nbabbies++) {
		babbies[nbabbies] = clone(_child, stacks+(nbabbies+1)*1024, CLONE_VM, NULL);
		if (babbies[nbabbies] < 0)
			break;
		if ((nbabbies % 500) == 0)
			printf("L...%d...\n", nbabbies);
	}
	printf("Lcloned %d babbies; waiting for remote\n", nbabbies);
	printf("S\n");
	
	/* Sooner or later, the other end will tell us to proceed. */
	read(0, &i, 1);
	
	printf("Lremote acknowledged\n");
	
	for (i = 0; i < nbabbies; i++) {
		kill(babbies[i], 9);
	}
	printf("Lall babbies killed\n");
	return 0;
}
