package symtable;

public class MethodSTE extends STE{

	private Signature mSignature;
	private Scope mScope;

	public MethodSTE(String mName, Signature mSignature, Scope mScope){
		super(mName);
		this.mSignature = mSignature;
		this.mScope = mScope;
	}

	public Scope getScope(){
		return mScope;
	}

	public String toString(){

		return " <f0> MethodSTE | <f1> mName = " + getSTEName() +
		"| <f2> mSignature = " + mSignature.toString() +
		"| <f3> mScope ";

	}

	public Signature getSignature(){
		return mSignature;
	}

}
