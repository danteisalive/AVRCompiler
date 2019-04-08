
import meggy.Meggy;

class Test2 {		//Main class
	public static void main(String[] whatever){

	}
}

class A {
	Meggy.Color[] 	cArray;
	int[]			aArray;
	boolean			b;

	public Meggy.Color[] mth1(byte tempByte, int a, Meggy.Color c){

		B 				BClass;

		b = b && true && false && b;

		b = (a  < 10) && (tempByte < 500);

		a = tempByte + (byte)10;
		a = a + tempByte + 10 + (byte)100;


		a = tempByte - (byte)11;
		a = a - tempByte - 11 - (byte)12;

		a = tempByte * (byte)234;

		c = Meggy.getPixel((byte)6, tempByte);
		b = Meggy.checkButton(Meggy.Button.Right) == false;

		b = !false && ! true && b;

		a = aArr[10 + (byte)5];
		c = cArr[(byte)3 - (byte)19];

		a = BClass.func2();




		return new Meggy.Color[100];
	}

	public Meggy.Color mth3(int a, byte b){
		return Meggy.Color.RED;
	}
}

class B {
	public int func2(){
		return 6;
	}
}
