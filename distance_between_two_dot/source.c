#include<stdio.h>
#include<math.h> // for sqrt(), pow()
#include<Windows.h>

void gotoxy(int x, int y)
{
	COORD position = { x,y };
	SetConsoleCursorPosition(GetStdHandle(STD_OUTPUT_HANDLE), position);
}

int main(void)
{
	int ax, ay;
	int bx, by;

	printf("�� a�� ��ǥ�� �Է��ϼ���.\n");
	scanf("%d %d", &ax, &ay);
	printf("�� b�� ��ǥ�� �Է��ϼ���.\n");
	scanf("%d %d", &bx, &by);

	if (ax<80 && ay<25 && bx<80 && by<25)
	{
		gotoxy(ax, ay);
		printf("%c a(%d,%d)", '.', ax, ay);
		gotoxy(bx, by);
		printf("%c b(%d,%d)", '.', bx, by);
		gotoxy(0, 24);
		printf("�� �� ������ �Ÿ��� %lf�Դϴ�.", sqrt(pow((double)ax - bx, 2.0) + pow((double)ay - by, 2.0)));
		// use sqrt((ax-bx)*(ax-bx)+(ay-by)*(ay-by)) inste
	}
	else
		printf("��ȿ ��ǥ ������ �ƴմϴ�.");
}
