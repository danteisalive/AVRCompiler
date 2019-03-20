package symtable;

import symtable.Type;
import java.util.*;


public class Signature{
	private Type returnType;
	private LinkedList<Type> formals;

	public Signature(Type returnType, LinkedList<Type> formals){
		this.returnType = returnType;
		this.formals = formals;
	}

	public LinkedList<Type> getFormals(){
		return formals;
	}

	public Type getReturnType(){
		return returnType;
	}
}
