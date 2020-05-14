typedef struct record
{
	char name;
	unsigned int id;
	double account;
} St;

char *num();
double func(int i, long j);

int main()
{
	float _a;
	float e[1][2];
	char * t;
	char z;
	
	_a = 1.02;
	b = 0.123E10;  /* xxx */
	c = 00200 + _a;
	e[0][1] = a * b;
	d = (e[0] + 0x12) * f;
	t = "%f";
	z = 'a';
	
	if ((a < b) || (d > c))
	{
		printf(t, e);
	}
	else
	{
		do{
			x++;
			if(x==7) {break;}
		} while(x <= 8);
	}
	
		
	for(;x > 4; x--)
	{
		continue;
	}
	
	return 0;
}

double func(int i, long j)
{
	int s;
	s = i + j;
	return s;
}
