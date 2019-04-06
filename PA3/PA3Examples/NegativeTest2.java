/* Checks type errors. Mainly undefined variables, methods and
classes, wrong type of parameters, wrong number of arguments etc. */

import meggy.Meggy;

class NegativeTest2 {
    public static void main(String[] whatever){

    }
}

class A {
	int 	vInt;
	byte 	vByte;
	int[] 	vIntArr;
	boolean	vBool;
	

	public void mth1() {
		Meggy.Color[] vColorArr;
		B vClassB;

		vInt = vInt + d;			//ERROR: Variable not defined: d
		vInt = new int[10];			//ERROR: wrong type assigned to vInt
		vIntArr = new int[vInt];	//valid
		vIntArr = new int[vBool];	//ERROR: invalid array index type
		
		new A().mth10();			//ERROR: Method not defined: mth10
		new D().mth1();				//ERROR: Class not defined: D

		vColorArr = vClassB.mth2(vIntArr, 2, (byte)10, true);		//valid
		vColorArr = vClassB.mth2(vInt, 2, (byte)10, true);	//ERROR: wrong argument type
		vColorArr = vClassB.mth2(vIntArr, 2, (byte)10); //ERROR: wrong number
														//of arguments (less)
		vColorArr = vClassB.mth2(vIntArr, 2, (byte)10, true, 2); //ERROR: wrong number
														//of arguments (more)
		vColor = vClassB.mth2(vIntArr, 2, (byte)10, true); //ERROR: Assigned to wrong type
		
		vInt = vInt * vInt;		//ERROR: wrong type. Mul operation works on two bytes only
	}

	//public void mth2(float f){	//Reference compiler Bug: cannot catch
									//udefined type in method arguments
	//}

	public void mth2(){
		float f;	//ERROR: class float not defined
	}
}

class B {
	public Meggy.Color[] mth2(int[] a, int b, byte c, boolean d){
		vInt = 1;		//ERROR: Symbol not defined in current scope
		return new Meggy.Color[a[6] + b - (byte)2 * c];
	}
}
