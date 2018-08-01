/*	2018-6-24 made by holoyolo88
	2018-8-1 edited by holoyolo88
*/

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

	printf("점 a의 좌표를 공백으로 구분하여 입력하세요. \n");
	scanf("%d %d", &ax, &ay);
	
	if (ax<80 && ay<25 ){
		
		printf("점 b의 좌표를 공백으로 구분하여 입력하세요. \n");
		scanf("%d %d", &bx, &by);
		
		if(bx<80 && by<25){
			
			gotoxy(ax, ay);
			printf("%c a(%d,%d)", '.', ax, ay);
			gotoxy(bx, by);
			printf("%c b(%d,%d)", '.', bx, by);
			gotoxy(0, 24);
			printf("두 점 사이의 거리는 %lf입니다.", sqrt(pow((double)ax - bx, 2.0) + pow((double)ay - by, 2.0)));}
			// use sqrt((ax-bx)*(ax-bx)+(ay-by)*(ay-by)) instead
		else
		printf("유효 좌표 범위가 아닙니다.");
	}
	else
		printf("유효 좌표 범위가 아닙니다.");
}
