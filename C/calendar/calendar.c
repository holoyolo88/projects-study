#include<stdio.h>
#include<Windows.h>

void printCalendar(int, int, int);
int main() {
	int month;
	int days;
	int delay;
	printf("month (ex 8):");
	scanf("%d", &month);
	printf("days (ex 31):");
	scanf("%d", &days);
	printf("delay (4):");
	scanf("%d", &delay);

	printCalendar(month,days,delay);
	printCalendar(30, 9, 1);
	printCalendar(31, 10, 4);

	HANDLE h = GetStdHandle(STD_OUTPUT_HANDLE);
	SetConsoleTextAttribute(h, 7);
	int daysarr[31];
	for (int i = 0; i < 31; i++)
		daysarr[i] = i+1;
	delay = 6;
	int cnt = 0;
	printf("\n\n          2018년 12월\n");
	printf("=============================\n");
	printf("  일  월  화  수  목  금  토\n");
	for (int i=0; i <31;) {
		if (cnt == 0)
			SetConsoleTextAttribute(h, FOREGROUND_RED);
		else if (cnt == 6)
			SetConsoleTextAttribute(h, FOREGROUND_BLUE);
		else
			SetConsoleTextAttribute(h, 7);
		if (delay > 0) {
			printf("    ");
			delay--;
		}
		else {
			printf("%4d", daysarr[i]);
			i++;
		}

		cnt++;
		if (cnt == 7) {
			printf("\n");
			cnt = 0;
		}
	}


		
}

void printCalendar(int days, int month, int delay) {
	int cnt = 0;
	HANDLE h = GetStdHandle(STD_OUTPUT_HANDLE);
	printf("\n\n          2018년 %d월\n",month);
	printf("=============================\n");
	printf("  일  월  화  수  목  금  토\n");
	for (int i = 1; i <= days;) {
		if (cnt == 0)
			SetConsoleTextAttribute(h, FOREGROUND_RED);
		else if (cnt == 6)
			SetConsoleTextAttribute(h, FOREGROUND_BLUE);
		else
			SetConsoleTextAttribute(h, 7);
		if (delay > 0) {
			printf("    ");
			delay--;
		}
		else {
			printf("%4d", i);
			i++;
		}

		cnt++;
		if (cnt == 7) {
			printf("\n");
			cnt = 0;
		}
	}


}