/* Checks many forms of complex expressions. Covers all types of expressions. */

import meggy.Meggy;

class PositiveTest2 {		//Main class
	public static void main(String[] whatever){

	}
}

class A {
	Meggy.Color[] 	cArray;
	int[]			aArray;
	boolean			b;

	public Meggy.Color[] mth1(byte vByte, int a, Meggy.Color c){

		B 				BClass;

		b = b && true && false && b;

		b = (a  < 10) && (vByte < 500);

		a = vByte + (byte)10;
		a = a + vByte + 10 + (byte)100;


		a = vByte - (byte)11;
		a = a - vByte - 11 - (byte)12;

		a = vByte * (byte)234;

		c = Meggy.getPixel((byte)6, vByte);
		b = Meggy.checkButton(Meggy.Button.Right) == false;

		b = !false && ! true && b;

		a = aArr[10 + (byte)5];
		c = cArr[(byte)3 - (byte)19];

		a = BClass.mth2();

		c = new A().mth1((byte)5, new int[65][5], c)[34];
		// b = (this.mth3(4, (byte)6) == Meggy.Color.BLUE);		//method call in same class using this


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
