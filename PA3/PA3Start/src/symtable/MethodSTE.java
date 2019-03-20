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
		return "mName: " + getSTEName();
	}

	public Signature getSignature(){
		return mSignature;
	}

}
