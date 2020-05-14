struct a{
	int b, c;
	short d;
	char f;
};

struct b{
	float b;
	double d, f;
	long e;
};

int temp(int a, int b);

int main()
{
	int w[4][2], x, y, a, b;
	char z[4], *c;
	
	z[2] = 'a';
	c = "a+b";
	
	x = 0;
	y = 1;
	a = x + a * b;
	w[3][1] = temp(x, y);
	
	x = (w[a+b][a] + y) % b;
	
	if(x < y)
		x = x + y;
	else
	{
		for(a = 0; a < 20; a=a*2)
			b = b + 3;
	}
	
	while(!b)
	{
		if(a <= b || x != a + b && y)
		{
			do{
				x=x+1;
			} while(x);
		}
		b = b / 2;
	}
	
	for(; b < a; b=b+1);
	
	return 0;
}

int temp(int a, int b)
{
	int t;
	t = a * b;
	return t + a;
}