/* Test symbol table building stage. Mainly catches duplicate symbol definitions */
/*
[28,7] Redefined symbol B
[31,15] Redefined symbol A
[37,14] Redefined symbol mth
[41,14] Redefined symbol c
[45,27] Redefined symbol a
[58,14] Redefined symbol C
[63,7] Redefined symbol A
[75,7] Redefined symbol E
[78,14] Redefined symbol E
[79,7] Redefined symbol E
Errors found while building symbol table

*/
import meggy.Meggy;

class NegativeTest1 {
    public static void main(String[] whatever){

    }
}

class A {
	int A;	//does not conflict with class A, since in different scope
	byte B;	//does not conflict with class B, since in different scope
	boolean c;
	byte B;	//ERROR: conflicts with previous declaration of B, although same type

	public void mth(Meggy.Color A){	//does not conflict with class A or variable int A
		Meggy.Color A;		//ERROR: conflicts with method argument A, although same type
		Meggy.Color B;		//does not conflict with byte B, because in diff scope

		int mth;			//does not conflict with method name, since in different scope
	}

	public void mth(){		//ERROR: conflicts with previous declaration of mth method

	}

	public void c(){		//ERROR: conflicts with variable boolean c

	}

	public void d(int a, int a){	//ERROR: symbol redefined in argument

	}
}

class B {
	public void B(){	//does not conflict with class B

	}
}

class C {
	int C;
	public void C(){	//ERROR: conflicts with variable int C, not class C

	}
}

class A {		//ERROR: conflicts with previous declaration of class A

}

class D {
	public void D(int D){	//D does not conflict

	}
}

class E {
	public void E(int E){	//method name does not conflict here with class name
		int E;				//ERROR: E conflicts with method parameter E
	}

	public void E(int E){	//ERROR: method E conflicts with previous declarationn of E
		int E;				//ERROR: E conflicts with method parameter E
	}
}
