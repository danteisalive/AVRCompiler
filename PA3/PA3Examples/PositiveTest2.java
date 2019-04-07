/* Checks many forms of complex expressions. Covers all types of expressions. */

import meggy.Meggy;

class PositiveTest2 {		//Main class
	public static void main(String[] whatever){

	}
}

class A {
	Meggy.Color[] 	vColorArr;
	int[]			vIntArr;
	boolean			vBool;

	public Meggy.Color[] mth1(byte vByte, int vInt, Meggy.Color vColor){

		Meggy.Button 	vButton;
		Meggy.Tone 		vTone;
		B 				vClassB;

		// && operator expression
		vBool = vBool && true && false && vBool;

		// < operator expression
		vBool = (vInt  < 10) && (vByte < 500);

		// == operator expression
		vBool = (vColorArr == new Meggy.Color[10]) == (vIntArr == new int[120]) == (Meggy.Color.RED == Meggy.Color.BLUE);

		// + operator expression
		vInt = vByte + (byte)10;		//promotes to int
		vInt = vInt + vByte + 10 + (byte)100;		//mix int/byte

		// - operator (MINUS) expression
		vInt = vByte - (byte)11;		//promotes to int
		vInt = vInt - vByte - 11 - (byte)12;		//mix int/byte

		// * operator expression
		vInt = vByte * (byte)234;

		// - operator (UMINUS) expression
		vInt = -(byte)5;
		vInt = -vInt;

		// ! operator expression
		vBool = !false && ! true && vBool;

		// new and array expressions
		vInt = vIntArr[10 + (byte)5];
		vColor = vColorArr[(byte)3 - (byte)19];
		vColor = new Meggy.Color[56][12];			//nice expression :)
		vInt = new int[100][20];					//First creates and int array, then accesses 21st element

		//getPixel and checkButton expressions
		vColor = Meggy.getPixel((byte)6, vByte);
		vBool = Meggy.checkButton(Meggy.Button.Right) == false;


		// Complex mixed operator expression
		vBool = ((new Meggy.Color[(byte)5 * vByte] == vColorArr) && (-vByte < -120) && (23 < (byte)-4) && ((3+4) - 2 == 1))
				== ((new int[vByte * (byte)09][001] == 3+2) && (vInt == vIntArr[2+3-5])
				&& Meggy.checkButton(Meggy.Button.Up));


		// Call expressions
		vInt = vClassB.mth2();		//class B instance without new
		vInt = new B().mth2() + 5;	//class B instance with new
		vColor = new A().mth1((byte)5, new int[65][5], vColor)[34];		//recurrsive method call
		vBool = (this.mth3(4, (byte)6) == Meggy.Color.BLUE);		//method call in same class using this


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
