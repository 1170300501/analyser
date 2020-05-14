struct a{
	int b, c;
	short d;
	char f;
};

/* 重复声明 */
struct a{
	float b;
	double d, f;
	long e;
};

int temp()
{
	return 0;
}

int main()
{
	int w[4][2], x, y;
	double a, b;
	char z[4], c;
	
	int x; /* 重复声明变量 */
	
	c = z[0.2]; /* 下标不是整数 */
	c = "a+b"; /* 赋值类型不一致 */
	
	x = d; /* 未声明变量引用 */
	m = temp(); /* 未声明变量赋值 */
	y = 1;
	a = z * b;  /* 运算分量类型不一致 */
	y[1] = x + 1; /* 非数组变量 */
	printf();  /* 未声明函数 */
	w[1][0][0] = x; /* 数组访问符越界 */
	
	return 0;
}