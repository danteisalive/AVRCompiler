/* Positive test files which check all possible statements,
variable declarations, and method calls between classes. */

import meggy.Meggy;

class PosTest1 {		//Main class
	public static void main(String[] whatever){
		Meggy.setPixel((byte)3, (byte)10, Meggy.Color.GREEN);	//Meggy.setPixel(exp, exp, exp) statement
		Meggy.setAuxLEDs(123);									//Meggy.setAuxLEDs(exp) statement
		Meggy.toneStart(Meggy.Tone.As3, 400);					//Meggy.toneStart(exp,exp) statement
		{												//Block statement
			Meggy.delay(557);							//Meggy.delay(exp) statement
			new A().mth1(	new Meggy.Color[10],		//new array statement (color)
							new int[20],				//new array statement (int)
							true,						//byte literal
							(byte)6,					//byte cast
							12,							//int
							Meggy.Color.BLUE			//Color literal
//							Meggy.Button.Right,			//Button literal
//							Meggy.Tone.C3,				//Tone literal
//							new B()
							);					//new class
			if(true && false){				//'&&' operator
				{							//nested block statement
					if(((34 == 54))){		//nested if statement and '==' operator
					}
				}
			} else if(10 < 11){				//'<' operator
				while(true){				//while statement
					new B().mth2();
				}
			} else{
			}
		}
		Meggy.delay(1000);
	}
}

class A {
	Meggy.Color[] 	a;
	int[]			b;
	boolean			c;
	byte			d;
	int				e;
	Meggy.Color		f;
//	Meggy.Button	g;
	Meggy.Tone		h;
	B				i;

	public Meggy.Color[] mth1(	Meggy.Color[] 	d,			//redefines class variable on different type
								int[] 			e,			//redefines class variable on different type
								boolean 		f,			//redefines class variable on different type
								byte 			j,
								int 			k,
								Meggy.Color 	l
//								Meggy.Button 	m,
//								Meggy.Tone 		n,
//								B 				o
								){

		Meggy.Color[] 	s;
		int[] 			t;
		boolean 		u;
		byte 			p;
		int 			q;
		Meggy.Color 	r;
//		Meggy.Button 	a;									//redefined class variable on different type
		Meggy.Tone 		b;									//redefined class variable on different type
		B 				c;									//redefined class variable on different type

		d = new A().mth1(s, e, u, p, k, r/*, g, h, c*/);		//recursive call using class variable,
															//argument and method variables

		t[6] = new B().mth2();								//calls new on different class

		return new Meggy.Color[100];
	}

	public Meggy.Color mth3(int a, byte b){
		return Meggy.Color.RED;
	}
}

class B {
	public int mth2(){
		return 6;
	}
}
